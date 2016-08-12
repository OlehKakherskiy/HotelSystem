package main.java.com.hotelSystem.exception;

import main.java.com.hotelSystem.app.constants.MessageCode;
import main.java.com.hotelSystem.app.util.LocalizedMessageFormatter;

import java.util.Locale;

/**
 * Class represents runtime exception, that can return localized exception
 * message. For providing this it uses {@link LocalizedMessageFormatter},
 * before returning localized message target {@link Locale} should be
 * set via {@link #setLocale(Locale)} method. Also simple message string
 * can't be instantiated as exception message via constructors, because there's
 * no logic how to localize message at runtime. Therefore {@link MessageCode}
 * is using to indicate what message should be returned as exception message.
 * Also messageParams can be added to message via constructors. For more info,
 * see {@link LocalizedMessageFormatter#getLocalizedMessage(Locale, MessageCode, Object...)}
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 * @see LocalizedMessageFormatter
 * @see LocalizedMessageFormatter#getLocalizedMessage(Locale, MessageCode, Object...)
 * @see Locale
 * @see MessageCode
 */
public class LocalizedRuntimeException extends RuntimeException {

    /**
     * message code, assosiated with target message, that will be localized
     */
    private MessageCode messageCode;

    /**
     * message params, that will be set to target message according to their positions in message
     */
    private Object[] messageParams;

    /**
     * target locale of the message
     */
    private Locale locale;

    /**
     * Contstructor, that sets code of the target message and adds message params.
     * Message params can be empty, but message code can't be null, otherwise
     * {@link NullPointerException} will be thrown.
     *
     * @param messageCode   code, that indicates which message will be returned
     * @param messageParams message params that will be insert to target message
     */
    public LocalizedRuntimeException(MessageCode messageCode, Object... messageParams) {
        this.messageCode = messageCode;
        this.messageParams = messageParams;
    }

    /**
     * Constructor, that among parameters of {@link #LocalizedRuntimeException(MessageCode, Object...)} sets
     * also exception cause.
     *
     * @param messageCode   code, that indicates which message will be returned
     * @param cause         exception cause
     * @param messageParams message params that will be insert to target message
     */
    public LocalizedRuntimeException(MessageCode messageCode, Throwable cause, Object... messageParams) {
        super(cause);
        this.messageCode = messageCode;
        this.messageParams = messageParams;
    }

    /**
     * returns formatted message in default locale, using
     * {@link #messageCode} and {@link #messageParams}
     *
     * @return formatted message in default locale
     * @see Locale#getDefault()
     */
    @Override
    public String getMessage() {
        return LocalizedMessageFormatter.getLocalizedMessage(messageCode, messageParams);
    }

    /**
     * returns formatted message in locale, instantiated
     * via {@link #setLocale(Locale)} method. If {@link #locale} is null,
     * then {@link #getMessage()} will be returned.
     *
     * @return message in target locale.
     */
    @Override
    public String getLocalizedMessage() {
        return (locale == null)
                ? getMessage() : LocalizedMessageFormatter.getLocalizedMessage(locale, messageCode, messageParams);
    }

    /**
     * sets locale for message
     *
     * @param locale message locale
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}