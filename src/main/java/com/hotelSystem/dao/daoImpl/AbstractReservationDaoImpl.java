package main.java.com.hotelSystem.dao.daoImpl;

import main.java.com.hotelSystem.dao.AbstractReservationDao;
import main.java.com.hotelSystem.exception.DaoException;
import main.java.com.hotelSystem.manager.managerImpl.daoManagerImpl.ConnectionAllocator;
import main.java.com.hotelSystem.model.Reservation;
import main.java.com.hotelSystem.model.enums.ReservationStatus;
import main.java.com.hotelSystem.model.roomParameter.ParameterValueTuple;

import javax.sql.DataSource;
import java.sql.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Class represents implementation of {@link AbstractReservationDao} for relational databases, represented
 * via {@link DataSource}. It uses {@link ConnectionAllocator} for getting connection from datasource.
 * Restriction: do NOT close allocated connection.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see DataSource
 * @see ConnectionAllocator
 */
public class AbstractReservationDaoImpl implements AbstractReservationDao {

    //--------------------------------- SQL REQUESTS ----------------------------------------//

    /**
     * db request for selecting basic information about reservation
     */
    private static final String GET_SHORT_INFO_BASE = "SELECT ID, date_request, date_from, date_to, id_Reservation_Status " +
            "FROM Reservation ";

    /**
     * db request for selecting basic information about reservations of specific user
     */
    private static final String SHORT_INFO_ABOUT_ALL_FOR_USER = GET_SHORT_INFO_BASE + " WHERE id_User = ?";

    /**
     * db request for selecting basic information about reservations with specific status
     */
    private static final String GET_SHORT_INFO_ABOUT_ALL_FILTERING_BY_STATUS =
            GET_SHORT_INFO_BASE + " WHERE id_Reservation_Status=?";

    /**
     * db request for selecting basic information about reservations filtering by user's id and reservation's status
     */
    private static final String SHORT_INFO_ABOUT_ALL_FOR_USER_FILTERING_BY_USER_AND_STATUS =
            SHORT_INFO_ABOUT_ALL_FOR_USER + " AND id_Reservation_Status=?";

    /**
     * db request for selecting basic information about reservations filtering by date range
     */
    private static final String SHORT_INFO_ABOUT_RESERVATIONS_FILTERING_BY_DATE = "SELECT ID, date_request, date_from, date_to, id_Reservation_Status " +
            "FROM Reservation WHERE ((date_from >= STR_TO_DATE(?,'%Y-%m-%d') AND date_from <= STR_TO_DATE(?,'%Y-%m-%d')) " +
            "OR (date_to >= STR_TO_DATE(?,'%Y-%m-%d') AND date_to <= STR_TO_DATE(?,'%Y-%m-%d')))";

    /**
     * db request for selecting basic information about reservations filtering by date range and reservation's status
     */
    private static final String SHORT_INFO_ABOUT_ROOM_FILTERING_BY_DATE_AND_STATUS =
            SHORT_INFO_ABOUT_RESERVATIONS_FILTERING_BY_DATE + " AND id_Reservation_Status=?";

    /**
     * db request for selecting full information about reservation
     */
    private static final String FULL_INFO_REQUEST =
            "SELECT ID, date_request, date_from, date_to, id_Reservation_Status, id_User, comment, id_Hotel_Room " +
                    "FROM Reservation WHERE ID = ?";

    /**
     * db request for selecting all reservation's parameters
     */
    private static final String RESERVATION_REQUESTS_IDS = "SELECT id_parameter_values " +
            "FROM Request_Parameters WHERE id_reservation_request = ?";

    /**
     * db request for inserting new reservation
     */
    private static final String INSERT_NEW_RESERVATION = "INSERT INTO Reservation" +
            "(date_from, date_to, id_User, date_request, comment, id_Hotel_Room, id_reservation_status)" +
            "VALUES" +
            "(?,?,?,?,?,?,?)";

    /**
     * db request for updating target reservation's hotelRoom id and status
     */
    private static final String UPDATE_RESERVATION_HOTEL_ROOM_AND_STATUS = "UPDATE Reservation SET id_Hotel_Room = ?, " +
            "id_Reservation_Status = ? WHERE ID = ?";

