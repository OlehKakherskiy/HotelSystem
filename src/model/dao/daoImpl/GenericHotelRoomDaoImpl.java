package model.dao.daoImpl;

import model.dao.GenericHotelRoomDao;
import model.entity.HotelRoom;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GenericHotelRoomDaoImpl extends GenericHotelRoomDao {



    @Override
    public HotelRoom getShortDetails(int id) {
        return null;
    }

    @Override
    public double getRoomPrice(int id) {
        return 0;
    }

    @Override
    public List<HotelRoom> getAllShortDetails() {
        return null;
    }
}
