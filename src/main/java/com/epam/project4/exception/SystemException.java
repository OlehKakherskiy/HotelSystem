package main.java.com.epam.project4.exception;

import main.java.com.epam.project4.app.constants.MessageCode;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class SystemException extends LocalizedRuntimeException {

    public SystemException(MessageCode messageCode, Object... messageParams) {
        super(messageCode, messageParams);
    }

    public SystemException(MessageCode messageCode, Throwable cause, Object... messageParams) {
        super(messageCode, cause, messageParams);
    }

}
