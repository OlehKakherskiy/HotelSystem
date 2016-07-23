package model.dao;

import model.entity.HotelRoom;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericHotelRoomDao extends TransparentGenericDao<HotelRoom, Integer> {

    public abstract HotelRoom getShortDetails(int id);

    public abstract double getRoomPrice(int id);

    public abstract List<HotelRoom> getAllShortDetails();

//    List<HotelRoom> findMatches(List<>)
    //TODO: поиск по параметрам

}