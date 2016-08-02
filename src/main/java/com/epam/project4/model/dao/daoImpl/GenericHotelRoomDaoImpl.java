package main.java.com.epam.project4.model.dao.daoImpl;

import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.model.dao.GenericHotelRoomDao;
import main.java.com.epam.project4.model.entity.HotelRoom;

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

    private static final String SELECT_ROOM_REQUEST = "SELECT * FROM Hotel_Room WHERE id_room = ? AND is_active = 1";

    private static final String READ_ROOM_PARAMS_REQUEST = "Select id_Parameter_Values from Hotel_Room_Characteristics " +
            "WHERE id_hotel_room = ?";

    private static final String GET_ALL_ROOMS_REQUEST = "SELECT * FROM Hotel_Room";

    private static final String GET_ALL_ACTIVE_ROOMS_REQUEST = "SELECT * FROM Hotel_Room WHERE is_active = 1";

    private static final String getAllHotelRoomsDetailsException = "Exception caused during executing SQL request for getting details about hotel rooms";

    private static final String GET_HOTEL_ROOM_DATA_EXCEPTION = "Exception caused during the executing SQL request for getting HotelRoom data process";

    private static final String GET_HOTEL_ROOM_PARAMS_EXCEPTION = "Exception caused while was attempt to read from BD or ResultSet all room parameters for room's ID = %d";

    private static final String BUILD_HOTEL_ROOM_FROM_RESULT_SET_EXCEPTION = "Exception caused while was attempt to map ResultSet object to %s";

    private DataSource dataSource;

    @Override
    public List<HotelRoom> getAllFullDetails(boolean onlyActive) throws DaoException { //TODO:
        List<HotelRoom> hotelRooms = new ArrayList<>();
        String req = onlyActive ? GET_ALL_ACTIVE_ROOMS_REQUEST : GET_ALL_ROOMS_REQUEST;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(req);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                HotelRoom current = buildRoom(resultSet);
                current.setParametersIds(getRoomParamsIDs(current.getRoomID(), connection));
                hotelRooms.add(current);
            }

        } catch (SQLException e) {
            throw new DaoException(getAllHotelRoomsDetailsException);
        }
        return hotelRooms;
    }

    @Override
    public HotelRoom read(Integer id) throws DaoException {
        HotelRoom room = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ROOM_REQUEST)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                room = buildRoom(resultSet);
            }

            if (room != null) {
                room.setParametersIds(getRoomParamsIDs(room.getRoomID(), connection));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DaoException(GET_HOTEL_ROOM_DATA_EXCEPTION, e);
        }
        return room;
    }

    private List<Integer> getRoomParamsIDs(int roomId, Connection connection) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(READ_ROOM_PARAMS_REQUEST)) {
            preparedStatement.setInt(1, roomId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Integer> list = new ArrayList<>();
            while (resultSet.next()) {
                list.add(resultSet.getInt(1));
            }
            return list;
        } catch (SQLException e) {
            throw new DaoException(String.format(GET_HOTEL_ROOM_PARAMS_EXCEPTION, roomId), e);
        }
    }

    private HotelRoom buildRoom(ResultSet resultSet) throws DaoException {
        try {
            HotelRoom hotelRoom = new HotelRoom();
            hotelRoom.setRoomID(resultSet.getInt(1));
            hotelRoom.setRoomName(resultSet.getString(2));
            hotelRoom.setActivationStatus(resultSet.getBoolean(3));
            return hotelRoom;
        } catch (SQLException e) {
            throw new DaoException(String.format(BUILD_HOTEL_ROOM_FROM_RESULT_SET_EXCEPTION, HotelRoom.class.getName()), e);
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}