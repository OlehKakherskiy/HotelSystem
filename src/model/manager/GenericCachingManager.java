package model.manager;

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
    public <V extends E> V getObject(K key) {
        return (cache.containsKey(key)) ? (V) cache.get(key) : createAndCache(key);
    }

    private <V extends E> V createAndCache(K key) {
        try {
            Class<? extends E> resultClass = keyObjectTemplateMap.get(key);
            V result = null;
            result = getObjectHook((Class<V>) resultClass);
            cache.put(key, result);
            return result;
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract <V extends E> V getObjectHook(Class<V> objectClass) throws IllegalAccessException, InstantiationException;
}