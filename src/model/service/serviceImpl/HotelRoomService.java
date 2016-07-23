package model.service.serviceImpl;

import app.GlobalContext;
import app.constants.GlobalContextConstant;
import model.dao.DaoManager;
import model.dao.GenericHotelRoomDao;
import model.dao.GenericReservationDao;
import model.entity.HotelRoom;
import model.entity.Reservation;
import model.service.GenericHotelRoomService;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class HotelRoomService extends GenericHotelRoomService {

    private GenericReservationDao reservationDao;

    public HotelRoomService(GenericHotelRoomDao dao, GenericReservationDao reservationDao) {
        super(dao);
        this.reservationDao = reservationDao;
    }

    @Override
    public List<HotelRoom> getAllRoomShortDetails() {
        return dao.getAllShortDetails();
    }

    @Override
    public HotelRoom getFullDetailsWithMonthReservationsDetails(int ID, Month month, Year year) {
        HotelRoom result = dao.read(ID);

        try {
            DaoManager daoManager = (DaoManager) GlobalContext.getValue(GlobalContextConstant.DAO_MANAGER);

            GenericReservationDao reservationDao = daoManager.getDAO(GenericReservationDao.class); //TODO:плохо, не оттестировать

            LocalDate startDate = LocalDate.of(year.getValue(), month.getValue(), 1);
            LocalDate endMonthDate = startDate.with(lastDayOfMonth());

            result.getReservationList().addAll(getSubmittedReservations(startDate, endMonthDate,
                    reservationDao, result.getRoomID()));
        } catch (InstantiationException | IllegalAccessException e) { //TODO: mb DI usage
            e.printStackTrace();
        }
        return result;
    }

    private List<Reservation> getSubmittedReservations(LocalDate startDate, LocalDate endDate,
                                                       GenericReservationDao reservationDao, int roomID) {
        return reservationDao.getAllRoomSubmittedReservationsInPeriod
                (roomID, startDate, endDate);
    }


}
