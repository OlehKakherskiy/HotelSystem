package main.java.com.hotelSystem.app;

import main.java.com.hotelSystem.app.constants.GlobalContextConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * Global application context. Values from it can be accessed by all objects.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GlobalContext {

    private static Map<GlobalContextConstant, Object> globalContextMap = new HashMap<>();

    /**
     * adds key/value pair to context
     *
     * @param key   key
     * @param value value
     */
    public static synchronized void addToGlobalContext(GlobalContextConstant key, Object value) {
        globalContextMap.put(key, value);
    }

    /**
     * gets value from context or null
     *
     * @param key key, which value will be returned
     * @return key's value or null
     */
    public static Object getValue(GlobalContextConstant key) {
        return globalContextMap.get(key);
    }

    /**
     * removes value with target key. If there's no value with such key - do nothing
     *
     * @param key key, which value will be returned
     */
    public static void removeValue(GlobalContextConstant key) {
        globalContextMap.remove(key);
    }
}