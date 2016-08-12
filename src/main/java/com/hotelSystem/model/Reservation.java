package main.java.com.hotelSystem.model;

import main.java.com.hotelSystem.model.enums.ReservationStatus;
import main.java.com.hotelSystem.model.roomParameter.ParameterValueTuple;

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
    private transient int hotelRoomId = -1;

    /**
     * user-owner's id
     */
    private transient int userId = -1;

    /**
     * request parameters, assosiated with current reservation
     */
    private List<ParameterValueTuple> requestParameters;

    /**
     * request parameters' ids, assosiated with current reservation
     */
    private List<Integer> requestParametersIds;

    /**
     * Constructor, that inits all object's parameters (except transient ones)
     *
     * @param id                   id
     * @param dateFrom             date, from which hotel room will be booked
     * @param dateTo               date, to which hotel room will be booked
     * @param requestDate          request date
     * @param status               current reservation status (can't be {@link ReservationStatus#ALL})
     * @param hotelRoomId          hotel room's id
     * @param userId               user-owner's id of current reservation
     * @param comment              reservation comment
     * @param requestParametersIds request parameters, assosiated with current reservation
     */
    public Reservation(int id, LocalDate dateFrom, LocalDate dateTo, LocalDate requestDate, ReservationStatus status, int hotelRoomId,
                       int userId, String comment, List<Integer> requestParametersIds) {
        this.id = id;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.requestDate = requestDate;
        this.status = status;
        this.userId = userId;
        this.hotelRoomId = hotelRoomId;
        this.comment = comment;
        this.requestParametersIds = requestParametersIds;
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

    public int getHotelRoomId() {
        return hotelRoomId;
    }

    public void setHotelRoomId(int hotelRoomId) {
        this.hotelRoomId = hotelRoomId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = (comment == null) ? "" : comment;
    }

    public List<ParameterValueTuple> getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(List<ParameterValueTuple> requestParameters) {
        this.requestParameters = requestParameters;
    }

    public List<Integer> getRequestParametersIds() {
        return requestParametersIds;
    }

    public void setRequestParametersIds(List<Integer> requestParametersIds) {
        this.requestParametersIds = requestParametersIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;

        Reservation that = (Reservation) o;

        if (hotelRoomId != that.hotelRoomId) return false;
        if (userId != that.userId) return false;
        if (!dateFrom.equals(that.dateFrom)) return false;
        if (!dateTo.equals(that.dateTo)) return false;
        if (!requestDate.equals(that.requestDate)) return false;
        if (status != that.status) return false;
        return comment != null ? comment.equals(that.comment) : that.comment == null;

    }

    @Override
    public int hashCode() {
        int result = dateFrom.hashCode();
        result = 31 * result + dateTo.hashCode();
        result = 31 * result + requestDate.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + hotelRoomId;
        result = 31 * result + userId;
        return result;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "requestParametersIds=" + requestParametersIds +
                ", userId=" + userId +
                ", hotelRoomId=" + hotelRoomId +
                ", comment='" + comment + '\'' +
                ", user=" + user +
                ", hotelRoom=" + hotelRoom +
                ", status=" + status +
                ", requestDate=" + requestDate +
                ", dateTo=" + dateTo +
                ", dateFrom=" + dateFrom +
                ", id=" + id +
                '}';
    }
}
