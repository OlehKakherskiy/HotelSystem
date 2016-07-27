package main.java.com.epam.project4.manager.managerImpl;

import main.java.com.epam.project4.manager.DaoManager;
import main.java.com.epam.project4.manager.GenericServiceManager;
import main.java.com.epam.project4.model.dao.GenericDao;
import main.java.com.epam.project4.model.exception.ManagerConfigException;
import main.java.com.epam.project4.model.service.AbstractService;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ServiceManagerImpl extends GenericServiceManager {

    private DaoManager daoManager;

    public ServiceManagerImpl(Map<Class<? extends AbstractService>, Class<? extends AbstractService>> keyObjectTemplateMap,
                              DaoManager daoManager) {
        super(keyObjectTemplateMap);
        this.daoManager = daoManager;
    }

    @Override
    protected <V extends AbstractService> V instantiate(Class<V> objectClass) throws ManagerConfigException {
        Constructor<V> constructor = (Constructor<V>) objectClass.getConstructors()[0];
        List<Object> preparedParams = new ArrayList<>(constructor.getParameterCount());
        V result = null;
        List<Class> types = Arrays.asList(constructor.getParameters()).stream().map(Parameter::getType).collect(Collectors.toList());
        for (Class type : types) {
            preparedParams.add(createNewParameterInstance(type));
        }
        try {
            result = newInstance(objectClass, constructor, preparedParams);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new ManagerConfigException(String.format("Exception caused while instantiating service of type %s with params:%s",
                    objectClass.getName(), Arrays.toString(preparedParams.toArray())), e);
        }
        return result;
    }

    private <V extends AbstractService> V newInstance(Class<V> clazz, Constructor<V> constructor, List<Object> paramInstance) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return (paramInstance.isEmpty()) ? clazz.newInstance() : constructor.newInstance(paramInstance.toArray());
    }

    private Object createNewParameterInstance(Class clazz) throws ManagerConfigException {
        System.out.println(clazz);
        if (GenericDao.class.isAssignableFrom(clazz)) {
            return getDaoInstance(clazz);
        } else if (AbstractService.class.isAssignableFrom(clazz)) {
            return this.getInstance(clazz);
        } else {
            throw new ManagerConfigException(String.format("Exception caused because of %s service's constructor parameter of type %s. There is no" +
                    "strategy how to instantiate this type.", this.getClass().getCanonicalName(), clazz.getCanonicalName()));
        }
    }

    private <V extends GenericDao> V getDaoInstance(Class<V> daoType) {
        return daoManager.getInstance(daoType);
    }
}