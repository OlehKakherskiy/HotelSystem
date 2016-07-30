package main.java.com.epam.project4.model.service;

import main.java.com.epam.project4.model.entity.HotelRoom;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface AbstractReservationService extends AbstractService {

    List<Reservation> getShortInfoAboutAllReservations(User user, ReservationStatus reservationStatus);

    List<Reservation> getShortInfoAboutAllReservations(ReservationStatus reservationStatus);

    Reservation getReservationDetailInfo(int reservationID);

    void offerHotelRoom(Reservation reservation, HotelRoom hotelRoom);

    void refuseReservationOffer(Reservation reservation);

    void submitReservationOffer(Reservation reservation);

    void refuseReservationProcessing(Reservation reservation);

    void addReservation(Reservation reservation, User user);

    void deleteReservation(int reservationId);

}
