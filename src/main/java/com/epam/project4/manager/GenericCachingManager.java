package main.java.com.epam.project4.manager;

import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.exception.ManagerConfigException;
import main.java.com.epam.project4.exception.SystemException;
import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericCachingManager<K, E, T> extends GenericManager<K, E, T> {

    private static final String NULL_KEY_EXCEPTION = "The key param in method createAndCache of manager {0} is null";

    private static final String NO_SUCH_KEY_EXCEPTION = "There is no key element in template map with the key: {0} in manager {1}";

    private static final Logger logger = Logger.getLogger(GenericCachingManager.class);

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
                throw new ManagerConfigException(MessageFormat.format(NULL_KEY_EXCEPTION, this.getClass().getName()));
            }
            return (cache.containsKey(key)) ? (V) cache.get(key) : createAndCache(key);
        } catch (RuntimeException | ManagerConfigException e) {
            throw new SystemException(MessageCode.GENERAL_SYSTEM_EXCEPTION, e);
        }
    }

    @SuppressWarnings("unchecked")
    private <V extends E> V createAndCache(K key) throws ManagerConfigException {
        V result = null;
        T intermediateTemplateElement = keyObjectTemplateMap.get(key);
        if (intermediateTemplateElement == null) {
            throw new ManagerConfigException(MessageFormat.format(NO_SUCH_KEY_EXCEPTION, key, this.getClass().getName()));
        }
        if (logger.isDebugEnabled()) {
            Object proxied = DebugLoggingProxy.newInstance(getObjectHook(intermediateTemplateElement));
            System.out.println("proxied = " + proxied);
            result = (V) proxied;
        }
        cache.put(key, result);

        return result;
    }

    protected abstract <V extends E> V getObjectHook(T intermediateTemplateElement) throws ManagerConfigException;
}