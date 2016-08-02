package main.java.com.epam.project4.manager.managerImpl;

import main.java.com.epam.project4.exception.ManagerConfigException;
import main.java.com.epam.project4.manager.AbstractDaoManager;
import main.java.com.epam.project4.model.dao.GenericDao;

import javax.sql.DataSource;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class DataSourceDaoManagerImpl extends AbstractDaoManager {

    private static final String NO_DATASOURCE_FIELD = "There\\'s no DataSource type field declared in Dao class {0}";

    private static final String NO_SET_DATASOURCE_METHOD = "There\\'s no set method for DataSource type field in class {0}";

    private static final String CREATE_OBJECT_EXCEPTION_MESSAGE = "Exception caused while " +
            "creating object of type {0} or injecting dataSource to instance";

    private DataSource dataSource;

    public DataSourceDaoManagerImpl(Map<Class<? extends GenericDao>, Class<? extends GenericDao>> keyObjectTemplateMap,
                                    DataSource dataSource) {
        super(keyObjectTemplateMap);
        this.dataSource = dataSource;
    }

    @Override
    protected <V extends GenericDao> V instantiate(Class<V> objectClass) throws ManagerConfigException {
        V result;
        try {
            result = objectClass.newInstance();
            Field dataSourceField = getDataSourceField(objectClass);
            if (dataSourceField == null) {
                throw new ManagerConfigException(MessageFormat.format(NO_DATASOURCE_FIELD, objectClass.getName()));
            }
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(dataSourceField.getName(), objectClass);
            Method setMethod = propertyDescriptor.getWriteMethod();
            if (setMethod == null) {
                throw new ManagerConfigException(MessageFormat.format(NO_SET_DATASOURCE_METHOD, objectClass.getName()));
            }
            setMethod.invoke(result, dataSource);
        } catch (InstantiationException | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            throw new ManagerConfigException(MessageFormat.format(CREATE_OBJECT_EXCEPTION_MESSAGE, objectClass.getName()));
        }
        return result;
    }

    private Field getDataSourceField(Class objectClass) {
        return Arrays.stream(objectClass.getDeclaredFields())
                .filter(f -> f.getType().isAssignableFrom(DataSource.class))
                .findFirst()
                .orElse(null);
    }
}