package model.service.serviceImpl;

import model.dao.GenericHotelRoomDao;
import model.dao.GenericReservationDao;
import model.dao.GenericUserDao;
import model.entity.HotelRoom;
import model.entity.Reservation;
import model.entity.User;
import model.entity.enums.ReservationStatus;
import model.entity.enums.UserType;
import model.service.AbstractReservationService;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ReservationService implements AbstractReservationService {

    private GenericReservationDao dao;

    private GenericHotelRoomDao hotelRoomDao;

    private GenericUserDao userDao;

    public ReservationService(GenericReservationDao dao) {
        this.dao = dao;
    }

    @Override
    public List<Reservation> getShortInfoAboutAllReservations(User user) {
        ReservationStatus status = (user.getUserType() == UserType.ADMIN) ? ReservationStatus.PROCESSING :
                ReservationStatus.ALL;
        return dao.getAllUserReservationsShortInfo(user.getIdUser(), status);
    }

    @Override
    public Reservation getReservationDetailInfo(int reservationID, User user) {
        Reservation reservation = dao.read(reservationID);
        if (user.getUserType() == UserType.ADMIN) {

            reservation.setUser(userDao.read(reservation.getUser().getIdUser()));
            reservation.setHotelRoom(hotelRoomDao.getShortDetails(reservation.getHotelRoomID()));
        }
        return reservation;
    }

    @Override
    public void offerHotelRoom(Reservation reservation, HotelRoom hotelRoom) {
        reservation.setHotelRoom(hotelRoom);
        reservation.setStatus(ReservationStatus.ANSWERED);
        dao.update(reservation); //TODO: exception
    }

    @Override
    public void setStatusToRefused(Reservation reservation) {
        reservation.setHotelRoom(null);
        reservation.setStatus(ReservationStatus.PROCESSING);
        dao.update(reservation);
    }

    @Override
    public void setStatusToSubmitted(Reservation reservation) {
        reservation.setStatus(ReservationStatus.SUBMITTED);
        dao.changeReservationStatus(reservation);
    }

    @Override
    public void addReservation(Reservation reservation, User user) {
        reservation.setUser(user);
        dao.save(reservation);
    }

}
