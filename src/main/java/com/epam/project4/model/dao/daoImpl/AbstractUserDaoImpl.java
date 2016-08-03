package main.java.com.epam.project4.model.dao.daoImpl;

import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.model.dao.AbstractUserDao;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.UserType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class AbstractUserDaoImpl extends AbstractUserDao {

    private static final String getUserFromID =
            "SELECT idUser, name, last_name, idUserType, " +
                    "FROM USER " +
                    "WHERE idUser=?";

    private static final String getUserFromLoginAndPassword =
            "SELECT idUser, name, last_name, idUserType " +
                    "FROM USER " +
                    "WHERE login=? AND password=?";

    private static final String userObjectBuildProcessException = "Exception was occurred while User object was being " +
            "built from ResultSet object";

    private static final String requestExecException = "Exception was occurred while was attempt " +
            "to execute request for reading User object";


    private static final User noUserStub = new User();

    static {
        noUserStub.setIdUser(-1);
    }

    private DataSource dataSource;

    @Override
    public User read(Integer id) throws DaoException {
        ResultSet resultSet = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUserFromID)) {

            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            return buildUserObject(resultSet);
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format("{0} using user id", requestExecException), e);
        }
    }

    @Override
    public User tryLogin(String login, String password) throws DaoException {
        ResultSet resultSet = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUserFromLoginAndPassword)) {

            statement.setString(1, login);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            return buildUserObject(resultSet);
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format("{0} using login and password", requestExecException), e);
        }
    }

    private User buildUserObject(ResultSet resultSet) throws DaoException {
        User user = null;
        try {
            if (resultSet.next()) {
                user = new User();
                user.setIdUser(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setLastName(resultSet.getString(3));
                user.setUserType(UserType.fromID(resultSet.getInt(4)));
                resultSet.close();
            }
            return user != null ? user : noUserStub;
        } catch (SQLException e) {
            throw new DaoException(userObjectBuildProcessException, e);
        }
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
