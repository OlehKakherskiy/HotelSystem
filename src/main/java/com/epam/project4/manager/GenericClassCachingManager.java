package main.java.com.epam.project4.manager;

import main.java.com.epam.project4.exception.ManagerConfigException;

import java.util.Map;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public abstract class GenericClassCachingManager<K, E> extends GenericCachingManager<K, E, Class<? extends E>> {

    public GenericClassCachingManager(Map<K, Class<? extends E>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }

    @Override
    protected <V extends E> V getObjectHook(Class<? extends E> intermediateTemplateElement) throws ManagerConfigException {
        return instantiate((Class<V>) intermediateTemplateElement);
    }

    protected abstract <V extends E> V instantiate(Class<V> fromClass) throws ManagerConfigException;
}