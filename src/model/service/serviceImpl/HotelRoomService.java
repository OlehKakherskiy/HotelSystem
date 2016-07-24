package model.service.serviceImpl;

import model.dao.GenericHotelRoomDao;
import model.dao.GenericReservationDao;
import model.entity.HotelRoom;
import model.entity.enums.ReservationStatus;
import model.entity.roomParameter.ParameterValue;
import model.service.AbstractHotelRoomService;
import model.service.AbstractParameterValueService;

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
    public List<HotelRoom> getAllRoomShortDetails() {
        return hotelRoomDao.getAllShortDetails(true);
    }

    @Override
    public HotelRoom getFullDetailsWithMonthReservationsDetails(int ID, Month month, Year year,
                                                                ReservationStatus status) {
        HotelRoom result = hotelRoomDao.read(ID);

        appendReformattedRoomParams(result);

        appendSubmittedReservations(result, month, year, status);

        return result;
    }

    private void appendReformattedRoomParams(HotelRoom hotelRoom) {
        List<ParameterValue> result = parameterValueService.getAllParams(hotelRoom.getParametersIds());
        hotelRoom.setParameters(result);
    }

    private void appendSubmittedReservations(HotelRoom hotelRoom, Month month, Year year, ReservationStatus status) {
        LocalDate startDate = LocalDate.of(year.getValue(), month.getValue(), 1);
        LocalDate endMonthDate = startDate.with(lastDayOfMonth());
        hotelRoom.setReservationList(reservationDao.getAllRoomReservationsInPeriod(hotelRoom.getRoomID(),
                status, startDate, endMonthDate));
    }
}
