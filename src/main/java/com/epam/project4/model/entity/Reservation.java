package main.java.com.epam.project4.model.entity;

import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.roomParameter.ParameterValue;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Class represents reservation entity in Hotel System.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class Reservation implements Serializable {

    /**
     * id
     */
    private int id;

    /**
     * date, from which hotel room will be booked
     */
    private LocalDate dateFrom;

    /**
     * date, to which hotel room will be booked
     */
    private LocalDate dateTo;

    /**
     * request date
     */
    private LocalDate requestDate;

    /**
     * current reservation status (can't be {@link ReservationStatus#ALL})
     */
    private ReservationStatus status;

    /**
     * hotel room, that is booked by current reservation
     */
    private HotelRoom hotelRoom;

    /**
     * user-owner of current reservation
     */
    private User user;

    /**
     * reservation comment
     */
    private String comment;

    /**
     * hotel room's id
     */
    private transient int hotelRoomID;

    /**
     * user-owner's id
     */
    private transient int userID;

    /**
     * request parameters, assosiated with current reservation
     */
    private List<ParameterValue> requestParameters;

    /**
     * request parameters' ids, assosiated with current reservation
     */
    private List<Integer> requestParametersIds;

    /**
     * Constructor, that inits all object's parameters (except transient ones)
     *
     * @param id                id
     * @param dateFrom          date, from which hotel room will be booked
     * @param dateTo            date, to which hotel room will be booked
     * @param requestDate       request date
     * @param status            current reservation status (can't be {@link ReservationStatus#ALL})
     * @param hotelRoom         hotel room's id
     * @param user              user-owner of current reservation
     * @param comment           reservation comment
     * @param requestParameters request parameters, assosiated with current reservation
     */
    public Reservation(int id, LocalDate dateFrom, LocalDate dateTo, LocalDate requestDate, ReservationStatus status, HotelRoom hotelRoom,
                       User user, String comment, List<ParameterValue> requestParameters) {
        this.id = id;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.requestDate = requestDate;
        this.status = status;
        this.hotelRoom = hotelRoom;
        this.user = user;
        this.comment = comment;
        this.requestParameters = requestParameters;
    }

    /**
     * constructor without parameters
     */
    public Reservation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public HotelRoom getHotelRoom() {
        return hotelRoom;
    }

    public void setHotelRoom(HotelRoom hotelRoom) {
        this.hotelRoom = hotelRoom;
    }

    public int getHotelRoomID() {
        return hotelRoomID;
    }

    public void setHotelRoomID(int hotelRoomID) {
        this.hotelRoomID = hotelRoomID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = (comment == null) ? "" : comment;
    }

    public List<ParameterValue> getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(List<ParameterValue> requestParameters) {
        this.requestParameters = requestParameters;
    }

    public List<Integer> getRequestParametersIds() {
        return requestParametersIds;
    }

    public void setRequestParametersIds(List<Integer> requestParametersIds) {
        this.requestParametersIds = requestParametersIds;
    }
}
