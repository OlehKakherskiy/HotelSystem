package main.java.com.epam.project4.model.service;

import main.java.com.epam.project4.model.entity.HotelRoom;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;

import java.time.Month;
import java.time.Year;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface AbstractHotelRoomService extends AbstractService {

    List<HotelRoom> getAllRoomFullDetails(boolean onlyActive);

    HotelRoom getFullDetailsWithMonthReservationsDetails(int ID, Month month, Year year, ReservationStatus status);

}
