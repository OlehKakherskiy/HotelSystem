package main.java.com.hotelSystem.service;

import main.java.com.hotelSystem.exception.RequestException;
import main.java.com.hotelSystem.exception.SystemException;
import main.java.com.hotelSystem.model.HotelRoom;
import main.java.com.hotelSystem.model.enums.ReservationStatus;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;

/**
 * Interface represents API for executing operations, assosiated with {@link HotelRoom} entity
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see HotelRoom
 */
public interface IHotelRoomService extends IService {

    /**
     * Returns all rooms with full details of each one
     * (without reservation list, with room parameters and price).
     *
     * @param onlyActive whether the room is active
     * @return room list with full details of each one (without reservation list)
     * @throws SystemException if exception was
     *                         thrown during processing any underlying operation
     */
    List<HotelRoom> getAllRoomFullDetails(boolean onlyActive);

    /**
     * Returns {@link HotelRoom} object with all details (without reservation list and with room's parameters),
     * assosiated with target id. If there's no room with target id,
     * throws {@link RequestException}
     *
     * @param id Hotel room's id
     * @return {@link HotelRoom} object, assosiated with target id
     * @throws RequestException if there's no hotel room
     *                          with target id in system
     * @throws SystemException  if exception was
     *                          thrown during processing any underlying operation
     */
    HotelRoom getFullDetails(int id);

    /**
     * Appends reservations with specific status for target hotel room. Appended reservation list will be selected
     * for target month and year. If there's no submitted reservation, booked in the target month and year,
     * returns empty list. If month or year is null, it'll use month or year of {@link LocalDate#now() request date}.
     *
     * @param hotelRoom hotel room, for which month's reservation list will be returned
     * @param month     target month
     * @param year      target year
     * @param status    specific reservation's status
     * @throws SystemException if exception was
     *                         thrown during processing any underlying operation
     *                         or if one input parameter is null.
     */
    void appendReservations(HotelRoom hotelRoom, Month month, Year year, ReservationStatus status);

}
