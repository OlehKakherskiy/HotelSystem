package main.java.com.epam.project4.model.dao;

import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface AbstractReservationDao extends GenericDao<Reservation> {

    List<Reservation> getAllRoomReservationsInPeriod(int roomID, ReservationStatus status, LocalDate startDate, LocalDate endDate) throws DaoException;

    List<Reservation> getAllUserReservationsShortInfo(int userID, ReservationStatus status) throws DaoException;

    List<Reservation> getAllReservationsShortInfo(ReservationStatus status) throws DaoException;
}
