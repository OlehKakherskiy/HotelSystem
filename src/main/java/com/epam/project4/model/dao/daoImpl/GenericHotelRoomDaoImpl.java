package main.java.com.epam.project4.model.dao.daoImpl;

import main.java.com.epam.project4.model.dao.GenericHotelRoomDao;
import main.java.com.epam.project4.model.entity.HotelRoom;
import main.java.com.epam.project4.model.exception.SystemException;

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
    public List<HotelRoom> getAllFullDetails(boolean onlyActive) { //TODO:
        List<HotelRoom> hotelRooms = new ArrayList<>();
        String req = onlyActive ? getAllActiveRooms : getAllRooms;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(req);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                HotelRoom current = buildRoom(resultSet);
                current.setParametersIds(getRoomParamsIDs(current.getRoomID(), connection));
                hotelRooms.add(current);
            }

        } catch (SQLException e) {
            hotelRooms = null;
        }
        return hotelRooms;
    }

    @Override
    public HotelRoom read(Integer id) {
        HotelRoom room = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(readRoom);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                room = buildRoom(resultSet);
            }

            room.setParametersIds(getRoomParamsIDs(room.getRoomID(), connection));
        } catch (SQLException e) {
            room.setParametersIds(new ArrayList<>());
            throw new SystemException(); //TODO: exceptionMessage
        }
        return room;
    }

    private List<Integer> getRoomParamsIDs(int roomId, Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(readRoomParams)) {
            preparedStatement.setInt(1, roomId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Integer> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getInt(1));
            }
            return list;
        } catch (SQLException e) {
            throw new SQLException(String.format("Exception caused while was attempt to read from BD or ResultSet all room parameters for room's ID = %d", roomId), e);
        }
    }

    private HotelRoom buildRoom(ResultSet resultSet) throws SQLException {
        try {
            HotelRoom hotelRoom = new HotelRoom();
            hotelRoom.setRoomID(resultSet.getInt(1));
            hotelRoom.setRoomName(resultSet.getString(2));
            hotelRoom.setActivationStatus(resultSet.getBoolean(3));
            return hotelRoom;
        } catch (SQLException e) {
            throw new SQLException(String.format("Exception caused while was attempt to map ResultSet object to %s", HotelRoom.class.getName()), e);
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}