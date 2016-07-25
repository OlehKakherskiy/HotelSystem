package model.manager;

import model.exceptions.ManagerConfigException;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericCachingManager<K, E> extends GenericManager<K, E> {

    protected Map<K, E> cache;

    public GenericCachingManager(Map<K, Class<? extends E>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public <V extends E> V getInstance(K key) {
        return (cache.containsKey(key)) ? (V) cache.get(key) : createAndCache(key);
    }

    @SuppressWarnings("unchecked")
    private <V extends E> V createAndCache(K key) {
        V result = null;
        try {
            Class<? extends E> resultClass = keyObjectTemplateMap.get(key);
            result = getObjectHook((Class<V>) resultClass);
            cache.put(key, result);

        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ManagerConfigException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    protected abstract <V extends E> V getObjectHook(Class<V> objectClass) throws IllegalAccessException, InstantiationException,
            InvocationTargetException, ManagerConfigException;
}