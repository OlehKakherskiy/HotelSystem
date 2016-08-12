package main.java.com.hotelSystem.dao;

import main.java.com.hotelSystem.exception.DaoException;
import main.java.com.hotelSystem.model.HotelRoom;
import main.java.com.hotelSystem.model.MobilePhone;

import java.util.List;

/**
 * Interface extends basic CRUD operations which are performed whith {@link HotelRoom} entity.
 * Defines additional operation, which allows to get all {@link HotelRoom} objects, filtering by
 * {@link HotelRoom#getActivationStatus()}.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see MobilePhone
 */
public interface AbstractHotelRoomDao extends GenericDao<HotelRoom> {

    /**
     * Returns all rooms, declared in system, with all room's details. Result
     * can be filtered by parameter, which defines, only active rooms will be
     * returned or not.
     *
     * @param onlyActive defines, rooms with active status will be returned or with
     *                   all statuses
     * @return rooms, declared in system, with {@link HotelRoom#activationStatus} = true
     * or without filtering by status
     * @throws DaoException if exception was caused by processing operations with persistent
     *                      storage or during the mapping operation from storage formatted
     *                      data to object representation
     */
    List<HotelRoom> getAllFullDetails(boolean onlyActive) throws DaoException;
}