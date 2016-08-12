package main.java.com.hotelSystem.exception;

/**
 * Exception indicates that some exception was caused during configuring
 * {@link main.java.com.epam.project4.manager.GenericManager} instance or target
 * object instantiation (for example, {@link IllegalAccessException},
 * {@link java.lang.reflect.InvocationTargetException}, etc.)
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see main.java.com.epam.project4.manager.GenericManager
 * @see main.java.com.epam.project4.manager.GenericCachingManager
 * @see main.java.com.epam.project4.manager.GenericClassCachingManager
 */
public class ManagerConfigException extends Exception {

    public ManagerConfigException(String message) {
        super(message);
    }

    public ManagerConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
