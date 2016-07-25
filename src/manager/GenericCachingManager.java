package manager;

import model.exceptions.ManagerConfigException;
import model.exceptions.SystemException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericCachingManager<K, E, T> extends GenericManager<K, E, T> {

    protected Map<K, E> cache;

    public GenericCachingManager(Map<K, T> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
        cache = new HashMap<K, E>();
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public <V extends E> V getInstance(K key) {
        try {
            return (cache.containsKey(key)) ? (V) cache.get(key) : createAndCache(key);
        } catch (RuntimeException | ManagerConfigException e) {
            throw new SystemException(e.getMessage(), e); //TODO: системная ошибка просто описание
        }
    }

    @SuppressWarnings("unchecked")
    private <V extends E> V createAndCache(K key) throws ManagerConfigException {
        V result = null;
        T intermediateTemplateElement = keyObjectTemplateMap.get(key);
        if (intermediateTemplateElement == null) {
            throw new ManagerConfigException("There is no template element in template map with the key: " + key);
        }
        result = getObjectHook(intermediateTemplateElement);
        cache.put(key, result);

        return result;
    }

    protected abstract <V extends E> V getObjectHook(T intermediateTemplateElement) throws ManagerConfigException;
}