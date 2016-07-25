package model.exceptions;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ManagerConfigException extends RuntimeException {

    public ManagerConfigException() {
    }

    public ManagerConfigException(String message) {
        super(message);
    }

    public ManagerConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
