package main.java.com.epam.project4.model.dao;

import main.java.com.epam.project4.model.entity.HotelRoom;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericHotelRoomDao extends TransparentGenericDao<HotelRoom, Integer> {

    public abstract List<HotelRoom> getAllFullDetails(boolean onlyActive);
}