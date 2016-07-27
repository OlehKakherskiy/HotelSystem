package main.java.com.epam.project4.app;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GlobalContext {

    private Map<GlobalContextConstant, Object> globalContextMap;

    private static final GlobalContext globalContext = new GlobalContext();

    private GlobalContext() {
        globalContextMap = new HashMap<>();
    }

    public static synchronized void addToGlobalContext(GlobalContextConstant key, Object value) { //TODO: нужно ли
        //todo: null check
        globalContext.globalContextMap.put(key, value);
    }

    public static Object getValue(GlobalContextConstant key) {
        return globalContext.globalContextMap.get(key);
    }

    public static void removeValue(GlobalContextConstant key) {
        globalContext.globalContextMap.remove(key);
    }

}
