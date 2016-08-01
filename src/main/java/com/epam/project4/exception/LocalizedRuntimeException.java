package main.java.com.epam.project4.exception;

import main.java.com.epam.project4.app.LocalizedMessageFormatter;
import main.java.com.epam.project4.app.constants.MessageCode;

import java.util.Locale;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class LocalizedRuntimeException extends RuntimeException {

    private MessageCode messageCode;

    private Object[] messageParams;

    private Locale locale;

    public LocalizedRuntimeException(MessageCode messageCode, Object... messageParams) {
        this.messageCode = messageCode;
        this.messageParams = messageParams;
    }

    public LocalizedRuntimeException(MessageCode messageCode, Throwable cause, Object... messageParams) {
        super(cause);
        this.messageCode = messageCode;
        this.messageParams = messageParams;
    }

    @Override
    public String getMessage() {
        return LocalizedMessageFormatter.getLocalizedMessage(messageCode, messageParams);
    }

    @Override
    public String getLocalizedMessage() {
        return LocalizedMessageFormatter.getLocalizedMessage(locale, messageCode, messageParams);
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}