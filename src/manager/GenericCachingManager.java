package manager;

import model.exceptions.ManagerConfigException;
import model.exceptions.SystemException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericCachingManager<K, E> extends GenericManager<K, E> {

    protected Map<K, E> cache;

    public GenericCachingManager(Map<K, Class<? extends E>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
        cache = new HashMap<K, E>();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public <V extends E> V getInstance(K key){
        try {
            return (cache.containsKey(key)) ? (V) cache.get(key) : createAndCache(key);
        } catch (RuntimeException | ManagerConfigException e) {
            throw new SystemException(e.getMessage(), e); //TODO: системная ошибка просто описание
        }
    }

    @SuppressWarnings("unchecked")
    private <V extends E> V createAndCache(K key) throws ManagerConfigException{
        V result = null;
        Class<? extends E> resultClass = keyObjectTemplateMap.get(key);
        if(resultClass == null){
            throw new ManagerConfigException("There is no class in template map with the key: "+key);
        }
        result = getObjectHook((Class<V>) resultClass);
        cache.put(key, result);

        return result;
    }

    protected abstract <V extends E> V getObjectHook(Class<V> objectClass) throws ManagerConfigException;
}