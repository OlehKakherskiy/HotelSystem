package main.java.com.epam.project4.app.constants;

import java.util.Arrays;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public enum CommandConstant {

    LOGIN_COMMAND("loginCommand"),

    GET_RESERVATION_LIST_COMMAND("getReservationListCommand"),

    LOGOUT_COMMAND("logoutCommand"),

    GET_RESERVATION_PROFILE_COMMAND("getReservationProfileCommand"),

    FILL_NEW_RESERVATION_COMMAND("fillNewReservationCommand"),

    REFUSE_HOTEL_ROOM_OFFER_COMMAND("refuseHotelRoomOfferCommand"),

    SUBMIT_HOTEL_ROOM_OFFER_COMMAND("submitHotelRoomOfferCommand"),

    OFFER_HOTEL_ROOM_COMMAND("offerHotelRoomCommand"),

    GET_HOTEL_ROOM_LIST_COMMAND("getHotelRoomListCommand"),

    PREPARE_RESERVATION_PAGE_COMMAND("prepareReservationPageCommand"),

    GET_HOTEL_ROOM_PROFILE_COMMAND("getHotelRoomProfileCommand"),

    DELETE_RESERVATION_COMMAND("deleteReservationCommand"),

    REFUSE_RESERVATION_PROCESSING_COMMAND("refuseReservationProcessingCommand");

    private String commandName;

    CommandConstant(String commandName) {
        this.commandName = commandName;
    }

    public static CommandConstant fromValue(String commandName) {
        if (commandName == null)
            return null; //TODO: exception
        return Arrays.asList(CommandConstant.values()).
                stream().filter(name -> name.commandName.equals(commandName)).findFirst().orElse(null);
    }

    public String getName() {
        return commandName;
    }
}
