package main.java.com.hotelSystem.app.util;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.controller.command.AbstractCommand;
import main.java.com.hotelSystem.controller.command.ICommand;
import main.java.com.hotelSystem.manager.AbstractCommandManager;
import main.java.com.hotelSystem.manager.AbstractDaoManager;
import main.java.com.hotelSystem.manager.AbstractServiceManager;
import main.java.com.hotelSystem.model.enums.UserType;
import org.apache.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Class configures application with initial parameters. For instance, it configures
 * {@link main.java.com.hotelSystem.manager.GenericManager} subclasses with
 * configuration parameters. Also it configures {@link LocalizedMessageFormatter}
 * with initial parameters, security map, that contains user's rights for invoking
 * {@link ICommand} object's processing
 * method.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ApplicationConfigurer {

    private static final Logger logger = Logger.getLogger(ApplicationConfigurer.class);

    /**
     * properties path
     */
    private static final String propertiesPath = "/properties.xml";

    /**
     * base name of bundle, containing message's and error's strings
     */
    private static final String messageBundle = "messageBundle";

    /**
     * datasource JNDI path
     */
    private static final String dataSourceJndiName = "java:/comp/env/jdbc/mysql";

    /**
     * calls {@link #configureApplication()}
     */
    public ApplicationConfigurer() {
        logger.info("Application configuring is started");
        configureApplication();
        logger.info("Application configuring is finished");
    }


    /**
     * executes application configuring. Configures managers, security map,
     * message formatter and data source. Loads properties via {@link Properties#loadFromXML(InputStream)}
     */
    private void configureApplication() {

        Properties mainProperties = new Properties();
        try {
            logger.info("Loading properties from file");
            mainProperties.loadFromXML(new BufferedInputStream(this.getClass().getResourceAsStream(propertiesPath)));
            logger.debug("Loading is finished: " + Arrays.toString(mainProperties.stringPropertyNames().toArray()));
            logger.info("Configuring command manager...");
            Class<? extends AbstractCommandManager> commandManager = (Class<? extends AbstractCommandManager>) Class.forName(mainProperties.getProperty("commandManager"));
            configureCommandManager(getPropsUsingKeyEnding(mainProperties, "Command"), commandManager);
            logger.info("Configuring command manager...");
            configureDataSource();
            logger.info("Command manager configuring is finished");

            logger.info("Configuring dao manager...");
            Class<? extends AbstractDaoManager> daoManager = (Class<? extends AbstractDaoManager>) Class.forName(mainProperties.getProperty("daoManager"));
            configureManager(getPropsUsingKeyEnding(mainProperties, "Dao"), daoManager, GlobalContextConstant.DAO_MANAGER, GlobalContext.getValue(GlobalContextConstant.DATA_SOURCE));
            logger.info("Dao manager configuring is finished...");

            logger.info("Configuring service manager...");
            Class<? extends AbstractServiceManager> serviceManager = (Class<? extends AbstractServiceManager>) Class.forName(mainProperties.getProperty("serviceManager"));
            configureManager(getPropsUsingKeyEnding(mainProperties, "Service"), serviceManager, GlobalContextConstant.SERVICE_MANAGER, (AbstractDaoManager) GlobalContext.getValue(GlobalContextConstant.DAO_MANAGER));
            logger.info("Service manager configuring is finished");

            configureSecurityMap(mainProperties);
            logger.info("Configuring localization...");
            Locale ruRu = new Locale("ru", "RU");
            Locale enEn = new Locale("en", "EN");
            LocalizedMessageFormatter.init(messageBundle, ruRu, enEn);
            logger.info("Localization configuring is finished");
        } catch (IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            logger.error("Exception while configuring the application", e);
        }
    }

    /**
     * Configures security map, which maps user's type to operation allowed to this user type.
     * As a key used name of user's type with ending "_i", where i - number. It calls
     * {@link #getPropsUsingKeyStarts(Properties, String)} for each user type and merges results
     * to one map. Result map is added to {@link GlobalContext} with the key
     * {@link GlobalContextConstant#SECURITY_CONFIGURATION}
     *
     * @param properties loaded properties
     */
    private void configureSecurityMap(Properties properties) {
        logger.info("Configuring security map");
        Map<UserType, List<CommandConstant>> userTypeListMap = new HashMap<>();
        Arrays.stream(UserType.values()).forEach(userType -> {
            List<CommandConstant> buffer = new ArrayList<>();
            Map<String, CommandConstant> configMap = getPropsUsingKeyStarts(properties, userType.name());
            buffer.addAll(configMap.values());
            configMap.clear();
            userTypeListMap.put(userType, buffer);
        });

        logger.debug(MessageFormat.format("secure config is ready: {0}", userTypeListMap));

        GlobalContext.addToGlobalContext(GlobalContextConstant.SECURITY_CONFIGURATION, userTypeListMap);
        logger.info("Configuring security map is finished");
    }

    /**
     * Searches all key/value pairs, which keys start with target String
     *
     * @param properties application properties
     * @param start      string, from which key must start
     * @return key/value pairs, which keys start with target String
     */
    private Map<String, CommandConstant> getPropsUsingKeyStarts(Properties properties, String start) {
        Map<String, CommandConstant> result = new HashMap<>();
        properties.entrySet()
                .stream()
                .filter(entry -> ((String) entry.getKey()).startsWith(start))
                .forEach(entry -> result.put((String) entry.getKey(), CommandConstant.fromValue(((String) entry.getValue()).trim())));
        return result;
    }

    /**
     * Searches all key/value pairs, which keys end with target String
     *
     * @param properties application properties
     * @param ending     string, from which key must end
     * @return key/value pairs, which keys start with target String
     */
    private Map<Object, Object> getPropsUsingKeyEnding(Properties properties, String ending) {
        Map<Object, Object> result = new HashMap<>();

        properties.entrySet()
                .stream()
                .filter(entry -> ((String) entry.getKey()).trim().endsWith(ending))
                .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
    }

    private void configureCommandManager(Map<Object, Object> map,
                                         Class<? extends AbstractCommandManager> commandManagerClass)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Map<CommandConstant, Class<? extends AbstractCommand>> commandMap = new HashMap<>();
        map.entrySet().stream().forEach(entry -> {
                    try {
                        commandMap.put(CommandConstant.fromValue((String) entry.getKey()),
                                (Class<? extends AbstractCommand>) Class.forName(((String) entry.getValue()).trim()));
                    } catch (ClassNotFoundException e) {
                        logger.warn("Class not found, while configuring command manager", e);
                    }
                }
        );
        logger.debug("Command map for command manager: " + commandMap);
        AbstractCommandManager manager = (AbstractCommandManager) commandManagerClass.getConstructors()[0].newInstance(commandMap);
        GlobalContext.addToGlobalContext(GlobalContextConstant.COMMAND_FACTORY, manager);
    }

    /**
     * Configures manager with appropriate params.
     *
     * @param map               manager's {@link main.java.com.hotelSystem.manager.GenericManager#keyObjectTemplateMap} param
     * @param managerClass      manager's class
     * @param saveAs            will be mapped to this key in {@link GlobalContext}
     * @param constructorParams manager's constructor parameters
     * @param <T>               manager's type
     * @param <V>               manager {@link main.java.com.hotelSystem.manager.GenericManager#keyObjectTemplateMap}'s type of value
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private <T, V> void configureManager(Map<Object, Object> map, Class<? extends T> managerClass, GlobalContextConstant saveAs, Object... constructorParams) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        Map<Class<? extends V>, Class<? extends V>> result = new HashMap<>();
        map.entrySet().forEach(entry -> {
            try {
                result.put((Class<? extends V>) Class.forName((String) entry.getKey()), (Class<? extends V>) Class.forName(((String) entry.getValue()).trim()));
            } catch (ClassNotFoundException e) {
                logger.error(MessageFormat.format("Can't find class {0}", entry.getValue()), e);
                e.printStackTrace();
            }
        });

        logger.debug(MessageFormat.format("Configuration map for manager {0}: {1}", managerClass.getName(), result));

        T manager = (constructorParams.length == 0) ? (T) managerClass.getConstructors()[0].newInstance(result) :
                (T) managerClass.getConstructors()[0].newInstance(result, constructorParams[0]);
        GlobalContext.addToGlobalContext(saveAs, manager);
    }

    /**
     * Configures data source, lookups from JNDI and adds to {@link GlobalContext} with the
     * key {@link GlobalContextConstant#DATA_SOURCE}
     */
    private void configureDataSource() {
        try {
            InitialContext initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup(dataSourceJndiName);
            GlobalContext.addToGlobalContext(GlobalContextConstant.DATA_SOURCE, dataSource);
        } catch (NamingException e) {
            logger.error("Exception while getting datasource from JNDI", e);
        }
    }

}
