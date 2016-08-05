package main.java.com.epam.project4.app;

import main.java.com.epam.project4.app.constants.CommandConstant;
import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.manager.AbstractCommandManager;
import main.java.com.epam.project4.manager.AbstractDaoManager;
import main.java.com.epam.project4.manager.AbstractServiceManager;
import main.java.com.epam.project4.model.entity.enums.UserType;
import org.apache.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ApplicationConfigurer {

    private static final Logger logger = Logger.getLogger(ApplicationConfigurer.class);

    private static final String propertiesPath = "/properties.xml";

    private static final String messageBundle = "messageBundle";

    private static final String dataSourceJndiName = "java:/comp/env/jdbc/mysql";

    public ApplicationConfigurer() {
        logger.info("Application configuring is started");
        configureApplication();
        logger.info("Application configuring is finished");
    }


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
        System.out.println(userTypeListMap);

        GlobalContext.addToGlobalContext(GlobalContextConstant.SECURE_CONFIGURATION, userTypeListMap);
        logger.info("Configuring security map is finished");
    }

    private Map<String, CommandConstant> getPropsUsingKeyStarts(Properties properties, String start) {
        Map<String, CommandConstant> result = new HashMap<>();
        properties.entrySet()
                .stream()
                .filter(entry -> ((String) entry.getKey()).startsWith(start))
                .forEach(entry -> result.put((String) entry.getKey(), CommandConstant.fromValue(((String) entry.getValue()).trim())));
        return result;
    }

    private Map<Object, Object> getPropsUsingKeyEnding(Properties properties, String ending) {
        Map<Object, Object> result = new HashMap<>();

        properties.entrySet()
                .stream()
                .filter(entry -> ((String) entry.getKey()).trim().endsWith(ending))
                .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
    }

    private void configureCommandManager(Map<Object, Object> map, Class<? extends AbstractCommandManager> commandManagerClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Map<CommandConstant, Class<? extends AbstractCommand>> commandMap = new HashMap<>();
        map.entrySet().stream().forEach(entry -> {
                    try {
                        commandMap.put(CommandConstant.fromValue((String) entry.getKey()),
                                (Class<? extends AbstractCommand>) Class.forName(((String) entry.getValue()).trim()));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        );
        logger.debug("Command map for command manager: " + commandMap);
        AbstractCommandManager manager = (AbstractCommandManager) commandManagerClass.getConstructors()[0].newInstance(commandMap);
        GlobalContext.addToGlobalContext(GlobalContextConstant.COMMAND_FACTORY, manager);
    }

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
                (T) managerClass.getConstructors()[0].newInstance(result, constructorParams[0]); //TODO!!!!тут только один параметр передаем из варарга
        GlobalContext.addToGlobalContext(saveAs, manager);
    }

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
