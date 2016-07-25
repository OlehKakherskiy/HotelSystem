package model.service.manager.managerImpl;

import model.dao.GenericDao;
import model.dao.manager.DaoManager;
import model.exceptions.ManagerConfigException;
import model.service.AbstractService;
import model.service.manager.GenericServiceManager;

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
    protected <V extends AbstractService> V getObjectHook(Class<V> objectClass) throws IllegalAccessException, InstantiationException, InvocationTargetException, ManagerConfigException {
        Constructor<V> constructor = (Constructor<V>) objectClass.getConstructors()[0];
        List<Object> preparedParams = new ArrayList<>(constructor.getParameterCount());
        V result = null;
        preparedParams.addAll(Arrays.asList(constructor.getParameters())
                .stream()
                .map(Parameter::getType)
                .map(this::createNewParameterInstance)
                .collect(Collectors.toList()));

        result = newInstance(constructor, preparedParams);
        return result;
    }

    private <V extends AbstractService> V newInstance(Constructor<V> constructor, List<Object> paramInstance) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return constructor.newInstance(paramInstance.toArray());
    }

    private Object createNewParameterInstance(Class clazz) throws ManagerConfigException {
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
