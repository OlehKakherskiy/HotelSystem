package model.entity;

import model.entity.enums.ReservationStatus;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class Reservation implements Serializable {

    private int id;

    private LocalDate dateFrom;

    private LocalDate dateTo;

    private LocalDate requestDate;

    private ReservationStatus status;

    private HotelRoom hotelRoom;

    private User user;

    //TODO: промежуточное состояние
    private int hotelRoomID;

    //TODO: промежуточное состояние
    private int userID;

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
}
