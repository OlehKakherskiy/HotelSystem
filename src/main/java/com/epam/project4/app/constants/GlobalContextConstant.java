package main.java.com.epam.project4.app.constants;

import java.util.Arrays;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public enum GlobalContextConstant {

    DAO_MANAGER("daoManager"),

    SERVICE_MANAGER("serviceManager"),

    COMMAND_FACTORY("commandFactory"),

    DATA_SOURCE("dataSource"),

    COMMAND_NAME("command"),

    USER("user"),

    LOGIN("login"),

    PASSWORD("password"),

    RESERVATION_ID("reservationID"),

    CURRENT_RESERVATION("currentReservation"),

    PARAMETER_VALUE_LIST("parameterValueList"),

    PARAMETER_VALUE_MAP("parameterValueMap"),

    DEFAULT_ROOM_PARAMS_TO_SHOW("defaultRoomParamsToShow");

    private String name;

    GlobalContextConstant(String name) {
        this.name = name;
    }

    public GlobalContextConstant fromValue(String name) {
        if (name == null || name.isEmpty()) {
            return null; // TODO: throw exception
        }
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