package main.java.com.hotelSystem.manager.managerImpl.daoManagerImpl;

import main.java.com.hotelSystem.dao.GenericDao;
import main.java.com.hotelSystem.exception.ManagerConfigException;
import main.java.com.hotelSystem.manager.AbstractDaoManager;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;

/**
 * Manager implementation for DAOs, that are using {@link DataSource} for
 * servicing invoked methods. DataSource object is injected using
 * {@link PropertyDescriptor} object and reflection API.
 * <p>
 * The restriction on DAO object is that it must have field with datasource
 * type and get/set pair methods, so that property
 * descriptor can lookup Bean property and inject datasource object. Also DAO
 * object must have constructor without parameters
 * </p>
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see DataSource
 * @see PropertyDescriptor
 */
public class DataSourceDaoManagerImpl extends AbstractDaoManager {

    private static final String NO_CONNECTION_FIELD = "There\\'s no DataSource type field declared in Dao class {0}";

    private static final String NO_SET_CONNECTION_METHOD = "There\\'s no set method for DataSource type field in class {0}";

    private static final String CREATE_OBJECT_EXCEPTION_MESSAGE = "Exception caused while " +
            "creating object of type {0} or injecting dataSource to instance";

    private static final Logger logger = Logger.getLogger(DataSourceDaoManagerImpl.class);

    /**
     * this instance will be injected to all DAO, that are created
     * by this type of manager.
     */
    private ConnectionAllocator allocator;

    /**
     * @param keyObjectTemplateMap key/extra_info for instantiation target object map
     * @param allocator
     */
    public DataSourceDaoManagerImpl(Map<Class<? extends GenericDao>, Class<? extends GenericDao>> keyObjectTemplateMap,
                                    ConnectionAllocator allocator) {
        super(keyObjectTemplateMap);
        this.allocator = allocator;
    }

    /**
     * {@inheritDoc}
     * Also injects
     *
     * @param objectClass {@inheritDoc}
     * @param <V>         {@inheritDoc}
     * @return {@inheritDoc}. Also {@link DataSource} object will be injected.
     * @throws ManagerConfigException {@inheritDoc} Also if there's no {@link DataSource} field
     *                                in {@link GenericDao} subclass, or {@link PropertyDescriptor}
     *                                of dataSource's field can't find set method or there was an
     *                                exception during the invoking this method.
     */
    @Override
    protected <V extends GenericDao> V instantiate(Class<V> objectClass) throws ManagerConfigException {
        V result;
        try {
            result = objectClass.newInstance();
            Field connectionField = getConnectionAllocatorField(objectClass);
            if (connectionField == null) {
                throw new ManagerConfigException(MessageFormat.format(NO_CONNECTION_FIELD, objectClass.getName()));
            }
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(connectionField.getName(), objectClass);
            Method setMethod = propertyDescriptor.getWriteMethod();
            if (setMethod == null) {
                throw new ManagerConfigException(MessageFormat.format(NO_SET_CONNECTION_METHOD, objectClass.getName()));
            }
            setMethod.invoke(result, allocator);

            logger.debug(MessageFormat.format("Method getInstance of class {0} returned {1}",
                    this.getClass().getName(), result.getClass().getName()));
            return result;
        } catch (InstantiationException | IllegalAccessException | IntrospectionException | InvocationTargetException e) {
            throw new ManagerConfigException(MessageFormat.format(CREATE_OBJECT_EXCEPTION_MESSAGE, objectClass.getName()));
        }
    }

    /**
     * Scans Class object for {@link Connection Connection} type or it subtypes field
     *
     * @param objectClass object, that will be scanned for datasource's field existence.
     * @return {@link Field} which type is {@link Class#isAssignableFrom(Class)} Connection. If there's
     * no target fields, returns null.
     */
    private Field getConnectionAllocatorField(Class objectClass) {
        return Arrays.stream(objectClass.getDeclaredFields())
                .filter(f -> f.getType().isAssignableFrom(ConnectionAllocator.class))
                .findFirst()
                .orElse(null);
    }

}