package main.java.com.epam.project4.model.dao.daoImpl;

import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.model.dao.AbstractReservationDao;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.roomParameter.ParameterValue;

import javax.sql.DataSource;
import java.sql.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class AbstractReservationDaoImpl extends AbstractReservationDao {

    //--------------------------------- SQL REQUESTS ----------------------------------------//
    private static final String getShortInfoBase = "SELECT ID, date_request, date_from, date_to, id_Reservation_Status " +
            "FROM Reservation ";

    private static final String shortInfoAboutAllForUser = getShortInfoBase + " WHERE id_User = ?";

    private static final String getShortInfoAboutAllFilteringByStatus = getShortInfoBase + " WHERE id_Reservation_Status=?";

    private static final String shortInfoAboutAllForUserFilteringByUserAndStatus = shortInfoAboutAllForUser + " AND id_Reservation_Status=?";

    private static final String shortInfoAboutRoomFilteringByDate = "SELECT ID, date_request, date_from, date_to, id_Reservation_Status " +
            "FROM Reservation WHERE ((date_from >= STR_TO_DATE(?,'%Y-%m-%d') AND date_from <= STR_TO_DATE(?,'%Y-%m-%d')) " +
            "OR (date_to >= STR_TO_DATE(?,'%Y-%m-%d') AND date_to <= STR_TO_DATE(?,'%Y-%m-%d')))";

    private static final String shortInfoAboutRoomFilteringByDateAndStatus = shortInfoAboutRoomFilteringByDate + " AND id_Reservation_Status=?";

    private static final String fullInfoRequest = "SELECT ID, date_request, date_from, date_to, id_Reservation_Status, id_User, comment, id_Hotel_Room " +
            "FROM Reservation WHERE ID = ?";

    private static final String reservationRequestsIds = "SELECT id_parameter_values " +
            "FROM Request_Parameters WHERE id_reservation_request = ?";

    private static final String insertNewReservation = "INSERT INTO Reservation" +
            "(date_from, date_to, id_User, date_request, comment, id_Hotel_Room, id_reservation_status)" +
            "VALUES" +
            "(?,?,?,?,?,?,?)";

    private static final String insertRequestParameters = "INSERT INTO Request_Parameters " +
            "(id_Parameter_Values , id_reservation_request) VALUES ";


    private DataSource dataSource;


    @Override
    public Reservation read(int id) throws DaoException {
        Reservation reservation = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(fullInfoRequest)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            reservation = buildReservation(resultSet);
            if (reservation == null) {
                return null;
            }

            PreparedStatement statement = connection.prepareStatement(reservationRequestsIds);
            statement.setInt(1, reservation.getId());
            ResultSet resultSet1 = statement.executeQuery();
            appendReservationParamIds(reservation, resultSet1);
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format("Exception was caused during the process of executing Sql " +
                    "requests of reading {0} object with Id = {1} and it parameters", Reservation.class.getName(), id));
        }
        return reservation;
    }

    private Reservation buildReservation(ResultSet resultSet) throws DaoException {
        Reservation reservation = null;
        try {
            if (resultSet.next()) {
                reservation = new Reservation();
                appendMainInfoToReservation(reservation, resultSet);
                reservation.setUserID(resultSet.getInt(6));
                reservation.setComment(resultSet.getString(7));
                reservation.setHotelRoomID(resultSet.getInt(8));
            }
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format("Exception was caused during the process of building {0} object" +
                    " from ResultSet in read operation", Reservation.class.getName()), e);
        }
        return reservation;
    }

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

    @Override
    public int save(Reservation object) throws DaoException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            saveReservationObject(object, connection);

            saveRequestParams(connection, object);

            connection.commit();
            return object.getId();


        } catch (SQLException e) {
            throw new DaoException("Exception caused during the process of opening JDBC connection or " +
                    "rollback/commit operations while was " + Reservation.class.getName() + " object to DB");
        }
    }

    private void saveReservationObject(Reservation reservation, Connection connection) throws DaoException, SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertNewReservation, Statement.RETURN_GENERATED_KEYS)) {
            appendReservationParamsToStatement(preparedStatement, reservation);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            int newId = (generatedKeys.next()) ? generatedKeys.getInt(1) : -1;
            if (newId == -1) {
                throw new DaoException(MessageFormat.format("Exception caused because the {0} object wasn\\'t " +
                        "inserted to BD as a result of executing Sql query", Reservation.class.getName()));
            }
            reservation.setId(newId);
        } catch (SQLException e) {
            connection.rollback();
            throw new DaoException("Exception was caused during the process of inserting " + Reservation.class.getName() + " object to BD");
        } catch (DaoException e1) {
            connection.rollback();
            throw new DaoException(e1.getMessage(), e1);
        }
    }

    private void appendReservationParamsToStatement(PreparedStatement preparedStatement, Reservation object) throws DaoException {
        try {
            preparedStatement.setString(1, object.getDateFrom().toString());
            preparedStatement.setString(2, object.getDateTo().toString());
            preparedStatement.setInt(3, object.getUserID());
            preparedStatement.setString(4, object.getRequestDate().toString());
            preparedStatement.setString(5, object.getComment());
            preparedStatement.setNull(6, java.sql.Types.INTEGER);
            preparedStatement.setInt(7, object.getStatus().getId());

        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format("Exception was caused during the process of appending {0} " +
                    "object parameters to INSERT Sql statement", Reservation.class.getName()));
        }
    }

    private void saveRequestParams(Connection connection, Reservation reservation) throws SQLException, DaoException {
        String template = "(?," + reservation.getId() + ")";
        StringJoiner joiner = new StringJoiner(",", insertRequestParameters, "");
        for (int i = 0; i < reservation.getRequestParameters().size(); i++) {
            joiner.add(template);
        }
        System.out.println("method saveRequestParams query: " + joiner.toString());
        try (PreparedStatement preparedStatement = connection.prepareStatement(joiner.toString())) {

            int rowCount = 0;
            for (ParameterValue pv : reservation.getRequestParameters()) {
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

    @Override
    public boolean update(Reservation object) throws DaoException {
        String query = "Update Reservation SET id_Hotel_Room = ?, id_Reservation_Status = ? WHERE ID = " + object.getId();
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            if (object.getHotelRoomID() == -1) {
                preparedStatement.setNull(1, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(1, object.getHotelRoomID());
            }
            preparedStatement.setInt(2, object.getStatus().getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format("Exception was caused during the updating process of {0} " +
                            "object\\'s hotel room and reservation status. HotelRoom id = {1}, reservation status id = {2}",
                    Reservation.class.getName(), object.getHotelRoomID(), object.getStatus().getId()));
        }
    }

    @Override
    public List<Reservation> getAllRoomReservationsInPeriod(int roomID, ReservationStatus status, LocalDate startDate, LocalDate endDate) throws DaoException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement;
            if (status == ReservationStatus.ALL) {
                preparedStatement = connection.prepareStatement(shortInfoAboutRoomFilteringByDate);
            } else {
                preparedStatement = connection.prepareStatement(shortInfoAboutRoomFilteringByDateAndStatus);
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

    @Override
    public List<Reservation> getAllReservationsShortInfo(ReservationStatus status) throws DaoException {
        String req = (status == ReservationStatus.ALL) ? getShortInfoBase : getShortInfoAboutAllFilteringByStatus;
        ResultSet resultSet = null;
        try (Connection c = dataSource.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement(req)) {
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

    @Override
    public boolean delete(int id) throws DaoException {
        boolean isSuccessfully = false;
        String query = "DELETE FROM Reservation WHERE ID = ?";
        try (Connection c = dataSource.getConnection();
             PreparedStatement preparedStatement = c.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Exception was occurred during the delete Reservation operation");
        }
    }

    //---------------------------------QUERIES FOR USER_ID----------------------------------------//
    @Override
    public List<Reservation> getAllUserReservationsShortInfo(int userId, ReservationStatus status) throws DaoException {
        try (Connection connection = dataSource.getConnection()) {
            return (status == ReservationStatus.ALL)
                    ? getAllReservationsForSpecUser(userId, connection)
                    : getAllReservationsForSpecUser(userId, status, connection);
        } catch (SQLException e) {
            throw new DaoException("Exception was occurred while there was an attempt to get a connection from datasource");
        }
    }

    private List<Reservation> getAllReservationsForSpecUser(int userId, Connection connection) throws DaoException {
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(shortInfoAboutAllForUser)) {
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();

            List<Reservation> reservations = buildReservationList(resultSet);
            resultSet.close();
            return reservations;
        } catch (SQLException e) {
            throw new DaoException("Exception was occurred while SQL statement was being executed in getAll operation", e);
        }
    }

    private List<Reservation> getAllReservationsForSpecUser(int userId, ReservationStatus status, Connection connection) throws DaoException {
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(shortInfoAboutAllForUserFilteringByUserAndStatus)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, status.getId());
            resultSet = preparedStatement.executeQuery();

            List<Reservation> reservations = buildReservationList(resultSet);
            resultSet.close();
            return reservations;
        } catch (SQLException e) {
            throw new DaoException("Exception was occurred while SQL statement was being executed in getAll operation", e); //TODO: вынести
        }
    }
    //---------------------------------QUERIES FOR USER_ID----------------------------------------//

    private List<Reservation> buildReservationList(ResultSet resultSet) throws DaoException {
        List<Reservation> reservationList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                reservationList.add(appendMainInfoToReservation(new Reservation(), resultSet));
            }
            return reservationList;
        } catch (SQLException e) {
            throw new DaoException("Exception was occurred while next() method of " +
                    "ResultSet object was invoked during Reservation list building process", e);
        }
    }

    private Reservation appendMainInfoToReservation(Reservation reservation, ResultSet resultSet) throws DaoException {
        try {
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

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}