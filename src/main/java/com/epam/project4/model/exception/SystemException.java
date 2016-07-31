package main.java.com.epam.project4.model.exception;

import java.util.Locale;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class SystemException extends RuntimeException {

    private Locale locale;

    public SystemException() {
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
