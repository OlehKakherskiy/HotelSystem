package main.java.com.epam.project4.exception;

import main.java.com.epam.project4.app.constants.MessageCode;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class RequestException extends LocalizedRuntimeException {

    public RequestException(MessageCode messageCode, Object... messageParams) {
        super(messageCode, messageParams);
    }

    public RequestException(MessageCode messageCode, Throwable cause, Object... messageParams) {
        super(messageCode, cause, messageParams);
    }

}
