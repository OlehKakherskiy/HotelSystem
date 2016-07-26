package manager;

import model.exception.ManagerConfigException;
import model.exception.SystemException;

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
            if (key == null) {
                throw new ManagerConfigException(String.format("The key param in method createAndCache of manager %s is null", this.getClass()));
            }
            return (cache.containsKey(key)) ? (V) cache.get(key) : createAndCache(key);
        } catch (RuntimeException | ManagerConfigException e) {
            throw new SystemException(e.getMessage(), e); //TODO: системная ошибка просто описание
        }
    }

    @SuppressWarnings("unchecked")
    private <V extends E> V createAndCache(K key) throws ManagerConfigException {
        V result;
        T intermediateTemplateElement = keyObjectTemplateMap.get(key);
        if (intermediateTemplateElement == null) {
            throw new ManagerConfigException(String.format("There is no template element in template map with the key: %s in manager %s", key, this.getClass().getName()));
        }
        result = getObjectHook(intermediateTemplateElement);
        cache.put(key, result);

        return result;
    }

    protected abstract <V extends E> V getObjectHook(T intermediateTemplateElement) throws ManagerConfigException;
}