package main.java.com.hotelSystem.exception;

import main.java.com.hotelSystem.dao.GenericDao;

/**
 * Exception indicates that some exception was caused during processing
 * {@link GenericDao} and it's subclasses
 * operations
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 * @see GenericDao
 */
public class DaoException extends Exception {

    /**
     * {@inheritDoc}
     */
    public DaoException() {
    }

    /**
     * {@inheritDoc}
     *
     * @param message {@inheritDoc}
     */
    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
