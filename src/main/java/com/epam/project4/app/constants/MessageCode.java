package main.java.com.epam.project4.app.constants;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public enum MessageCode {

    LOGIN_OPERATION_SYSTEM_EXCEPTION("loginOperationSystemException"),

    WRONG_LOGIN_OR_PASSWORD("wrongLoginOrPassword"),

    WRONG_USER_ID("wrongUserId"),

    READ_USER_OPERATION_SYSTEM_EXCEPTION("readUserSystemException"),

    GET_MOBILE_PHONE_LIST_SYSTEM_EXCEPTION("getMobilePhoneListSystemException"),

    GET_PARAMETER_VALUE_LIST_SYSTEM_EXCEPTION("getParameterValueListException"),

    GET_ALL_ROOMS_FULL_DETAILS_SYSTEM_EXCEPTION("getAllRoomFullDetailsSystemException"),

    WRONG_RESERVATION_ID_EXCEPTION("wrongReservationIdException"),

    READ_RESERVATION_SYSTEM_EXCEPTION("readReservationSystemException"),

    WRONG_RESERVATION_STATUS_FOR_UPDATING_EXCEPTION("wrongReservationStatusForUpdatingException"),

    DELETE_RESERVATION_SYSTEM_EXCEPTION("deleteReservationSystemException"),

    DELETE_RESERVATION_REQUEST_EXCEPTION("deleteReservationRequestException"),

    SAVE_RESERVATION_SYSTEM_EXCEPTION("saveReservationSystemException"),

    REFUSE_RESERVATION_PROCESSING_SYSTEM_EXCEPTION("refuseReservationProcessingSystemException"),

    SUBMIT_RESERVATION_OFFER_SYSTEM_EXCEPTION("submitReservationOfferSystemException"),

    REFUSE_RESERVATION_OFFER_SYSTEM_EXCEPTION("refuseReservationOfferSystemException"),

    OFFER_HOTEL_ROOM_SYSTEM_EXCEPTION("offerHotelRoomSystemException"),

    GET_RESERVATION_LIST_SYSTEM_EXCEPTION("getReservationListSystemException"),

    GET_USER_RESERVATION_LIST_SYSTEM_EXCEPTION("getUserReservationListSystemException"),

    GET_ROOM_MONTH_RESERVATION_LIST_SYSTEM_EXCEPTION("getRoomMonthReservationListSystemException"),

    WRONG_ROOM_ID_SYSTEM_EXCEPTION("wrongRoomIdSystemException"),

    READ_ROOM_SYSTEM_EXCEPTION("readRoomSystemException"),

    GENERAL_SYSTEM_EXCEPTION("generalSystemException");

    private String code;

    MessageCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
