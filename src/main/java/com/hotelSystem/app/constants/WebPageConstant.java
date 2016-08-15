package main.java.com.hotelSystem.app.constants;

/**
 * Constants, that are mapped to target jsps. To get JSPs path
 * should be called {@link #getPath()} of target enum.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public enum WebPageConstant {

    LOGIN("login"),

    INDEX("index"),

    RESERVATION_PROFILE("reservationProfile"),

    USER_PROFILE("userProfile"),

    HOTEL_ROOM_LIST("hotelRoomList"),

    HOTEL_ROOM_PROFILE("hotelRoomProfile"),

    UPDATE_USER_PAGE("updateUserPage");

    private String path;


    WebPageConstant(String path) {
        this.path = path;
    }

    /**
     * formats jsp's path from deployment root.
     *
     * @return jsp's path from deployment root.
     */
    public String getPath() {
        return (this == LOGIN) ? String.format("/%s.jsp", path) : String.format("/WEB-INF/jspf/page/%s.jsp", path);
    }
}
