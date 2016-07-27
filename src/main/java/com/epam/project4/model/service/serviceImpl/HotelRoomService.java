package main.java.com.epam.project4.model.service.serviceImpl;

import main.java.com.epam.project4.model.dao.GenericHotelRoomDao;
import main.java.com.epam.project4.model.dao.GenericReservationDao;
import main.java.com.epam.project4.model.entity.HotelRoom;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.exception.SystemException;
import main.java.com.epam.project4.model.service.AbstractHotelRoomService;
import main.java.com.epam.project4.model.service.AbstractParameterValueService;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Collections;
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
        List<HotelRoom> hotelRoomList = hotelRoomDao.getAllFullDetails(onlyActive);
        if (hotelRoomList == null) {
            throw new SystemException(); //TODO: системная ошибка со списком комнат при загрузке
        }
        hotelRoomList.stream().forEach(this::appendReformattedRoomParams);
        return hotelRoomList;
    }

    @Override
    public void appendSubmittedReservations(HotelRoom hotelRoom, Month month, Year year, ReservationStatus status) {
        LocalDate startDate = LocalDate.of(year.getValue(), month.getValue(), 1);
        LocalDate endMonthDate = startDate.with(lastDayOfMonth());
        List<Reservation> periodReservations = reservationDao.getAllRoomReservationsInPeriod(hotelRoom.getRoomID(), status, startDate, endMonthDate);
        if (periodReservations == null) {
            hotelRoom.setReservationList(Collections.emptyList());
            throw new SystemException(); //TODO: возникла ошибка при считывании заказов на бронирование за период
        }
        hotelRoom.setReservationList(periodReservations);
    }

    @Override
    public HotelRoom getFullDetails(int id) {
        HotelRoom result = hotelRoomDao.read(id);
        if (result == null) {
            throw new SystemException(); //TODO: ошибка - не считалась с бд комната
        }
        appendReformattedRoomParams(result);
        return result;
    }

    private void appendReformattedRoomParams(HotelRoom hotelRoom) {
        hotelRoom.setParameters(parameterValueService.getParamValueList(hotelRoom.getParametersIds()));
    }


}
