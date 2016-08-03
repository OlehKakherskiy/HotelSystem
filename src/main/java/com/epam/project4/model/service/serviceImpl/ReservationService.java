package main.java.com.epam.project4.model.service.serviceImpl;

import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.exception.RequestException;
import main.java.com.epam.project4.exception.SystemException;
import main.java.com.epam.project4.model.dao.GenericHotelRoomDao;
import main.java.com.epam.project4.model.dao.GenericReservationDao;
import main.java.com.epam.project4.model.entity.HotelRoom;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.service.AbstractParameterValueService;
import main.java.com.epam.project4.model.service.AbstractReservationService;
import main.java.com.epam.project4.model.service.AbstractUserService;

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
        try {
            return dao.getAllUserReservationsShortInfo(user.getIdUser(), reservationStatus);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.GET_USER_RESERVATION_LIST_SYSTEM_EXCEPTION, e, user.getIdUser(), reservationStatus);
        }
    }

    @Override
    public List<Reservation> getShortInfoAboutAllReservations(ReservationStatus reservationStatus) {
        try {
            return dao.getAllReservationsShortInfo(reservationStatus);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.GET_RESERVATION_LIST_SYSTEM_EXCEPTION, e, reservationStatus);
        }
    }

    @Override
    public Reservation getReservationDetailInfo(int reservationId) {
        try {
            Reservation reservation = dao.read(reservationId);
            if (reservation == null) {
                throw new RequestException(MessageCode.WRONG_RESERVATION_ID_EXCEPTION, reservationId);
            }
            reservation.setRequestParameters(parameterValueService.getParamValueList(reservation.getRequestParametersIds()));
            reservation.getRequestParametersIds().clear();
            reservation.setRequestParametersIds(null);

            return reservation;
        } catch (DaoException e) {
            throw new SystemException(MessageCode.READ_RESERVATION_SYSTEM_EXCEPTION, e, reservationId);
        }
    }

    @Override
    public void offerHotelRoom(Reservation reservation, HotelRoom hotelRoom) {
        try {
            update(reservation, ReservationStatus.WAITING_FOR_ANSWER, hotelRoom, true);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.OFFER_HOTEL_ROOM_SYSTEM_EXCEPTION, e, reservation.getId(), hotelRoom.getRoomID());
        }
    }

    @Override
    public void refuseReservationOffer(Reservation reservation) {
        try {
            update(reservation, ReservationStatus.PROCESSING, null, true);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.REFUSE_RESERVATION_OFFER_SYSTEM_EXCEPTION, e, reservation.getId());
        }
    }

    @Override
    public void submitReservationOffer(Reservation reservation) {
        try {
            update(reservation, ReservationStatus.SUBMITTED, null, false);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.SUBMIT_RESERVATION_OFFER_SYSTEM_EXCEPTION, e, reservation.getId());
        }
    }

    @Override
    public void refuseReservationProcessing(Reservation reservation) {
        try {
            update(reservation, ReservationStatus.REFUSED, null, false);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.REFUSE_RESERVATION_PROCESSING_SYSTEM_EXCEPTION, e, reservation.getId());
        }
    }

    @Override
    public void addReservation(Reservation reservation, User user) {
        reservation.setUserID(user.getIdUser());
        try {
            dao.save(reservation);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.SAVE_RESERVATION_SYSTEM_EXCEPTION, reservation);
        }
    }

    @Override
    public void deleteReservation(int reservationId) {
        try {
            boolean wasDeleted = dao.delete(reservationId);
            if (!wasDeleted) {
                throw new RequestException(MessageCode.WRONG_RESERVATION_ID_EXCEPTION, reservationId);
            }
        } catch (DaoException e) {
            throw new SystemException(MessageCode.DELETE_RESERVATION_SYSTEM_EXCEPTION, reservationId);
        }
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
    private void update(Reservation reservation, ReservationStatus status, HotelRoom room, boolean updateHotelRoom) throws DaoException {
        if (status == ReservationStatus.ALL) {
            throw new RequestException(MessageCode.WRONG_RESERVATION_STATUS_FOR_UPDATING_EXCEPTION, status.getName());
        }
        reservation.setStatus(status);
        if (updateHotelRoom) {
            reservation.setHotelRoom(room);
            reservation.setHotelRoomID((room == null) ? -1 : room.getRoomID());
        }
        dao.update(reservation);
    }
}