package main.java.com.hotelSystem.service.serviceImpl;

import main.java.com.hotelSystem.app.constants.MessageCode;
import main.java.com.hotelSystem.dao.AbstractHotelRoomDao;
import main.java.com.hotelSystem.dao.AbstractReservationDao;
import main.java.com.hotelSystem.exception.DaoException;
import main.java.com.hotelSystem.exception.RequestException;
import main.java.com.hotelSystem.exception.SystemException;
import main.java.com.hotelSystem.model.HotelRoom;
import main.java.com.hotelSystem.model.Reservation;
import main.java.com.hotelSystem.model.enums.ReservationStatus;
import main.java.com.hotelSystem.model.roomParameter.ParameterValueTuple;
import main.java.com.hotelSystem.service.IHotelRoomService;
import main.java.com.hotelSystem.service.IParameterValueService;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * Class represents implementation of {@link IHotelRoomService} and uses {@link AbstractHotelRoomDao},
 * {@link IParameterValueService} and {@link AbstractReservationDao} for executing business-logic operations.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see IHotelRoomService
 * @see AbstractHotelRoomDao
 * @see AbstractReservationDao
 */
public class HotelRoomService implements IHotelRoomService {

    /**
     * for executing operations with {@link HotelRoom}
     */
    private AbstractHotelRoomDao hotelRoomDao;

    /**
     * for executing operations with {@link Reservation}
     */
    private AbstractReservationDao reservationDao;

    /**
     * for executing operations with {@link ParameterValueTuple}
     */
    private IParameterValueService parameterValueService;

    /**
     * Inits all fields. Each parameter MUSTN'T be null.
     *
     * @param hotelRoomDao          inits {@link #hotelRoomDao}
     * @param reservationDao        inits {@link #reservationDao}
     * @param parameterValueService inits{@link #parameterValueService}
     */
    public HotelRoomService(AbstractHotelRoomDao hotelRoomDao, AbstractReservationDao reservationDao, IParameterValueService parameterValueService) {
        this.hotelRoomDao = hotelRoomDao;
        this.reservationDao = reservationDao;
        this.parameterValueService = parameterValueService;
    }

    /**
     * {@inheritDoc}
     *
     * @param onlyActive whether the room is active
     * @return {@inheritDoc}
     * @throws SystemException {@inheritDoc}
     */
    @Override
    public List<HotelRoom> getAllRoomFullDetails(boolean onlyActive) {
        try {
            List<HotelRoom> hotelRoomList = hotelRoomDao.getAllFullDetails(onlyActive);
            hotelRoomList.stream().forEach(hotelRoom -> {
                this.appendReformattedRoomParams(hotelRoom);
                hotelRoom.setPrice(calculateRoomBasicPrice(hotelRoom));
            });
            return hotelRoomList;
        } catch (DaoException e) {
            throw new SystemException(MessageCode.GET_ALL_ROOMS_FULL_DETAILS_SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param hotelRoom hotel room, for which month's reservation list will be returned
     * @param month     target month
     * @param year      target year
     * @param status    specific reservation's status
     * @throws SystemException {@inheritDoc}
     */
    @Override
    public void appendReservations(HotelRoom hotelRoom, Month month, Year year, ReservationStatus status) {
        LocalDate startDate = LocalDate.of(year.getValue(), month.getValue(), 1);
        LocalDate endMonthDate = startDate.with(lastDayOfMonth());
        try {
            hotelRoom.setReservationList(reservationDao.getAllRoomReservationsInPeriod(hotelRoom.getRoomId(),
                    status, startDate, endMonthDate));
        } catch (DaoException e) {
            throw new SystemException(MessageCode.GET_ROOM_MONTH_RESERVATION_LIST_SYSTEM_EXCEPTION, e, hotelRoom.getRoomId(), month, year, status);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param id Hotel room's id
     * @return {@inheritDoc}
     * @throws RequestException {@inheritDoc}. Also if id < 0
     * @throws SystemException  {@inheritDoc}
     */
    @Override
    public HotelRoom getFullDetails(int id) {
        HotelRoom result;
        try {
            if (id < 0 || (result = hotelRoomDao.read(id)) == null) {
                throw new RequestException(MessageCode.WRONG_ROOM_ID, id);
            }
            appendReformattedRoomParams(result);
            result.setPrice(calculateRoomBasicPrice(result));
            return result;
        } catch (DaoException e) {
            throw new SystemException(MessageCode.READ_ROOM_SYSTEM_EXCEPTION, id);
        }
    }

    /**
     * appends room list of {@link ParameterValueTuple} from their ids
     *
     * @param hotelRoom target room, for which parameters will be reformatted from ids to objects
     */
    private void appendReformattedRoomParams(HotelRoom hotelRoom) {
        hotelRoom.setParameters(parameterValueService.getParamValueList(hotelRoom.getParameterListIds()));
    }

    /**
     * Calculates basic room's price. Each {@link ParameterValueTuple} has a {@link ParameterValueTuple#price}.
     * Method sums up all parameterValue prices, which are assosiated with target hotel room.
     *
     * @param room {@link HotelRoom}, for which price will be calculated
     * @return hotel room's price
     */
    private int calculateRoomBasicPrice(HotelRoom room) {
        return room.getParameters().stream().map(ParameterValueTuple::getPrice).reduce((accumulator, price) -> accumulator + price).orElse(0);
    }
}