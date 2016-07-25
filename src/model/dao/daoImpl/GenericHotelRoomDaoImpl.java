package model.dao.daoImpl;

import model.dao.GenericHotelRoomDao;
import model.entity.HotelRoom;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GenericHotelRoomDaoImpl extends GenericHotelRoomDao {


    private static final String readRoom = "SELECT * FROM Hotel_Room WHERE id_room = ? AND is_active = 1";

    private static final String readRoomParams =
            "Select id_Parameter_Values from Hotel_Room_Characteristics " +
                    "WHERE id_hotel_room = ?";

    private static final String getAllRooms = "SELECT * FROM Hotel_Room";

    private static final String getAllActiveRooms = "SELECT * FROM Hotel_Room WHERE is_active = 1";

    private DataSource dataSource;

    @Override
    public List<HotelRoom> getAllShortDetails(boolean onlyActive) {
        List<HotelRoom> hotelRooms = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String req = onlyActive ? getAllActiveRooms : getAllRooms;
            PreparedStatement preparedStatement = connection.prepareStatement(req);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                hotelRooms.add(buildRoom(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (hotelRooms.size() == 0) {
            throw new RuntimeException(); //TODO: список комнат не может быть пустым.
        }
        return hotelRooms;
    }

    @Override
    public HotelRoom read(Integer id) {
        HotelRoom room = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(readRoom);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                room = buildRoom(resultSet);
            }
            if (room == null) {
                throw new RuntimeException(); //TODO: нет комнаты хотя должжна быть!!!!
            }
            room.setParametersIds(getRoomParamsIDs(room.getRoomID(), connection));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }

    private List<Integer> getRoomParamsIDs(int roomId, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(readRoomParams);
        preparedStatement.setInt(1, roomId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Integer> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(resultSet.getInt(1));
        }
        if (list.size() == 0) {
            throw new RuntimeException(); //TODO: у комнаты, которая существует, не может не быть параметров.
        }
        return list;
    }

    private HotelRoom buildRoom(ResultSet resultSet) throws SQLException {
        HotelRoom hotelRoom = null;
        hotelRoom = new HotelRoom();
        hotelRoom.setRoomID(resultSet.getInt(1));
        hotelRoom.setRoomName(resultSet.getString(2));
        hotelRoom.setIsActiveStatus(resultSet.getBoolean(3));
        return hotelRoom;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
