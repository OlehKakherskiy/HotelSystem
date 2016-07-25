package app;

import app.constants.CommandConstant;
import app.constants.GlobalContextConstant;
import controller.command.AbstractCommand;
import manager.AbstractCommandManager;
import manager.DaoManager;
import manager.GenericServiceManager;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ApplicationConfigurer {

    public static void main(String[] args) {
        new ApplicationConfigurer().configureApplication();
    }

    private void configureApplication() {
        Properties mainProperties = new Properties();
        try {
            mainProperties.loadFromXML(new BufferedInputStream(new FileInputStream("resources/properties.xml")));
            Class<? extends AbstractCommandManager> commandManager = (Class<? extends AbstractCommandManager>) Class.forName(mainProperties.getProperty("commandManager"));
            configureCommandManager(getPropsUsingKeyEnding(mainProperties, "Command"), commandManager);

            //TODO: вызвать метод configureDataSource, когда будет деплоймент

            Class<? extends DaoManager> daoManager = (Class<? extends DaoManager>) Class.forName(mainProperties.getProperty("daoManager"));
            configureManager(getPropsUsingKeyEnding(mainProperties, "Dao"), daoManager, GlobalContextConstant.DAO_MANAGER, GlobalContext.getValue(GlobalContextConstant.DATA_SOURCE));

            Class<? extends GenericServiceManager> serviceManager = (Class<? extends GenericServiceManager>) Class.forName(mainProperties.getProperty("serviceManager"));
            configureManager(getPropsUsingKeyEnding(mainProperties, "Service"), serviceManager, GlobalContextConstant.SERVICE_MANAGER, (DaoManager) GlobalContext.getValue(GlobalContextConstant.DAO_MANAGER));

        } catch (IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private Map<Object, Object> getPropsUsingKeyEnding(Properties properties, String ending) {
        Map<Object, Object> result = new HashMap<>();

        properties.entrySet()
                .stream()
                .filter(entry -> ((String) entry.getKey()).endsWith(ending))
                .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
    }

    private void configureCommandManager(Map<Object, Object> map, Class<? extends AbstractCommandManager> commandManagerClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Map<CommandConstant, Class<? extends AbstractCommand>> commandMap = new HashMap<>();
        map.entrySet().stream().forEach(entry -> {
                    try {
                        commandMap.put(CommandConstant.fromValue((String) entry.getKey()),
                                (Class<? extends AbstractCommand>) Class.forName((String) entry.getValue()));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        );
        AbstractCommandManager manager = (AbstractCommandManager) commandManagerClass.getConstructors()[0].newInstance(commandMap); //TODO: не получился маппинг на нужный конструктор
        GlobalContext.addToGlobalContext(GlobalContextConstant.COMMAND_FACTORY, manager);
    }

    private <T, V> void configureManager(Map<Object, Object> map, Class<? extends T> managerClass, GlobalContextConstant saveAs, Object... constructorParams) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException, InstantiationException {
        Map<Class<? extends V>, Class<? extends V>> result = new HashMap<>();
        map.entrySet().forEach(entry -> {
            try {
                Class<? extends V> clazz = (Class<? extends V>) Class.forName((String) entry.getValue());
                result.put(clazz, clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        T manager = (constructorParams.length == 0) ? (T) managerClass.getConstructors()[0].newInstance(result) :
                (T) managerClass.getConstructors()[0].newInstance(result, constructorParams[0]); //TODO!!!!тут только один параметр передаем из варарга
        GlobalContext.addToGlobalContext(saveAs, manager);
    }

    private void configureDataSource(){
        try {
            InitialContext initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup("java:/comp/env/jdbc/mysql");
            GlobalContext.addToGlobalContext(GlobalContextConstant.DATA_SOURCE,dataSource);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

}
