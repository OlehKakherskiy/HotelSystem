package model.service;

import model.entity.HotelRoom;
import model.entity.Reservation;
import model.entity.User;
import model.entity.enums.ReservationStatus;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface AbstractReservationService extends AbstractService {

    List<Reservation> getShortInfoAboutAllReservations(User user, ReservationStatus reservationStatus);

    Reservation getReservationDetailInfo(int reservationID, User user);

    void offerHotelRoom(Reservation reservation, HotelRoom hotelRoom);

    void setStatusToRefused(Reservation reservation);

    void setStatusToSubmitted(Reservation reservation);

    void addReservation(Reservation reservation, User user);

}
