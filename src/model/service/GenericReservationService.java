package model.service;

import model.dao.GenericReservationDao;
import model.entity.HotelRoom;
import model.entity.Reservation;
import model.entity.User;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericReservationService extends GenericService<GenericReservationDao, Reservation, Integer> {

    public GenericReservationService(GenericReservationDao dao) {
        super(dao);
    }

    public abstract List<Reservation> getShortInfoAboutAllReservations(User user);

    public abstract Reservation getReservationDetailInfo(int reservationID, User user);

    public abstract void offerHotelRoom(Reservation reservation, HotelRoom hotelRoom);

    public abstract void setStatusToRefused(Reservation reservation);

    public abstract void setStatusToSubmitted(Reservation reservation);

    public abstract void addReservation(Reservation reservation, User user);

}
