package main.java.com.epam.project4.app;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GlobalContext {

    private static Map<GlobalContextConstant, Object> globalContextMap = new HashMap<>();

    public static synchronized void addToGlobalContext(GlobalContextConstant key, Object value) { //TODO: нужно ли
        //todo: null check
        System.out.println("key = " + key);
        System.out.println("value = " + value);
        globalContextMap.put(key, value);
    }

    public static Object getValue(GlobalContextConstant key) {
        return globalContextMap.get(key);
    }

    public static void removeValue(GlobalContextConstant key) {
        globalContextMap.remove(key);
    }
}