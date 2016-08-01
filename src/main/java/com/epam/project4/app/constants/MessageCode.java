package main.java.com.epam.project4.app.constants;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public enum MessageCode {

    LOGIN_OPERATION_SYSTEM_EXCEPTION("loginOperationSystemException"),

    WRONG_LOGIN_OR_PASSWORD("wrongLoginOrPassword"),

    WRONG_USER_ID("wrongUserId"),

    READ_USER_OPERATION_SYSTEM_EXCEPTION("readUserSystemException");

    private String code;

    MessageCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
