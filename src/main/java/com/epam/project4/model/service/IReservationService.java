package main.java.com.epam.project4.model.service;

import main.java.com.epam.project4.model.entity.HotelRoom;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;

import java.util.List;

/**
 * Interface represents API for performing operations, assosiated with {@link Reservation} entity
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see Reservation
 */
public interface IReservationService extends IService {

    /**
     * Returns list of {@link Reservation} for target user and reservation status.
     * If there's no reservations with these parameters - returns empty list.
     *
     * @param user              target user
     * @param reservationStatus target reservation status (if {@link ReservationStatus#ALL} - reservations with
     *                          all statuses will be returned)
     * @return reservation list of target user with target status or empty list
     * @throws main.java.com.epam.project4.exception.SystemException if exception was
     *                                                               thrown during processing any underlying operation
     *                                                               or if parameters are null.
     */
    List<Reservation> getShortInfoAboutAllReservations(User user, ReservationStatus reservationStatus);

    /**
     * Returns list of {@link Reservation} of all users and target reservation status.
     * If there's no reservations with this reservation status - returns empty list.
     *
     * @param reservationStatus target reservation status (if {@link ReservationStatus#ALL} - reservations with
     *                          all statuses will be returned)
     * @return reservation list of all users with target status or empty list
     * @throws main.java.com.epam.project4.exception.SystemException if exception was
     *                                                               thrown during processing any underlying operation
     */
    List<Reservation> getShortInfoAboutAllReservations(ReservationStatus reservationStatus);

    /**
     * Returns reservation, assosiated with target reservation's id.
     * Reservation's object will have instantiated all simple parameters and
     * also {@link Reservation#requestParameters}. If there's no reservation
     * with target id in system, {@link main.java.com.epam.project4.exception.RequestException} will
     * be thrown
     *
     * @param reservationId id, which assosiated reservation object will be returned
     * @return {@link Reservation} object, assosiated with target id
     * @throws main.java.com.epam.project4.exception.SystemException  if exception was
     *                                                                thrown during processing any underlying operation
     * @throws main.java.com.epam.project4.exception.RequestException if there's no reservation with target id
     */
    Reservation getReservationDetailInfo(int reservationId);

    /**
     * Offers hotel room for current reservation, changing reservation status to {@link ReservationStatus#PROCESSING},
     * setting this room as parameter to this reservation and updating changes in DB.
     *
     * @param reservation target reservation, to which hotel room will be offered
     * @param hotelRoom   hotel room, that will be offered to target reservation
     * @throws main.java.com.epam.project4.exception.SystemException if exception was
     *                                                               thrown during processing any underlying operation
     */
    void offerHotelRoom(Reservation reservation, HotelRoom hotelRoom);

    /**
     * refuses offer, which was done by {@link #offerHotelRoom(Reservation, HotelRoom)} for target reservation. Changes
     * reservation status to {@link ReservationStatus#PROCESSING} and resets values of {@link Reservation#hotelRoomID} and
     * {@link Reservation#hotelRoom}.
     *
     * @param reservation target reservation, for which offer will be refused.
     * @throws main.java.com.epam.project4.exception.SystemException if exception was
     *                                                               thrown during processing any underlying operation
     */
    void refuseReservationOffer(Reservation reservation);

    /**
     * submits offer, which was done by {@link #offerHotelRoom(Reservation, HotelRoom)} for target reservation. Changes
     * reservation status to {@link ReservationStatus#SUBMITTED}.
     *
     * @param reservation target reservation, for which offer will be submitted.
     * @throws main.java.com.epam.project4.exception.SystemException if exception was
     *                                                               thrown during processing any underlying operation
     */
    void submitReservationOffer(Reservation reservation);

    /**
     * refuses reservation processing for target reservation. Changes
     * reservation status to {@link ReservationStatus#REFUSED}.
     *
     * @param reservation target reservation, for which processing was refused.
     * @throws main.java.com.epam.project4.exception.SystemException if exception was
     *                                                               thrown during processing any underlying operation
     */
    void refuseReservationProcessing(Reservation reservation);

    /**
     * adds reservation to system for target user with inited fields : {@link Reservation#dateFrom},
     * {@link Reservation#dateTo}, {@link Reservation#requestDate}, {@link Reservation#requestParameters}, and, possibly,
     * {@link Reservation#comment}.
     *
     * @param reservation reservation, that will be added to system
     * @param user        user, which reservation is
     * @throws main.java.com.epam.project4.exception.SystemException if exception was
     *                                                               thrown during processing any underlying operation or
     *                                                               if params are null
     */
    void addReservation(Reservation reservation, User user);

    /**
     * removes reservation with target id from system. If there's no reservation with current id,
     * {@link main.java.com.epam.project4.exception.RequestException} will be thrown.
     *
     * @param reservationId id, assosiated reservation will be removed from the system
     * @throws main.java.com.epam.project4.exception.RequestException if there's no reservation with the target id in system
     * @throws main.java.com.epam.project4.exception.SystemException  if exception was
     *                                                                thrown during processing any underlying operation or
     */
    void deleteReservation(int reservationId);
}