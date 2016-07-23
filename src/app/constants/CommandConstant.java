package app.constants;

import java.util.Arrays;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public enum CommandConstant {

    LOGIN_COMMAND("login"),

    GET_RESERVATION_LIST_COMMAND("getReservationList"),

    LOGOUT_COMMAND("logout"),

    GET_RESERVATION_FULL_INFO_COMMAND("getReservationFullInfo"),

    FILL_NEW_RESERVATION_COMMAND("fillNewReservation"),

    REFUSE_HOTEL_ROOM_OFFER_COMMAND("refuseHotelRoomOffer"),

    SUBMIT_HOTEL_ROOM_OFFER_COMMAND("submitHotelRoomOffer");

    private String commandName;

    CommandConstant(String commandName) {
        this.commandName = commandName;
    }

    public static CommandConstant fromValue(String commandName) {
        if (commandName == null || commandName.isEmpty())
            return null; //TODO: exception
        return Arrays.asList(CommandConstant.values()).
                stream().filter(name -> name.commandName.equals(commandName)).findFirst().get();
    }
}