    /**
     * db request for removing target reservation
     */
    private static final String DELETE_RESERVATION = "DELETE FROM Reservation WHERE ID = ?";

    /**
     * db request for inserting all reservation's parameters
     */
    private static final String insertRequestParameters = "INSERT INTO Request_Parameters " +
            "(id_Parameter_Values , id_reservation_request) VALUES ";

    /**
     * allocator, from which {@link Connection} will be gotten for processing operations with persistent storage
     */
    private ConnectionAllocator connectionAllocator;

    /**
     * {@inheritDoc}
     * <p>
     * Reads and maps all information about reservation to {@link Reservation}, also inits
     * {@link Reservation#requestParametersIds} via {@link #appendReservationParamIds(Reservation, ResultSet)} }
     * </p>
     *
     * @param id target object's id, which persistenced data will be mapped to object representation
     * @return {@link Reservation} object with all simple parameters inititalized and also with reservations' id
     * in {@link Reservation#id}
     * @throws DaoException {@inheritDoc}
     */
    @Override
    public Reservation read(int id) throws DaoException {
        Reservation reservation = null;
        Connection connection = connectionAllocator.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(FULL_INFO_REQUEST)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            reservation = buildReservation(resultSet);
            if (reservation == null) {
                return null;
            }

            PreparedStatement statement = connection.prepareStatement(RESERVATION_REQUESTS_IDS);
            statement.setInt(1, reservation.getId());
            ResultSet resultSet1 = statement.executeQuery();
            appendReservationParamIds(reservation, resultSet1);
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format("Exception was caused during the process of executing Sql " +
                    "requests of reading {0} object with Id = {1} and it parameters", Reservation.class.getName(), id));
        }
        return reservation;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Saves object main information (represented in {@link Reservation} non-agrigated params) via
     * {@link #saveReservationObject(Reservation, Connection)} and then saves all object reservation
     * parameters ({@link Reservation#getRequestParameters()} will be executed.
     * </p>
     *
     * @param object object that will be mapped to persistent storage data format
     * @return {@inheritDoc}
     * @throws DaoException {@inheritDoc}
     */
    @Override
    public void save(Reservation object) throws DaoException {
        try {
            Connection connection = connectionAllocator.getConnection();
            connection.setAutoCommit(false);

            saveReservationObject(object, connection);

            saveRequestParams(connection, object);

            connection.commit();

            //because the connection is shared, other Daos can't know,
            // that autocommit is disabled, so reset it to default HERE
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            throw new DaoException("Exception caused during the process of opening JDBC connection or " +
                    "rollback/commit operations while was " + Reservation.class.getName() + " object to DB");
        }
    }

    /**
     * maps {@link Reservation} object to relational representation via configuring
     * and executing jdbc statement. After execution initializes {@link Reservation#id}.
     *
     * @param reservation entity, that will be saved to db
     * @param connection  connection, through which select {@link #INSERT_NEW_RESERVATION} will be executed
     * @throws DaoException if exception was thrown during executing insert statement or if entity wasn't
     *                      inserted (there wasn't generated key)
     * @throws SQLException if exception was thrown during the rollback operation
     */
    private void saveReservationObject(Reservation reservation, Connection connection) throws DaoException, SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_RESERVATION, Statement.RETURN_GENERATED_KEYS)) {
            appendReservationParamsToStatement(preparedStatement, reservation);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            int newId = (generatedKeys.next()) ? generatedKeys.getInt(1) : -1;
            if (newId == -1) {
                throw new DaoException(MessageFormat.format("Exception caused because the {0} object wasn''t " +
                        "inserted to BD as a result of executing Sql query", Reservation.class.getName()));
            }
            reservation.setId(newId);
            generatedKeys.close();
        } catch (DaoException e1) {
            connection.rollback();
            throw e1;
        } catch (SQLException e) {
            connection.rollback();
            throw new DaoException("Exception was caused during the process of inserting " + Reservation.class.getName() + " object to BD");
        }
    }

    /**
     * Appends reservation's parameters to insert statement.
     *
     * @param preparedStatement statement, to which params will be inserted
     * @param object            object, from which params will be gotten
     * @throws DaoException if exception was thrown during the insertion parameters process
     */
    private void appendReservationParamsToStatement(PreparedStatement preparedStatement, Reservation object) throws DaoException {
        try {
            preparedStatement.setString(1, object.getDateFrom().toString());
            preparedStatement.setString(2, object.getDateTo().toString());
            preparedStatement.setInt(3, object.getUserId());
            preparedStatement.setString(4, object.getRequestDate().toString());
            preparedStatement.setString(5, object.getComment());
            preparedStatement.setNull(6, java.sql.Types.INTEGER);
            preparedStatement.setInt(7, object.getStatus().getId());

        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format("Exception was caused during the process of appending {0} " +
                    "object parameters to INSERT Sql statement", Reservation.class.getName()));
        }
    }


    private void saveRequestParams(Connection connection, Reservation reservation) throws SQLException, DaoException { //TODO:через batch!!!!
        String template = "(?," + reservation.getId() + ")";
        StringJoiner joiner = new StringJoiner(",", insertRequestParameters, "");
        for (int i = 0; i < reservation.getRequestParameters().size(); i++) {
            joiner.add(template);
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(joiner.toString())) {

            int rowCount = 0;
            for (ParameterValueTuple pv : reservation.getRequestParameters()) {
                preparedStatement.setInt(++rowCount, pv.getId());
            }

            int paramsSaved = preparedStatement.executeUpdate();

            if (paramsSaved < reservation.getRequestParameters().size()) {
                throw new DaoException("Exception caused because the quantity of inserted to BD reservation parameters " +
                        "is smaller than was declared in " + Reservation.class.getName() + " object with id = " + reservation.getId());
            }
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException(MessageFormat.format("Exception was caused while was inserting request parameters " +
                    "{0} in create new Reservation operation", reservation.getRequestParameters()), e);
        } catch (DaoException e) {
            connection.rollback();
            throw new DaoException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param object, which data will be mapped to persistent storage format
     * @return {@inheritDoc}
     * @throws DaoException {@inheritDoc}
     */
    @Override
    public boolean update(Reservation object) throws DaoException {
        Connection connection = connectionAllocator.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RESERVATION_HOTEL_ROOM_AND_STATUS)) {
            preparedStatement.setInt(1, object.getId());
            if (object.getHotelRoomId() == -1) {
                preparedStatement.setNull(2, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(2, object.getHotelRoomId());
            }
            preparedStatement.setInt(3, object.getStatus().getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format("Exception was caused during the updating process of {0} " +
                            "object''s hotel room and reservation status. HotelRoom id = {1}, reservation status id = {2}",
                    Reservation.class.getName(), object.getHotelRoomId(), object.getStatus().getId()));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If {@link ReservationStatus} is equal to {@link ReservationStatus#ALL} -
     * {@link #SHORT_INFO_ABOUT_RESERVATIONS_FILTERING_BY_DATE} will be executed,
     * otherwise - {@link #SHORT_INFO_ABOUT_ROOM_FILTERING_BY_DATE_AND_STATUS} will be executed
     * </p>
     *
     * @param roomID    target room's id
     * @param status    status of reservations
     * @param startDate date, from which reservations were requested
     * @param endDate   date, to which reservations were requested
     * @return {@inheritDoc}
     * @throws DaoException {@inheritDoc}
     */
    @Override
    public List<Reservation> getAllRoomReservationsInPeriod(int roomID, ReservationStatus status, LocalDate startDate, LocalDate endDate) throws DaoException {
        String query = (status == ReservationStatus.ALL)
                ? SHORT_INFO_ABOUT_RESERVATIONS_FILTERING_BY_DATE
                : SHORT_INFO_ABOUT_ROOM_FILTERING_BY_DATE_AND_STATUS;

        Connection connection = connectionAllocator.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            if (status != ReservationStatus.ALL) {
                preparedStatement.setInt(5, status.getId());
            }
            String startDateStr = startDate.toString();
            String endDateStr = endDate.toString();
            preparedStatement.setString(1, startDateStr);
            preparedStatement.setString(2, endDateStr);
            preparedStatement.setString(3, startDateStr);
            preparedStatement.setString(4, endDateStr);
            ResultSet res = preparedStatement.executeQuery();
            return buildReservationList(res);
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format("Exception was occurred during the process of executing data from DB of " +
                    "reservation list for room {0} in period from{1} to {2}", roomID, startDate, endDate));
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * If {@link ReservationStatus} is equal to {@link ReservationStatus#ALL} - {@link #GET_SHORT_INFO_BASE}
     * will be executed, otherwise - {@link #GET_SHORT_INFO_ABOUT_ALL_FILTERING_BY_STATUS} will be executed
     * </p>
     *
     * @param status status of reservations
     * @return {@inheritDoc}
     * @throws DaoException {@inheritDoc}
     */
    @Override
    public List<Reservation> getAllReservationsShortInfo(ReservationStatus status) throws DaoException {
        String req = (status == ReservationStatus.ALL) ? GET_SHORT_INFO_BASE : GET_SHORT_INFO_ABOUT_ALL_FILTERING_BY_STATUS;
        ResultSet resultSet = null;
        Connection connection = connectionAllocator.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            if (status != ReservationStatus.ALL) {
                preparedStatement.setInt(1, status.getId());
            }
            resultSet = preparedStatement.executeQuery();
            List<Reservation> reservations = buildReservationList(resultSet);
            resultSet.close();
            return reservations;
        } catch (SQLException e) {
            throw new DaoException("Exception was occurred while sql query was executing in operation of getting short " +
                    "information about all reservations", e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param id target id, whose connected data will be removed from storage
     * @return {@inheritDoc}
     * @throws DaoException {@inheritDoc}
     */
    @Override
    public boolean delete(int id) throws DaoException {
        Connection connection = connectionAllocator.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_RESERVATION)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Exception was occurred during the delete Reservation operation");
        }
    }

    //---------------------------------QUERIES FOR USER_ID----------------------------------------//

    /**
     * {@inheritDoc}
     * <p>
     * <p>
     * if {@link ReservationStatus} is {@link ReservationStatus#ALL},
     * then {@link #getAllReservationsForUser(int, Connection)} will be processed.
     * Otherwise {@link #getAllReservationsForUser(int, ReservationStatus, Connection)} will be
     * executed.
     * </p>
     *
     * @param userId {@inheritDoc}
     * @param status status of reservations
     * @return {@inheritDoc}
     * @throws DaoException {@inheritDoc}
     */
    @Override
    public List<Reservation> getAllUserReservationsShortInfo(int userId, ReservationStatus status) throws DaoException {
        Connection connection = connectionAllocator.getConnection();
        return (status == ReservationStatus.ALL)
                ? getAllReservationsForUser(userId, connection)
                : getAllReservationsForUser(userId, status, connection);
    }

    /**
     * Returns all reservations for specific user.
     *
     * @param userId     user's id, whose reservations will be returned
     * @param connection connection, through which select
     *                   {@link #SHORT_INFO_ABOUT_ALL_FOR_USER} will be executed
     * @return list of {@link Reservation}, (filtering by user's id and reservation status) or empty list
     * @throws DaoException if exception was thrown during the execution
     */
    private List<Reservation> getAllReservationsForUser(int userId, Connection connection) throws DaoException {
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SHORT_INFO_ABOUT_ALL_FOR_USER)) {
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            List<Reservation> reservations = buildReservationList(resultSet);
            resultSet.close();
            return reservations;
        } catch (SQLException e) {
            throw new DaoException("Exception was occurred while SQL statement was being executed in getAll operation", e);
        }
    }

    /**
     * Returns all reservations for specific user and with specific {@link ReservationStatus}
     *
     * @param userId     user's id, whose reservations will be returned
     * @param status     reservation's status, that all reservations will contain
     * @param connection connection, through which select
     *                   {@link #SHORT_INFO_ABOUT_ALL_FOR_USER_FILTERING_BY_USER_AND_STATUS} will be executed
     * @return list of {@link Reservation}, (filtering by user's id and reservation status) or empty list
     * @throws DaoException if exception was thrown during the execution
     */
    private List<Reservation> getAllReservationsForUser(int userId, ReservationStatus status, Connection connection) throws DaoException {
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(SHORT_INFO_ABOUT_ALL_FOR_USER_FILTERING_BY_USER_AND_STATUS)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, status.getId());
            resultSet = preparedStatement.executeQuery();

            List<Reservation> reservations = buildReservationList(resultSet);
            resultSet.close();
            return reservations;
        } catch (SQLException e) {
            throw new DaoException("Exception was occurred while SQL statement was being executed in getAll operation", e);
        }
    }

    /**
     * Builds list of {@link Reservation} via object-relational mapping process, getting data
     * from resultSet parameter (each row will be mapped to {@link Reservation} object). Will
     * return empty list, if there's no rows in resultSet.
     *
     * @param resultSet set, containing data for mapping to {@link Reservation}
     * @return list of {@link Reservation} or empty list
     * @throws DaoException if there was an exception during the object-relational mapping process
     */
    private List<Reservation> buildReservationList(ResultSet resultSet) throws DaoException {
        List<Reservation> reservationList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                reservationList.add(appendMainInfoToReservation(resultSet));
            }
            return reservationList;
        } catch (SQLException e) {
            throw new DaoException("Exception was occurred while next() method of " +
                    "ResultSet object was invoked during Reservation list building process", e);
        }
    }

    /**
     * Maps all information, connected to target reservation, from relational representation to object.
     * </p>
     *
     * @param resultSet set, containing data for mapping to {@link Reservation}
     * @return reservation object with all non-aggregated fields initialized
     * @throws DaoException if there was an exception during the object-relational mapping process
     */
    private Reservation buildReservation(ResultSet resultSet) throws DaoException {
        Reservation reservation = null;
        try {
            if (resultSet.next()) {
                reservation = appendMainInfoToReservation(resultSet);
                reservation.setUserId(resultSet.getInt(6));
                reservation.setComment(resultSet.getString(7));
                reservation.setHotelRoomId(resultSet.getInt(8));
            }
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format("Exception was caused during the process of building {0} object" +
                    " from ResultSet in read operation", Reservation.class.getName()), e);
        }
        return reservation;
    }

    /**
     * Maps main information about reservation (id, request's date, start/end date, status)
     * from relational representation to object.
     *
     * @param resultSet set, containing data for mapping to {@link Reservation}
     * @return reservation object with main fields initialized
     * @throws DaoException if there was an exception during the object-relational mapping process
     */
    private Reservation appendMainInfoToReservation(ResultSet resultSet) throws DaoException {
        try {
            Reservation reservation = new Reservation();
            reservation.setId(resultSet.getInt(1));
            reservation.setRequestDate(LocalDate.parse(resultSet.getString(2)));
            reservation.setDateFrom(LocalDate.parse(resultSet.getString(3)));
            reservation.setDateTo(LocalDate.parse(resultSet.getString(4)));
            reservation.setStatus(ReservationStatus.fromId(resultSet.getInt(5)));
            return reservation;
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format("Exception caused while main information was inserting to new " +
                    "{0} object from ResultSet object", Reservation.class.getName()));
        }
    }

    /**
     * Appends to target {@link Reservation} object all {@link ParameterValueTuple}'s id connected to it.
     *
     * @param reservation current {@link Reservation}, list of {@link ParameterValueTuple} will be inserted to
     * @param resultSet   set, containing list of {@link ParameterValueTuple}'s id.
     * @throws DaoException if there's no ids were read or exception was thrown during the process of
     *                      reading parameters from db
     */
    private void appendReservationParamIds(Reservation reservation, ResultSet resultSet) throws DaoException {
        try {
            List<Integer> requestIds = new ArrayList<>();
            while (resultSet.next()) {
                requestIds.add(resultSet.getInt(1));
            }
            if (requestIds.isEmpty()) {
                throw new DaoException(MessageFormat.format("Exception was caused because there is no reservation " +
                                "parameters were read during {0} object build process. Reservation Id = {1}",
                        Reservation.class.getName(), reservation.getId()));
            }
            reservation.setRequestParametersIds(requestIds);
        } catch (SQLException e) {
            throw new DaoException("Exception was caused during the process of reading reservation request parameters", e);
        }
    }

    public ConnectionAllocator getConnectionAllocator() {
        return connectionAllocator;
    }

    public void setConnectionAllocator(ConnectionAllocator connectionAllocator) {
        this.connectionAllocator = connectionAllocator;
    }
}