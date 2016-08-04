package main.java.com.epam.project4.app.constants;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public enum WebPageConstant {

    LOGIN("login"),

    INDEX("index"),

    RESERVATION_PROFILE("reservationProfile"),

    USER_PROFILE("userProfile"),

    HOTEL_ROOM_LIST("hotelRoomList"),

    HOTEL_ROOM_PROFILE("hotelRoomProfile");

    private String path;


    WebPageConstant(String path) {
        this.path = path;
    }

    public String getPath() {
        return (this == LOGIN) ? String.format("/%s.jsp", path) : String.format("/WEB-INF/jspf/%s.jsp", path);
    }
}
