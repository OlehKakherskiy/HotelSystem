package model.service.manager.managerImpl;

import model.dao.GenericDao;
import model.dao.manager.DaoManager;
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

    public ServiceManagerImpl(Map<Class<? extends AbstractService>, Class<? extends AbstractService>> keyObjectTemplateMap, DaoManager daoManager) {
        super(keyObjectTemplateMap);
        this.daoManager = daoManager;
    }

    @Override
    protected <V extends AbstractService> V getObjectHook(Class<V> objectClass) {
        Constructor<V> constructor = (Constructor<V>) objectClass.getConstructors()[0];
        List<Object> preparedParams = new ArrayList<>(constructor.getParameterCount());

        preparedParams.addAll(Arrays.asList(constructor.getParameters())
                .stream()
                .map(Parameter::getType)
                .map(this::createNewParameterInstance)
                .collect(Collectors.toList()));

        return newInstance(constructor, preparedParams);
    }

    private <V extends AbstractService> V newInstance(Constructor<V> constructor, List<Object> paramInstance) {
        try {
            return constructor.newInstance(paramInstance.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object createNewParameterInstance(Class clazz) {
        if (GenericDao.class.isAssignableFrom(clazz)) {
            return getDaoInstance(clazz);
        } else if (AbstractService.class.isAssignableFrom(clazz)) {
            return this.getObject(clazz);
        } else {
            throw new RuntimeException(); //TODO: продумать exception
        }
    }

    private <V extends GenericDao> V getDaoInstance(Class<V> daoType) {
        return daoManager.getObject(daoType);
    }

}
