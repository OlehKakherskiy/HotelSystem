package main.java.com.hotelSystem.exception;

import main.java.com.hotelSystem.app.constants.MessageCode;

/**
 * Exception indicates that there was an exception during reformatting
 * user request to service request (e.g. invalid params were recieved,
 * there wasn't some parameter or they are not valid according to business process)
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class RequestException extends LocalizedRuntimeException {

    /**
     * Contstructor, that sets code of the target message and adds message params.
     * Message params can be empty, but message code can't be null, otherwise
     * {@link NullPointerException} will be thrown.
     *
     * @param messageCode   code, that indicates which message will be returned
     * @param messageParams message params that will be insert to target message
     */
    public RequestException(MessageCode messageCode, Object... messageParams) {
        super(messageCode, messageParams);
    }

    /**
     * Constructor, that among parameters of {@link #RequestException(MessageCode, Object...)} sets
     * also exception cause.
     *
     * @param messageCode   code, that indicates which message will be returned
     * @param cause         exception cause
     * @param messageParams message params that will be insert to target message
     */
    public RequestException(MessageCode messageCode, Throwable cause, Object... messageParams) {
        super(messageCode, cause, messageParams);
    }

}
