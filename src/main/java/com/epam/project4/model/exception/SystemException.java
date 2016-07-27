package main.java.com.epam.project4.model.exception;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class SystemException extends RuntimeException{

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
}
