package main.java.com.epam.project4.app.util;

import main.java.com.epam.project4.app.constants.MessageCode;
import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.*;

/**
 * Utility class, that formats string in target locale and inserts specific parameters, that were inputted
 * to it.
 * <p>
 * Should be configured by {@link #init(String, Locale...)},
 * and then can be used as localized string formatter
 * </p>
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 * @see ResourceBundle
 */
public class LocalizedMessageFormatter {

    private static final Logger logger = Logger.getLogger(LocalizedMessageFormatter.class);
    /**
     * locale to bundle map, it is used for returning message in target locale
     */
    private static Map<Locale, ResourceBundle> resourceBundleMap;

    /**
     * default locale's bundle. If there's no bundle of target locale,
     * returns string from this bundle
     */
    private static ResourceBundle defaultBundle;

    /**
     * inits message formatter with bundles. Loads bundle
     * for each locale to {@link #resourceBundleMap}
     *
     * @param base    the base name of the resource bundle
     * @param locales locales, for which bundle will be created
     */
    public static void init(String base, Locale... locales) {
        resourceBundleMap = new HashMap<>();
        defaultBundle = ResourceBundle.getBundle(base);
        Arrays.stream(locales).forEach(locale -> resourceBundleMap.put(locale, ResourceBundle.getBundle(base, locale)));
    }

    /**
     * Returns localized message, that is mapped to specific code parameter, in specific locale
     * and inserts Object params to message according to their positions
     *
     * @param locale target locale, if null - retruns message in default locale (provided by {@link #defaultBundle})
     * @param code   key that is associated with target message
     * @param params params that will be inserted to the message according to their position
     *               (if params more than target places in message - will be inserted just as declared in message)
     * @return localized message with inserted params in target or default locale
     */
    public static String getLocalizedMessage(Locale locale, MessageCode code, Object... params) {
        if (locale == null) {
            return getLocalizedMessage(code, params);
        }
        ResourceBundle currentBundle = resourceBundleMap.get(locale);
        String message = null;
        if (currentBundle != null && (message = currentBundle.getString(code.getCode())) != null) {
            return MessageFormat.format(message, params);
        } else {
            logger.warn(MessageFormat.format("No message for this type of operation's result: {0}", code));
            return "No message for this type of operation's result";
        }
    }

    /**
     * Returns localized message, that is mapped to specific code parameter, in default locale
     * and inserts Object params to message according to their positions
     *
     * @param code   key that is associated with target message
     * @param params params that will be inserted to the message according to their position
     *               (if params more than target places in message - will be inserted just as declared in message)
     * @return localized message with inserted params in default locale
     */
    public static String getLocalizedMessage(MessageCode code, Object... params) {
        String message = (defaultBundle.getString(code.getCode()));
        System.out.println(MessageFormat.format("Localized message with default locale:" + message, params));
        if (message != null) {
            return MessageFormat.format(message, params);
        } else {
            logger.warn(MessageFormat.format("No message for this type of message: {0}", code));
            return "No message for this type of operation's result";
        }
    }
}