package main.java.com.epam.project4.model.service.serviceImpl;

import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.exception.RequestException;
import main.java.com.epam.project4.exception.SystemException;
import main.java.com.epam.project4.model.dao.GenericHotelRoomDao;
import main.java.com.epam.project4.model.dao.GenericReservationDao;
import main.java.com.epam.project4.model.entity.HotelRoom;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.roomParameter.ParameterValue;
import main.java.com.epam.project4.model.service.AbstractHotelRoomService;
import main.java.com.epam.project4.model.service.AbstractParameterValueService;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class HotelRoomService implements AbstractHotelRoomService {

    private GenericHotelRoomDao hotelRoomDao;

    private GenericReservationDao reservationDao;

    private AbstractParameterValueService parameterValueService;

    public HotelRoomService(GenericHotelRoomDao hotelRoomDao, GenericReservationDao reservationDao, AbstractParameterValueService parameterValueService) {
        this.hotelRoomDao = hotelRoomDao;
        this.reservationDao = reservationDao;
        this.parameterValueService = parameterValueService;
    }

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

    @Override
    public void appendSubmittedReservations(HotelRoom hotelRoom, Month month, Year year, ReservationStatus status) {
        LocalDate startDate = LocalDate.of(year.getValue(), month.getValue(), 1);
        LocalDate endMonthDate = startDate.with(lastDayOfMonth());
        try {
            hotelRoom.setReservationList(reservationDao.getAllRoomReservationsInPeriod(hotelRoom.getRoomID(),
                    status, startDate, endMonthDate));
        } catch (DaoException e) {
            throw new SystemException(MessageCode.GET_ROOM_MONTH_RESERVATION_LIST_SYSTEM_EXCEPTION, e, hotelRoom.getRoomID(), month, year, status);
        }
    }

    @Override
    public HotelRoom getFullDetails(int id) {
        try {
            HotelRoom result = hotelRoomDao.read(id);
            if (result == null) {
                throw new RequestException(MessageCode.WRONG_ROOM_ID_SYSTEM_EXCEPTION, id);
            }
            appendReformattedRoomParams(result);
            result.setPrice(calculateRoomBasicPrice(result));
            return result;
        } catch (DaoException e) {
            throw new SystemException(MessageCode.READ_ROOM_SYSTEM_EXCEPTION, id);
        }
    }

    private void appendReformattedRoomParams(HotelRoom hotelRoom) {
        hotelRoom.setParameters(parameterValueService.getParamValueList(hotelRoom.getParametersIds()));
    }

    private int calculateRoomBasicPrice(HotelRoom room) {
        return room.getParameters().stream().map(ParameterValue::getPrice).reduce((accumulator, price) -> accumulator + price).orElse(0);
    }
}