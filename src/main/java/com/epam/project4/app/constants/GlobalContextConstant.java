package main.java.com.epam.project4.app.constants;

import java.util.Arrays;

/**
 * Global constants.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public enum GlobalContextConstant {

    DAO_MANAGER("daoManager"),

    SERVICE_MANAGER("serviceManager"),

    COMMAND_FACTORY("commandFactory"),

    DATA_SOURCE("dataSource"),

    SECURITY_CONFIGURATION("secureConfiguration"),

    COMMAND_NAME("commandName"),

    USER("user"),

    LOGIN("login"),

    PASSWORD("password"),

    RESERVATION_ID("reservationID"),

    CURRENT_RESERVATION("currentReservation"),

    PARAMETER_VALUE_LIST("parameterValueList"),

    PARAMETER_VALUE_MAP("parameterValueMap"),

    CURRENT_HOTEL_ROOM("currentHotelRoom"),

    HOTEL_ROOM_LIST("hotelRoomList"),

    RESERVATION_MONTH("reservationMonth"),

    RESERVATION_YEAR("reservationYear"),

    RESERVATION_STATUS("reservationStatus"),

    CURRENT_LOCALE("lang");


    private String name;

    GlobalContextConstant(String name) {
        this.name = name;
    }

    public GlobalContextConstant fromValue(String name) {
        return Arrays.asList(GlobalContextConstant.values())
                .stream()
                .filter(constant -> constant.name.equals(name))
                .findFirst()
                .orElse(null);
    }

    public String getName() {
        return name;
    }
}
