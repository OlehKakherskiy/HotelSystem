package manager.managerImpl;

import manager.DaoManager;
import model.dao.GenericDao;
import model.exceptions.ManagerConfigException;

import javax.sql.DataSource;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class DataSourceDaoManagerImpl extends DaoManager {

    private DataSource dataSource;

    public DataSourceDaoManagerImpl(Map<Class<? extends GenericDao>, Class<? extends GenericDao>> keyObjectTemplateMap,
                                    DataSource dataSource) {
        super(keyObjectTemplateMap);
        this.dataSource = dataSource;
    }

    @Override
    protected <V extends GenericDao> V getObjectHook(Class<V> objectClass) throws ManagerConfigException{
        V result = null;
        try {
            result = objectClass.newInstance();
            Field dataSourceField = getDataSourceField(objectClass);
            if(dataSourceField == null){
                throw new ManagerConfigException("There's no DataSource type field declared in Dao class "+objectClass.getName());
            }
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor(dataSourceField.getName(),objectClass);
            Method setMethod = propertyDescriptor.getWriteMethod();
            if(setMethod == null){
                throw new ManagerConfigException("There's no set method for DataSource type field in class "+objectClass.getName());
            }
            setMethod.invoke(result,dataSource);
        } catch (InstantiationException|IllegalAccessException|IntrospectionException|InvocationTargetException e) {
            throw new ManagerConfigException("Exception caused while creating object of type "+objectClass.getName()+"or injecting dataSource to instance");
        }
        return result;
    }

    private Field getDataSourceField(Class objectClass ){
        return Arrays.stream(objectClass.getDeclaredFields())
                .filter(f->f.getType().isAssignableFrom(DataSource.class))
                .findFirst()
                .orElse(null);
    }
}