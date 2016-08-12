package main.java.com.hotelSystem.dao.daoImpl;

import main.java.com.hotelSystem.dao.AbstractUserDao;
import main.java.com.hotelSystem.exception.DaoException;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.model.enums.UserType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

/**
 * Class represents implementation of {@link AbstractUserDao} for relational databases, represented
 * via {@link DataSource}.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see DataSource
 */
public class AbstractUserDaoImpl implements AbstractUserDao {

    private static final String userObjectBuildProcessException = "Exception was occurred while User object was being " +
            "built from ResultSet object";

    private static final String requestExecException = "Exception was occurred while was attempt " +
            "to execute request for reading User object";

    /**
     * db select for gettin user info using user id
     */
    private static final String getUserFromID =
            "SELECT idUser, name, last_name, idUserType, " +
                    "FROM USER " +
                    "WHERE idUser=?";

    /**
     * db request for getting user info using signIn and password
     */
    private static final String getUserFromLoginAndPassword =
            "SELECT idUser, name, last_name, idUserType " +
                    "FROM USER " +
                    "WHERE login=? AND password=?";


    /**
     * if request for getting user info was processed successfully, but there's no user with inputted params,
     * so {@link User#idUser} = -1
     */
    private static final User noUserStub = new User();

    static {
        noUserStub.setIdUser(-1);
    }

    /**
     * datasource, from wich {@link Connection} will be get for processing operations with persistent storage
     */
    private DataSource dataSource;

    /**
     * {@inheritDoc}
     * <p>
     * Inits statement {@link #getUserFromID}, sets param to it and exectutes it. While {@link ResultSet} is
     * get, calls {@link #buildUserObject(ResultSet)}
     * </p>
     *
     * @param id target object's id, which persistenced data will be mapped to object representation
     * @return {@inheritDoc}
     * @throws DaoException {@inheritDoc}
     */
    @Override
    public User read(int id) throws DaoException {
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

    /**
     * {@inheritDoc}
     * <p>
     * Inits statement {@link #getUserFromLoginAndPassword}, sets params to it and exectutes.
     * After ResultSet is get, calls {@link #buildUserObject(ResultSet)}
     * </p>
     *
     * @param login    user's signIn
     * @param password user's password
     * @return {@inheritDoc}
     * @throws DaoException {@inheritDoc}
     */
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
            throw new DaoException(MessageFormat.format("{0} using signIn and password", requestExecException), e);
        }
    }

    /**
     * Builds {@link User} object from ResultSet, inits such fields, as: {@link User#idUser},
     * {@link User#name}, {@link User#lastName}, {@link User#userType}. If ResultSet's
     * {@link ResultSet#next()} is false, returns {@link #noUserStub}
     *
     * @param resultSet set with {@link User}'s data
     * @return {@link User} representation of sql represented data. If there's no data, returned,
     * returns {@link #noUserStub}
     * @throws DaoException if exception was caused during the process of mapping data from sql format
     *                      to object representation
     */
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
