package main.java.com.hotelSystem.dao;

import main.java.com.hotelSystem.exception.DaoException;
import main.java.com.hotelSystem.model.HotelRoom;
import main.java.com.hotelSystem.model.Reservation;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.model.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface extends basic CRUD operations which are performed whith {@link Reservation} entity.
 * Defines additional operations, that allow get all {@link Reservation} basic information,
 * filtering by date period and {@link HotelRoom}'s id, status
 * or {@link User}'s id and status
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see Reservation
 */
public interface AbstractReservationDao extends GenericDao<Reservation> {

    /**
     * Returns all reservations of target room in specific period of time and
     * with specific reservation status.
     *
     * @param roomID    target room's id
     * @param status    status of reservations
     * @param startDate date, from which reservations were requested
     * @param endDate   date, to which reservations were requested
     * @return list of reservations or empty list if there is no reservatiom matching target parameters
     * @throws DaoException if there was an exception during interaction with persistent storage or during the
     *                      process of mapping data from storage format to object representation
     */
    List<Reservation> getAllRoomReservationsInPeriod(int roomID, ReservationStatus status, LocalDate startDate, LocalDate endDate) throws DaoException;

    /**
     * Returns all reservations target user and with specific reservation status.
     *
     * @param userID target user's id
     * @param status status of reservations
     * @return list of reservations or empty list if there is no reservatiom matching target parameters
     * @throws DaoException if there was an exception during interaction with persistent storage or during the
     *                      process of mapping data from storage format to object representation
     */
    List<Reservation> getAllUserReservationsShortInfo(int userID, ReservationStatus status) throws DaoException;

    /**
     * Returns all reservations with specific reservation status
     *
     * @param status status of reservations
     * @return list of reservations or empty list if there is no reservatiom matching target parameters
     * @throws DaoException if there was an exception during interaction with persistent storage or during the
     *                      process of mapping data from storage format to object representation
     */
    List<Reservation> getAllReservationsShortInfo(ReservationStatus status) throws DaoException;
}
