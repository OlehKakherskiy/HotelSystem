package main.java.com.hotelSystem.exception;

import main.java.com.hotelSystem.app.constants.MessageCode;

/**
 * Exception indicates that there were some exceptions during
 * request servicing (internal exceptions, not because of user's request).
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class SystemException extends LocalizedRuntimeException {

    /**
     * Contstructor, that sets code of the target message and adds message params.
     * Message params can be empty, but message code can't be null, otherwise
     * {@link NullPointerException} will be thrown.
     *
     * @param messageCode   code, that indicates which message will be returned
     * @param messageParams message params that will be insert to target message
     */
    public SystemException(MessageCode messageCode, Object... messageParams) {
        super(messageCode, messageParams);
    }

    /**
     * Constructor, that among parameters of {@link #SystemException(MessageCode, Object...)} sets
     * also exception cause.
     *
     * @param messageCode   code, that indicates which message will be returned
     * @param cause         exception cause
     * @param messageParams message params that will be insert to target message
     */
    public SystemException(MessageCode messageCode, Throwable cause, Object... messageParams) {
        super(messageCode, cause, messageParams);
    }

}
