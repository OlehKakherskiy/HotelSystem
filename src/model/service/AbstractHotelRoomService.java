package model.service;

import model.entity.HotelRoom;
import model.entity.enums.ReservationStatus;

import java.time.Month;
import java.time.Year;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface AbstractHotelRoomService extends AbstractService {

    List<HotelRoom> getAllRoomShortDetails();

    HotelRoom getFullDetailsWithMonthReservationsDetails(int ID, Month month, Year year, ReservationStatus status);

}
