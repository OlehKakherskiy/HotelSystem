package main.java.com.epam.project4.model.dao;

import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericReservationDao extends TransparentGenericDao<Reservation, Integer> {

    public abstract List<Reservation> getAllRoomReservationsInPeriod(int roomID, ReservationStatus status, LocalDate startDate, LocalDate endDate);

    public abstract List<Reservation> getAllUserReservationsShortInfo(int userID, ReservationStatus status);

    public abstract List<Reservation> getAllReservationsShortInfoInPeriod(ReservationStatus status, LocalDate startDate, LocalDate endDate);

}
