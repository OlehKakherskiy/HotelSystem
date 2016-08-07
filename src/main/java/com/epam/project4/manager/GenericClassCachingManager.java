package main.java.com.epam.project4.manager;

import main.java.com.epam.project4.exception.ManagerConfigException;

import java.util.Map;

/**
 * Class represents caching manager, which intermediate type is class of target object.
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
abstract class GenericClassCachingManager<K, E> extends GenericCachingManager<K, E, Class<? extends E>> {

    GenericClassCachingManager(Map<K, Class<? extends E>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <p>
     * Casts intermediate template element to target generic type of class object.
     * </p>
     *
     * @param intermediateTemplateElement element that has extra information
     *                                    about instantiation process of target type element
     * @param <V>                         {@inheritDoc}
     * @return {@inheritDoc}
     * @throws ManagerConfigException {@inheritDoc}
     */
    @Override
    protected <V extends E> V getObjectHook(Class<? extends E> intermediateTemplateElement) throws ManagerConfigException {
        return instantiate((Class<V>) intermediateTemplateElement);
    }

    /**
     * Create instance from input parameter.
     *
     * @param fromClass class, from which instance will be created.
     * @param <V>       target instance type (can be equal as <E> or a subclass of it)
     * @return instance of type <V>, which type is mapped to key parameter
     * @throws ManagerConfigException if exception caused during the object instantiation process.
     */
    protected abstract <V extends E> V instantiate(Class<V> fromClass) throws ManagerConfigException;
}