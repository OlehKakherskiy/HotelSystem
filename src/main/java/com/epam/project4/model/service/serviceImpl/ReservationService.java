package main.java.com.epam.project4.model.service.serviceImpl;

import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.exception.RequestException;
import main.java.com.epam.project4.exception.SystemException;
import main.java.com.epam.project4.model.dao.AbstractReservationDao;
import main.java.com.epam.project4.model.entity.HotelRoom;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.service.IParameterValueService;
import main.java.com.epam.project4.model.service.IReservationService;

import java.util.List;

/**
 * Class represents implementation of {@link IParameterValueService} and
 * uses {@link IParameterValueService} and {@link AbstractReservationDao} for
 * executing business-logic operations.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see IParameterValueService
 * @see AbstractReservationDao
 */
public class ReservationService implements IReservationService {

    /**
     * for executing operations with {@link Reservation}
     */
    private AbstractReservationDao dao;

    /**
     * for executing operations with {@link main.java.com.epam.project4.model.entity.roomParameter.ParameterValue}
     */
    private IParameterValueService parameterValueService;

    /**
     * Inits all fields. Each parameter MUSTN'T be null.
     *
     * @param dao                   inits {@link #dao}
     * @param parameterValueService inits {@link #parameterValueService}
     */
    public ReservationService(AbstractReservationDao dao, IParameterValueService parameterValueService) {
        this.dao = dao;
        this.parameterValueService = parameterValueService;
    }

    /**
     * {@inheritDoc}
     *
     * @param user              target user
     * @param reservationStatus target reservation status (if {@link ReservationStatus#ALL} - reservations with
     *                          all statuses will be returned)
     * @return {@inheritDoc}
     * @throws SystemException {@inheritDoc}
     */
    @Override
    public List<Reservation> getShortInfoAboutAllReservations(User user, ReservationStatus reservationStatus) {
        try {
            return dao.getAllUserReservationsShortInfo(user.getIdUser(), reservationStatus);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.GET_USER_RESERVATION_LIST_SYSTEM_EXCEPTION, e, user.getIdUser(), reservationStatus);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param reservationStatus target reservation status (if {@link ReservationStatus#ALL} - reservations with
     *                          all statuses will be returned)
     * @return {@inheritDoc}
     * @throws {@inheritDoc}
     */
    @Override
    public List<Reservation> getShortInfoAboutAllReservations(ReservationStatus reservationStatus) {
        try {
            return dao.getAllReservationsShortInfo(reservationStatus);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.GET_RESERVATION_LIST_SYSTEM_EXCEPTION, e, reservationStatus);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param reservationId id, which assosiated reservation object will be returned
     * @return {@inheritDoc}
     * @throws RequestException {@inheritDoc}
     * @throws SystemException  {@inheritDoc}
     */
    @Override
    public Reservation getReservationDetailInfo(int reservationId) {
        try {
            Reservation reservation = dao.read(reservationId);
            if (reservation == null) {
                throw new RequestException(MessageCode.WRONG_RESERVATION_ID_EXCEPTION, reservationId);
            }
            reservation.setRequestParameters(parameterValueService.getParamValueList(reservation.getRequestParametersIds()));
            reservation.getRequestParametersIds().clear();

            return reservation;
        } catch (DaoException e) {
            throw new SystemException(MessageCode.READ_RESERVATION_SYSTEM_EXCEPTION, e, reservationId);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param reservation target reservation, to which hotel room will be offered
     * @param hotelRoom   hotel room, that will be offered to target reservation
     * @throws SystemException {@inheritDoc}
     */
    @Override
    public void offerHotelRoom(Reservation reservation, HotelRoom hotelRoom) {
        try {
            update(reservation, ReservationStatus.WAITING_FOR_ANSWER, hotelRoom, true);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.OFFER_HOTEL_ROOM_SYSTEM_EXCEPTION, e, reservation.getId(), hotelRoom.getRoomID());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param reservation target reservation, for which offer will be refused.
     * @throws SystemException {@inheritDoc}
     */
    @Override
    public void refuseReservationOffer(Reservation reservation) {
        try {
            update(reservation, ReservationStatus.PROCESSING, null, true);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.REFUSE_RESERVATION_OFFER_SYSTEM_EXCEPTION, e, reservation.getId());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param reservation target reservation, for which offer will be submitted.
     * @throws SystemException {@inheritDoc}
     */
    @Override
    public void submitReservationOffer(Reservation reservation) {
        try {
            update(reservation, ReservationStatus.SUBMITTED, null, false);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.SUBMIT_RESERVATION_OFFER_SYSTEM_EXCEPTION, e, reservation.getId());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param reservation target reservation, for which processing was refused.
     * @throws SystemException{@inheritDoc}
     */
    @Override
    public void refuseReservationProcessing(Reservation reservation) {
        try {
            update(reservation, ReservationStatus.REFUSED_FROM_PROCESSING, null, false);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.REFUSE_RESERVATION_PROCESSING_SYSTEM_EXCEPTION, e, reservation.getId());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param reservation reservation, that will be added to system
     * @param user        user, which reservation is
     * @throws {@inheritDoc}
     */
    @Override
    public void addReservation(Reservation reservation, User user) {
        reservation.setUserID(user.getIdUser());
        try {
            dao.save(reservation);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.SAVE_RESERVATION_SYSTEM_EXCEPTION, reservation);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param reservationId id, assosiated reservation will be removed from the system
     * @throws RequestException {@inheritDoc}
     * @throws SystemException  {@inheritDoc}
     */
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