package main.java.com.epam.project4.app;

import main.java.com.epam.project4.app.constants.MessageCode;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class LocalizedMessageFormatter {

    private static Map<Locale, ResourceBundle> resourceBundleMap;

    private static ResourceBundle defaultBundle;

    public static void init(String base, Locale... locales) {
        defaultBundle = ResourceBundle.getBundle(base);
        Arrays.stream(locales).forEach(locale -> resourceBundleMap.put(locale, ResourceBundle.getBundle(base, locale)));
    }

    public static String getLocalizedMessage(Locale locale, MessageCode code, Object... params) {
        if (locale == null) {
            return getLocalizedMessage(code, params);
        }
        ResourceBundle currentBundle = resourceBundleMap.get(locale);
        String message = null;
        if (currentBundle != null && (message = currentBundle.getString(code.getCode())) != null) {
            return MessageFormat.format(message, params);
        } else {
            return null; //TODO: занести в лог, что нет такого через варнинг
        }
    }

    public static String getLocalizedMessage(MessageCode code, Object... params) {
        String message = (defaultBundle.getString(code.getCode()));
        if (message != null) {
            return MessageFormat.format(message, params);
        } else {
            return null; //TODO: в лог
        }
    }
}