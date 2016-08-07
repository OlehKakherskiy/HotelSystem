package main.java.com.epam.project4.manager.managerImpl;

import main.java.com.epam.project4.exception.ManagerConfigException;
import main.java.com.epam.project4.manager.AbstractDaoManager;
import main.java.com.epam.project4.manager.AbstractServiceManager;
import main.java.com.epam.project4.model.dao.GenericDao;
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
 * Manager implementation for {@link AbstractService} subtypes. All type's
 * dependencies are injected via constructor. Constructor should have
 * only {@link GenericDao} and {@link AbstractService} types for injecting.
 * If there are several public constructors, manager will get constructor
 * from 0 position of {@link Class#getConstructors()}.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see AbstractService
 * @see Class#getConstructors()
 * @see Constructor
 */
public class ServiceManagerImpl extends AbstractServiceManager {

    private static final String INSTANTIATION_EXCEPTION_MESSAGE = "Exception caused while instantiating service of type %s with params:%s";

    private static final String NO_INSTANTIATION_STRATEGY = "Exception caused because of %s service's constructor parameter of type %s. There is no" +
            "strategy how to instantiate this type.";


    /**
     * uses for creating {@link GenericDao} and inject them to target service object
     */
    private AbstractDaoManager daoManager;

    public ServiceManagerImpl(Map<Class<? extends AbstractService>, Class<? extends AbstractService>> keyObjectTemplateMap,
                              AbstractDaoManager daoManager) {
        super(keyObjectTemplateMap);
        this.daoManager = daoManager;
    }

    /**
     * {@inheritDoc}
     *
     * @param objectClass {@inheritDoc}
     * @param <V>         {@inheritDoc} This type is a subtype of {@link AbstractService}
     * @return {@inheritDoc} All object's dependencies are injected via constructor
     * @throws ManagerConfigException {@inheritDoc}.
     *                                Also see {@link #newInstance(Class, Constructor, List)} throws paragraph
     */
    @Override
    protected <V extends AbstractService> V instantiate(Class<V> objectClass) throws ManagerConfigException {
        Constructor<V> constructor = (Constructor<V>) objectClass.getConstructors()[0];
        List<Object> preparedParams = new ArrayList<>(constructor.getParameterCount());
        List<Class> types = Arrays.asList(constructor.getParameters()).stream().map(Parameter::getType).collect(Collectors.toList());
        for (Class type : types) {
            preparedParams.add(createNewParameterInstance(type));
        }
        return newInstance(objectClass, constructor, preparedParams);
    }

    /**
     * Creates new {@link AbstractService} instance of specific class, using
     * specific constructor, and injects parameters according to constructor's
     * params.
     *
     * @param clazz             result object's type
     * @param constructor       constructor object, that will be invoked for creating new target instance
     * @param paramInstanceList type's instances according to constructor's param types
     * @param <V>               target instance type (can be equal as <E> or a subclass of it)
     * @return {@link AbstractService} object with injected objects through constructor
     * @throws ManagerConfigException if exception caused during the object instantiation process.
     */
    private <V extends AbstractService> V newInstance(Class<V> clazz, Constructor<V> constructor, List<Object> paramInstanceList) throws ManagerConfigException {
        try {
            return (paramInstanceList.isEmpty()) ? clazz.newInstance() : constructor.newInstance(paramInstanceList.toArray());
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new ManagerConfigException(String.format(INSTANTIATION_EXCEPTION_MESSAGE,
                    clazz.getName(), Arrays.toString(paramInstanceList.toArray())), e);
        }
    }

    /**
     * Creates new instance of {@link GenericDao} or {@link AbstractService} with all injections.
     *
     * @param clazz class, which instance should be returned.
     * @return clazz prepared instance (with all injections)
     * @throws ManagerConfigException if constructor parameter (input parameter - clazz) is not a
     *                                subclass of {@link GenericDao} or {@link AbstractService}
     */
    private Object createNewParameterInstance(Class clazz) throws ManagerConfigException {
        System.out.println(clazz);
        if (GenericDao.class.isAssignableFrom(clazz)) {
            return getDaoInstance(clazz);
        } else if (AbstractService.class.isAssignableFrom(clazz)) {
            return this.getInstance(clazz);
        } else {
            throw new ManagerConfigException(String.format(NO_INSTANTIATION_STRATEGY,
                    this.getClass().getCanonicalName(), clazz.getCanonicalName()));
        }
    }

    /**
     * Returns instance of specific DAO type. Result object
     * is being instanced by {@link AbstractDaoManager} instance
     *
     * @param daoType Class object of target DAO type
     * @param <V>     target DAO type
     * @return instance of daoType parameter
     */
    private <V extends GenericDao> V getDaoInstance(Class<V> daoType) {
        return daoManager.getInstance(daoType);
    }
}