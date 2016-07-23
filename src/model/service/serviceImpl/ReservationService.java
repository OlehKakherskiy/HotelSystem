package model.service.serviceImpl;

import app.GlobalContext;
import app.constants.GlobalContextConstant;
import model.dao.DaoManager;
import model.dao.GenericHotelRoomDao;
import model.dao.GenericReservationDao;
import model.dao.GenericUserDao;
import model.entity.HotelRoom;
import model.entity.Reservation;
import model.entity.User;
import model.entity.enums.ReservationStatus;
import model.entity.enums.UserType;
import model.service.GenericReservationService;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ReservationService extends GenericReservationService {

    public ReservationService(GenericReservationDao dao) {
        super(dao);
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
            try {
                DaoManager daoManager = (DaoManager) GlobalContext.getValue(GlobalContextConstant.DAO_MANAGER);

                GenericUserDao userDao = daoManager.getDAO(GenericUserDao.class);
                GenericHotelRoomDao hotelRoomDao = daoManager.getDAO(GenericHotelRoomDao.class);

                reservation.setUser(userDao.read(reservation.getUser().getIdUser()));
                reservation.setHotelRoom(hotelRoomDao.getShortDetails(reservation.getHotelRoomID()));

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
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
        dao.changeReservationStatus(reservation, ReservationStatus.SUBMITTED);
    }

    @Override
    public void addReservation(Reservation reservation, User user) {
        reservation.setUser(user);
        dao.save(reservation);
    }


}
