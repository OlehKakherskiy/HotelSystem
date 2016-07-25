package app.constants;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public enum ServiceConstant {

    USER_SERVICE("userService"),

    RESERVATION_SERVICE("reservationService"),

    HOTEL_ROOM_SERVICE("hotelRoomService"),

    PARAMETER_VALUE_SERVICE("parameterValueService");

    private String serviceName;

    ServiceConstant(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }


}
