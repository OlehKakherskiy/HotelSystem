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

    SAVE_RESERVATION_SYSTEM_EXCEPTION("saveReservationSystemException"),

    REFUSE_RESERVATION_PROCESSING_SYSTEM_EXCEPTION("refuseReservationProcessingSystemException"),

    SUBMIT_RESERVATION_OFFER_SYSTEM_EXCEPTION("submitReservationOfferSystemException"),

    REFUSE_RESERVATION_OFFER_SYSTEM_EXCEPTION("refuseReservationOfferSystemException"),

    OFFER_HOTEL_ROOM_SYSTEM_EXCEPTION("offerHotelRoomSystemException"),

    GET_RESERVATION_LIST_SYSTEM_EXCEPTION("getReservationListSystemException"),

    GET_USER_RESERVATION_LIST_SYSTEM_EXCEPTION("getUserReservationListSystemException"),

    GET_ROOM_MONTH_RESERVATION_LIST_SYSTEM_EXCEPTION("getRoomMonthReservationListSystemException"),

    WRONG_ROOM_ID("wrongRoomId"),

    READ_ROOM_SYSTEM_EXCEPTION("readRoomSystemException"),

    GENERAL_SYSTEM_EXCEPTION("generalSystemException"),

    NO_RESERVATION_ID_FOR_DELETE("noReservationIdForDelete"),

    NO_DATE_FROM_PARAMETER("noDateFromParameter"),

    NO_DATE_TO_PARAMETER("noDateToParameter"),

    DATE_FROM_IS_BEFORE_REQ_DATE_EXCEPTION("dateFromIsBeforeReqDateException"),

    DATE_TO_IS_BEFORE_REQ_DATE_EXCEPTION("dateToIsBeforeReqDateException"),

    DATE_TO_IS_BEFORE_DATE_FROM_EXCEPTION("dateToIsBeforeDateFromException");


    private String code;

    MessageCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
