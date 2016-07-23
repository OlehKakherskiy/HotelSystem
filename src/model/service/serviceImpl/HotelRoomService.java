package model.service.serviceImpl;

import model.dao.GenericHotelRoomDao;
import model.dao.GenericReservationDao;
import model.entity.HotelRoom;
import model.entity.Reservation;
import model.service.AbstractHotelRoomService;

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

    public HotelRoomService(GenericHotelRoomDao hotelRoomDao, GenericReservationDao reservationDao) {
        this.hotelRoomDao = hotelRoomDao;
        this.reservationDao = reservationDao;
    }

    @Override
    public List<HotelRoom> getAllRoomShortDetails() {
        return hotelRoomDao.getAllShortDetails();
    }

    @Override
    public HotelRoom getFullDetailsWithMonthReservationsDetails(int ID, Month month, Year year) {
        HotelRoom result = hotelRoomDao.read(ID);

        LocalDate startDate = LocalDate.of(year.getValue(), month.getValue(), 1);
        LocalDate endMonthDate = startDate.with(lastDayOfMonth());

        result.getReservationList().addAll(getSubmittedReservations(startDate, endMonthDate,
                reservationDao, result.getRoomID()));
        return result;
    }

    private List<Reservation> getSubmittedReservations(LocalDate startDate, LocalDate endDate,
                                                       GenericReservationDao reservationDao, int roomID) {
        return reservationDao.getAllRoomSubmittedReservationsInPeriod
                (roomID, startDate, endDate);
    }

}
