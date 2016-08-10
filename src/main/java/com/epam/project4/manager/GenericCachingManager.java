package main.java.com.epam.project4.manager;

import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.app.util.DebugLevelLoggerProxy;
import main.java.com.epam.project4.exception.ManagerConfigException;
import main.java.com.epam.project4.exception.SystemException;
import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * This type of manager caches objects that are instantiated by it and, firstly, check the cache
 * for object existence with target key. If object exists in cache, returns from it, otherwise create
 * new one and add it to cache.
 * <p>
 * <p>
 * Also wraps target object by proxy-logger, that logs all methods calls,
 * input params and return values. Do wrapping only if {@link Logger#isDebugEnabled()} returns true.
 * </p>
 *
 * @param <K> key for value instancing from manager
 * @param <E> target type, which subtypes will be returned by {@link #getInstance(Object)}
 * @param <T> special intermediate type, from which element of target type can
 *            be get (can be the same as target type)
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see DebugLevelLoggerProxy
 * @see Logger#isDebugEnabled()
 */
abstract class GenericCachingManager<K, E, T> extends GenericManager<K, E, T> {

    private static final String NULL_KEY_EXCEPTION = "The key param in method createAndCache of manager {0} is null";

    private static final String NO_SUCH_KEY_EXCEPTION = "There is no key element in template map with the key: {0} in manager {1}";

    private static final Logger logger = Logger.getLogger(GenericCachingManager.class);

    /**
     * target object cache. Maps key object to target value object.
     */
    protected Map<K, E> cache;

    /**
     * Inits {@link #cache} field.
     *
     * @param keyObjectTemplateMap key/extra_info for instantiation target object map
     */
    GenericCachingManager(Map<K, T> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
        cache = new HashMap<K, E>();
    }

    /**
     * {@inheritDoc}.
     * <p>
     * <p>
     * If target type instance is already in the cache, returns gets result object from
     * it. Otherwise see {@link #createAndCache(Object)}
     * </p>
     *
     * @param key key object, whose mapped type instance should be instantiate.
     * @param <V> {@inheritDoc}
     * @return {@inheritDoc}
     * @throws SystemException if key = null or exception was caused in {@link #createAndCache(Object)}
     */
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

    /**
     * Creates object using {@link #getObjectHook(Object)}. After instantiating adds result
     * object to cache with key that is identical to input parameter. Also wraps target object
     * by {@link DebugLevelLoggerProxy} if there are appropriate conditions.
     *
     * @param key key object, whose mapped type instance should be instantiate.
     * @param <V> target instance type (can be equal as <E> or a subclass of it)
     * @return instance of type <V>, which type is mapped to key parameter
     * @throws ManagerConfigException if intermediate type, associated with key, is null, or exception caused during
     *                                the object instantiation process.
     */
    @SuppressWarnings("unchecked")
    private <V extends E> V createAndCache(K key) throws ManagerConfigException {
        V result = null;
        T intermediateTemplateElement = keyObjectTemplateMap.get(key);
        if (intermediateTemplateElement == null) {
            throw new ManagerConfigException(MessageFormat.format(NO_SUCH_KEY_EXCEPTION, key, this.getClass().getName()));
        }
        result = getObjectHook(intermediateTemplateElement);
        if (logger.isDebugEnabled()) {
            result = (V) DebugLevelLoggerProxy.newInstance(result);
        }
        cache.put(key, result);

        return result;
    }

    /**
     * Creates target type instance using intermediate type element.
     *
     * @param intermediateTemplateElement element that has extra information
     *                                    about instantiation process of target type element
     * @param <V>                         target instance type (can be equal as <E> or a subclass of it)
     * @return instance of type <V>, which type is mapped to key parameter
     * @throws ManagerConfigException if exception caused during the object instantiation process.
     */
    protected abstract <V extends E> V getObjectHook(T intermediateTemplateElement) throws ManagerConfigException;
}