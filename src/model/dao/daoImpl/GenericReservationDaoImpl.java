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


    private static final String shortInfoAboutAll =
            "SELECT ID, date_from, date_to, date_request, id_Reservation_Status, id_Hotel_Room, id_User" +
                    "FROM Reservation " +
                    "WHERE id_User = ?";

    private static final String shortInfoAboutAllFilteringByStatus =
            shortInfoAboutAll + " AND id_Reservation_status = ?";


    private static final String changeSubmittedOrRefusedStatus =
            "UPDATE Reservation " +
                    "SET id_Reservation_Status = ?";

    private static final String changeAnsweredOrProcessingStatus =
            changeSubmittedOrRefusedStatus + ",id_Hotel_Room=?";


    private DataSource dataSource;


    @Override
    public Reservation read(Integer id) {
        return super.read(id); //TODO:!!!!реализация
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
        PreparedStatement preparedStatement = connection.prepareStatement(shortInfoAboutAll);
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
            Reservation reservation = new Reservation();
            reservation.setId(resultSet.getInt(1));
            reservation.setDateFrom(LocalDate.parse(resultSet.getString(2)));
            reservation.setDateTo(LocalDate.parse(resultSet.getString(3)));
            reservation.setDateFrom(LocalDate.parse(resultSet.getString(4)));
            reservation.setStatus(ReservationStatus.fromId(resultSet.getInt(5)));
            reservation.setHotelRoomID(resultSet.getInt(6));
            reservation.setUserID(resultSet.getInt(7));
            reservationList.add(reservation);
        }
        return reservationList;
    }

    @Override
    public void changeReservationStatus(Reservation reservation) { //TODO: для уменьшения размера кода просто update делать и всё
        try (Connection connection = dataSource.getConnection()) {

            ReservationStatus newStatus = reservation.getStatus();
            PreparedStatement preparedStatement =
                    (newStatus == ReservationStatus.ANSWERED || newStatus == ReservationStatus.PROCESSING)
                    ? updateProcessingOrAnsweredReservationStatus(reservation, connection)
                    : updateSubmittedOrRefusedReservationStatus(reservation, connection);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated < 1) {
                throw new RuntimeException(); //TODO: не было обновления - ошибка!!
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Reservation save(Reservation object) {
        return super.save(object);
    }


    private PreparedStatement updateProcessingOrAnsweredReservationStatus(Reservation reservation, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(changeAnsweredOrProcessingStatus);
        preparedStatement.setInt(1, reservation.getStatus().getId());
        if (reservation.getHotelRoom() == null) {
            preparedStatement.setNull(2, Types.INTEGER);
        } else {
            preparedStatement.setInt(2, reservation.getHotelRoom().getRoomID());
        }
        return preparedStatement;
    }

    private PreparedStatement updateSubmittedOrRefusedReservationStatus(Reservation reservation, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(changeSubmittedOrRefusedStatus);
        preparedStatement.setInt(1, reservation.getStatus().getId());
        return preparedStatement;
    }


    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
