package model.dao.daoImpl;

import model.dao.GenericReservationDao;
import model.entity.Reservation;
import model.entity.enums.ReservationStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GenericReservationDaoImpl extends GenericReservationDao {


    private static final String mainInfoAboutAll =
            "SELECT ID, date_request, id_Reservation_Status" +
                    "FROM Reservation " +
                    "WHERE id_User = ?";

    private static final String shortInfoAboutAllFilteringByStatus =
            mainInfoAboutAll + " AND id_Reservation_status = ?";


    private static final String changeSubmittedOrRefusedStatus =
            "UPDATE Reservation " +
                    "SET id_Reservation_Status = ?";

    private static final String changeAnsweredOrProcessingStatus =
            changeSubmittedOrRefusedStatus + ",id_Hotel_Room=?";

    private static final String fullInfoRequest =
            "SELECT ID, date_request, id_Reservation_Status, date_from, date_to, id_User, comment, id_Hotel_Room FROM " +
                    "FROM Reservation WHERE ID = ?";

    private static final String insertNewReservation = "INSERT INTO Reservation" +
            "(date_from, date_to, id_User, date_request, comment, id_Hotel_Room, id_reservation_status)" +
            "VALUES" +
            "(?,?,?,?,?,?,?)";


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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }

    @Override
    public List<Reservation> getAllRoomSubmittedReservationsInPeriod(int roomID, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public List<Reservation> getAllUserReservationsShortInfo(int userId, ReservationStatus status) {
        try (Connection connection = dataSource.getConnection()) {
            return (status == ReservationStatus.ALL)
                    ? getAllReservations(userId, connection)
                    : getFilteredReservations(userId, status, connection);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Reservation> getAllReservations(int userId, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(mainInfoAboutAll);
        preparedStatement.setInt(1, userId);
        return buildReservationList(preparedStatement.executeQuery());
    }

    private List<Reservation> getFilteredReservations(int userId, ReservationStatus status, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(shortInfoAboutAllFilteringByStatus);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, status.getId());
        return buildReservationList(preparedStatement.executeQuery());
    }

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

    @Override
    public Integer save(Reservation object) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(insertNewReservation, Statement.RETURN_GENERATED_KEYS);
//            preparedStatement.setInt(1, object.getId());
            preparedStatement.setString(1, object.getDateFrom().toString());
            preparedStatement.setString(2, object.getDateTo().toString());
            preparedStatement.setInt(3, object.getUserID());
            preparedStatement.setString(4, object.getRequestDate().toString());
            preparedStatement.setString(5, object.getComment());
            preparedStatement.setInt(6, object.getHotelRoomID());
            preparedStatement.setInt(7, object.getStatus().getId());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            return generatedKeys.next() ? generatedKeys.getInt(1) : -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
