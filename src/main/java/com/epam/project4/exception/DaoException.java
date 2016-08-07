package main.java.com.epam.project4.exception;

/**
 * Exception indicates that some exception was caused during processing
 * {@link main.java.com.epam.project4.model.dao.GenericDao} and it's subclasses
 * operations
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 * @see main.java.com.epam.project4.model.dao.GenericDao
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
