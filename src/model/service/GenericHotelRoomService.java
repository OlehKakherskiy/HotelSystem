package model.service;

import model.dao.GenericHotelRoomDao;
import model.entity.HotelRoom;

import java.time.Month;
import java.time.Year;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericHotelRoomService extends GenericService<GenericHotelRoomDao, HotelRoom, Integer> {

    public GenericHotelRoomService(GenericHotelRoomDao dao) {
        super(dao);
    }

    public abstract List<HotelRoom> getAllRoomShortDetails();

    public abstract HotelRoom getFullDetailsWithMonthReservationsDetails(int ID, Month month, Year year);

//    public abstract List<HotelRoom> getAllRoomMonthReservationDetails(Month month, Year year);
}
