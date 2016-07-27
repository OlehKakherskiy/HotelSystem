package main.java.com.epam.project4.model.service.serviceImpl;

import main.java.com.epam.project4.model.entity.HotelRoom;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.service.AbstractUserService;
import main.java.com.epam.project4.model.dao.GenericHotelRoomDao;
import main.java.com.epam.project4.model.dao.GenericReservationDao;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.enums.UserType;
import main.java.com.epam.project4.model.service.AbstractParameterValueService;
import main.java.com.epam.project4.model.service.AbstractReservationService;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ReservationService implements AbstractReservationService {

    private GenericReservationDao dao;

    private GenericHotelRoomDao hotelRoomDao;

    private AbstractUserService userService;

    private AbstractParameterValueService parameterValueService;

    public ReservationService(GenericReservationDao dao, GenericHotelRoomDao hotelRoomDao,
                              AbstractUserService userService, AbstractParameterValueService parameterValueService) {
        this.dao = dao;
        this.hotelRoomDao = hotelRoomDao;
        this.userService = userService;
        this.parameterValueService = parameterValueService;
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
            reservation.setHotelRoom(hotelRoomDao.read(reservation.getHotelRoomID()));
        }

        parameterValueService.getParamValueList(reservation.getRequestParametersIds());
        reservation.getRequestParametersIds().clear();
        reservation.setRequestParametersIds(null);

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
        //TODO: в дао также инфу по параметрам сохранять
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
