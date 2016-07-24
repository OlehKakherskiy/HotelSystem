package model.service.serviceImpl;

import model.dao.GenericHotelRoomDao;
import model.dao.GenericReservationDao;
import model.entity.HotelRoom;
import model.entity.Reservation;
import model.entity.User;
import model.entity.enums.ReservationStatus;
import model.entity.enums.UserType;
import model.service.AbstractReservationService;
import model.service.AbstractUserService;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ReservationService implements AbstractReservationService {

    private GenericReservationDao dao;

    private GenericHotelRoomDao hotelRoomDao;

    private AbstractUserService userService;

    public ReservationService(GenericReservationDao dao, GenericHotelRoomDao hotelRoomDao, AbstractUserService userService) {
        this.dao = dao;
        this.hotelRoomDao = hotelRoomDao;
        this.userService = userService;
    }

    @Override
    public List<Reservation> getShortInfoAboutAllReservations(User user, ReservationStatus reservationStatus) {
        return dao.getAllUserReservationsShortInfo(user.getIdUser(), reservationStatus);
    }

    @Override
    public List<Reservation> getShortInfoAboutAllReservationsForAdminInPeriod(ReservationStatus reservationStatus, LocalDate dateFrom, LocalDate dateTo) {
        return dao.getAllReservationsShortInfoInPeriod(reservationStatus, dateFrom, dateTo);
    }

    @Override
    public Reservation getReservationDetailInfo(int reservationID, User user) {
        Reservation reservation = dao.read(reservationID);
        if (user.getUserType() == UserType.ADMIN) {
            reservation.setUser(userService.getSimpleUserInfo(reservation.getUser().getIdUser()));
            reservation.setHotelRoom(hotelRoomDao.getShortDetails(reservation.getHotelRoomID()));
        }
        return reservation;
    }

    @Override
    public void offerHotelRoom(Reservation reservation, HotelRoom hotelRoom) {
        update(reservation, ReservationStatus.ANSWERED, hotelRoom, true);
    }

    @Override
    public void setStatusToRefused(Reservation reservation) {
        update(reservation, ReservationStatus.PROCESSING, null, true);
    }

    @Override
    public void setStatusToSubmitted(Reservation reservation) {
        update(reservation, ReservationStatus.SUBMITTED, null, false);
    }

    @Override
    public void addReservation(Reservation reservation, User user) {
        reservation.setUserID(user.getIdUser());
        int newID = dao.save(reservation);

        if (newID == -1) {
            throw new RuntimeException(); //TODO: не вернул первичный ключ - ошибка
        }
        reservation.setId(newID);
    }


    /**
     * updates reservation details according to input parameters.
     *
     * @param reservation     target reservation object
     * @param status          {@link ReservationStatus}. MUST NOT be {@link ReservationStatus#ALL}
     * @param room            if null - resets to null room parameter in reservation in DB.
     *                        Must be used with updateHotelRoom parameter together
     * @param updateHotelRoom if true - signal that room parameter must be added for updating
     */
    private void update(Reservation reservation, ReservationStatus status, HotelRoom room, boolean updateHotelRoom) {
        reservation.setStatus(status);
        if (updateHotelRoom) {
            reservation.setHotelRoomID(room.getRoomID());
            reservation.setHotelRoom(room);
        }
        boolean wasUpdated = (status != ReservationStatus.ALL) && dao.update(reservation);
        if (!wasUpdated) {
            throw new RuntimeException(); //TODO: exception!!!
        }
    }

}
