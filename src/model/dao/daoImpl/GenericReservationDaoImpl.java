package model.dao.daoImpl;

import model.dao.GenericReservationDao;
import model.entity.Reservation;
import model.entity.enums.ReservationStatus;
import model.entity.roomParameter.ParameterValue;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GenericReservationDaoImpl extends GenericReservationDao {

    private static final String shortInfoAboutAllForUser = "SELECT ID, date_request, id_Reservation_Status " +
            "FROM Reservation WHERE id_User = ?";

    private static final String shortInfoAboutAllForUserFilteringByStatus = "SELECT ID, date_request, id_Reservation_Status " +
            "FROM Reservation WHERE id_User=? AND id_Reservation_Status=?";

    private static final String shortInfoAboutAllFilteringByStatusAndDate = "SELECT ID, date_request, id_Reservation_Status " +
            "FROM Reservation WHERE id_Reservation_Status=? AND ((date_from >= STR_TO_DATE(?,'%Y-%m-%d') AND date_from <= STR_TO_DATE(?,'%Y-%m-%d')) " +
            "OR (date_to >= STR_TO_DATE(?,'%Y-%m-%d') AND date_to <= STR_TO_DATE(?,'%Y-%m-%d')))";

    private static final String fullInfoRequest = "SELECT ID, date_request, id_Reservation_Status, date_from, date_to, id_User, comment, id_Hotel_Room FROM " +
            "FROM Reservation WHERE ID = ?";

    private static final String reservationRequestsIds = "SELECT id_parameter_values " +
            "from Request_Parameters WHERE id_reservation_request = ?";

    private static final String insertNewReservation = "INSERT INTO Reservation" +
            "(date_from, date_to, id_User, date_request, comment, id_Hotel_Room, id_reservation_status)" +
            "VALUES" +
            "(?,?,?,?,?,?,?)";

    private static final String insertRequestParameters = "INSERT INTO Request_Parameters " +
            "(id_reservation_request, id_Parameter_Values) VALUES ";


    private DataSource dataSource;


    @Override
    public Reservation read(Integer id) {
        Reservation reservation = null;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(fullInfoRequest);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                reservation = new Reservation();
                appendMainInfoToReservation(reservation, resultSet);
                reservation.setDateFrom(LocalDate.parse(resultSet.getString(4)));
                reservation.setDateTo(LocalDate.parse(resultSet.getString(5)));
                reservation.setUserID(resultSet.getInt(6));
                reservation.setComment(resultSet.getString(7));
                reservation.setHotelRoomID(resultSet.getInt(8));
            }
            if (reservation == null)
                throw new RuntimeException(); //TODO:!!!!


            PreparedStatement statement = connection.prepareStatement(reservationRequestsIds);
            statement.setInt(1, reservation.getId());
            ResultSet set = statement.getResultSet();
            List<Integer> requestIds = new ArrayList<>();
            while (set.next()) {
                requestIds.add(set.getInt(1));
            }
            reservation.setRequestParametersIds(requestIds);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }

    @Override
    public Integer save(Reservation object) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(insertNewReservation, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, object.getDateFrom().toString());
            preparedStatement.setString(2, object.getDateTo().toString());
            preparedStatement.setInt(3, object.getUserID());
            preparedStatement.setString(4, object.getRequestDate().toString());
            preparedStatement.setString(5, object.getComment());
            preparedStatement.setInt(6, object.getHotelRoomID());
            preparedStatement.setInt(7, object.getStatus().getId());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            saveRequestParams(connection, object);
            return generatedKeys.next() ? generatedKeys.getInt(1) : -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private int saveRequestParams(Connection connection, Reservation reservation) throws SQLException {
        String template = "(?," + reservation.getId() + ")";
        StringJoiner joiner = new StringJoiner(",", insertRequestParameters, "");
        for (int i = 0; i < reservation.getRequestParameters().size(); i++) {
            joiner.add(template);
        }
        PreparedStatement preparedStatement = connection.prepareStatement(joiner.toString());

        int rowCount = 0;
        for (ParameterValue pv : reservation.getRequestParameters()) {
            preparedStatement.setInt(rowCount++, pv.getId());
        }

        int insertedRows = preparedStatement.executeUpdate();
        return insertedRows; //TODO: exception if caused!!! TRANSACTION!!!!!!!!
    }

    @Override
    public boolean update(Reservation object) {
        String query = "Update Reservation SET id_Hotel_Room = ?, id_Reservation_Status = ? WHERE ID = " + object.getId();
        boolean wasUpdate = false;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(object.getHotelRoom().getRoomID(), object.getStatus().getId());
            wasUpdate = preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wasUpdate;
    }

    @Override
    public List<Reservation> getAllRoomReservationsInPeriod(int roomID, ReservationStatus status, LocalDate startDate, LocalDate endDate) {
        return null; //TODO!!!!!!
    }

    @Override
    public List<Reservation> getAllReservationsShortInfoInPeriod(ReservationStatus status, LocalDate startDate, LocalDate endDate) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(shortInfoAboutAllFilteringByStatusAndDate);
            String startDateStr = startDate.toString();
            String endDateStr = endDate.toString();
            preparedStatement.setInt(1, status.getId());
            preparedStatement.setString(2, startDateStr);
            preparedStatement.setString(3, endDateStr);
            preparedStatement.setString(4, startDateStr);
            preparedStatement.setString(5, endDateStr);
            ResultSet res = preparedStatement.executeQuery();
            return buildReservationList(res); //TODO: exception handling.
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


    //---------------------------------QUERIES FOR USER_ID----------------------------------------//
    @Override
    public List<Reservation> getAllUserReservationsShortInfo(int userId, ReservationStatus status) {
        try (Connection connection = dataSource.getConnection()) {
            return (status == ReservationStatus.ALL)
                    ? getAllReservationsForSpecUser(userId, connection)
                    : getAllReservationsForSpecUser(userId, status, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Reservation> getAllReservationsForSpecUser(int userId, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(shortInfoAboutAllForUser);
        preparedStatement.setInt(1, userId);
        return buildReservationList(preparedStatement.executeQuery());
    }

    private List<Reservation> getAllReservationsForSpecUser(int userId, ReservationStatus status, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(shortInfoAboutAllForUserFilteringByStatus);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, status.getId());
        return buildReservationList(preparedStatement.executeQuery());
    }
    //---------------------------------QUERIES FOR USER_ID----------------------------------------//


    private List<Reservation> buildReservationList(ResultSet resultSet) throws SQLException {
        List<Reservation> reservationList = new ArrayList<>();

        while (resultSet.next()) {
            reservationList.add(appendMainInfoToReservation(new Reservation(), resultSet));
        }
        return reservationList;
    }

    private Reservation appendMainInfoToReservation(Reservation reservation, ResultSet resultSet) throws SQLException {
        reservation.setId(resultSet.getInt(1));
        reservation.setRequestDate(LocalDate.parse(resultSet.getString(2)));
        reservation.setStatus(ReservationStatus.fromId(resultSet.getInt(3)));
        return reservation;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
