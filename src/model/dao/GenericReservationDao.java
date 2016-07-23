package model.dao;

import model.entity.Reservation;
import model.entity.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericReservationDao extends TransparentGenericDao<Reservation, Integer> {

    public abstract List<Reservation> getAllRoomSubmittedReservationsInPeriod(Integer roomID, LocalDate startDate, LocalDate endDate);

    public abstract List<Reservation> getAllUserReservationsShortInfo(Integer userID, ReservationStatus status);

    public abstract void changeReservationStatus(Reservation reservation, ReservationStatus status);
}
