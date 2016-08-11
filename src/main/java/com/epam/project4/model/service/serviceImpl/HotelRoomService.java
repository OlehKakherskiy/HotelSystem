package main.java.com.epam.project4.model.service.serviceImpl;

import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.exception.RequestException;
import main.java.com.epam.project4.exception.SystemException;
import main.java.com.epam.project4.model.dao.AbstractHotelRoomDao;
import main.java.com.epam.project4.model.dao.AbstractReservationDao;
import main.java.com.epam.project4.model.entity.HotelRoom;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.roomParameter.ParameterValue;
import main.java.com.epam.project4.model.service.IHotelRoomService;
import main.java.com.epam.project4.model.service.IParameterValueService;

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
     * for executing operations with {@link main.java.com.epam.project4.model.entity.Reservation}
     */
    private AbstractReservationDao reservationDao;

    /**
     * for executing operations with {@link ParameterValue}
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
        List<HotelRoom> hotelRoomList = null;
        try {
            hotelRoomList = hotelRoomDao.getAllFullDetails(onlyActive);
            hotelRoomList.stream().forEach(this::appendReformattedRoomParams);
            hotelRoomList.forEach(hotelRoom -> hotelRoom.setPrice(calculateRoomBasicPrice(hotelRoom)));
        } catch (DaoException e) {
            throw new SystemException(MessageCode.GET_ALL_ROOMS_FULL_DETAILS_SYSTEM_EXCEPTION, e);
        }
        return hotelRoomList;
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
     * @throws RequestException {@inheritDoc}
     * @throws SystemException  {@inheritDoc}
     */
    @Override
    public HotelRoom getFullDetails(int id) {
        try {
            HotelRoom result = hotelRoomDao.read(id);
            if (result == null) {
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
     * appends room list of {@link ParameterValue} from their ids
     *
     * @param hotelRoom target room, for which parameters will be reformatted from ids to objects
     */
    private void appendReformattedRoomParams(HotelRoom hotelRoom) {
        hotelRoom.setParameters(parameterValueService.getParamValueList(hotelRoom.getParametersIds()));
    }

    /**
     * Calculates basic room's price. Each {@link ParameterValue} has a {@link ParameterValue#price}.
     * Method sums up all parameterValue prices, which are assosiated with target hotel room.
     *
     * @param room {@link HotelRoom}, for which price will be calculated
     * @return hotel room's price
     */
    private int calculateRoomBasicPrice(HotelRoom room) {
        return room.getParameters().stream().map(ParameterValue::getPrice).reduce((accumulator, price) -> accumulator + price).orElse(0);
    }
}