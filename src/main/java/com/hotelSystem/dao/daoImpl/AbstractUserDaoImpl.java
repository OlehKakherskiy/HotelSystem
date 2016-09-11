package main.java.com.hotelSystem.dao.daoImpl;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.dao.AbstractMobilePhoneDao;
import main.java.com.hotelSystem.dao.AbstractUserDao;
import main.java.com.hotelSystem.exception.DaoException;
import main.java.com.hotelSystem.manager.AbstractDaoManager;
import main.java.com.hotelSystem.manager.managerImpl.daoManagerImpl.ConnectionAllocator;
import main.java.com.hotelSystem.model.MobilePhone;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.model.enums.UserType;

import javax.sql.DataSource;
import java.sql.*;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Class represents implementation of {@link AbstractUserDao} for relational databases, represented
 * via {@link DataSource}. It uses {@link ConnectionAllocator} for getting connection from datasource.
 * Restriction: do NOT close allocated connection.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see DataSource
 * @see ConnectionAllocator
 */
public class AbstractUserDaoImpl implements AbstractUserDao {

    private static final String USER_OBJECT_BUILD_PROCESS_EXCEPTION = "Exception was occurred while User object was being " +
            "built from ResultSet object";

    private static final String REQUEST_EXEC_EXCEPTION = "Exception was occurred while was attempt " +
            "to execute request for reading User object";

    /**
     * db select for gettin user info using user id
     */
    private static final String GET_USER_FROM_ID =
            "SELECT idUser, name, last_name, idUserType, " +
                    "FROM USER " +
                    "WHERE idUser=?";

    /**
     * db request for getting user info using signIn and password
     */
    private static final String GET_USER_FROM_LOGIN_AND_PASSWORD =
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
     * allocator, from which {@link Connection} will be gotten for processing operations with persistent storage
     */
    private ConnectionAllocator connectionAllocator;

    /**
     * {@inheritDoc}
     * <p>
     * Inits statement {@link #GET_USER_FROM_ID}, sets param to it and exectutes it. While {@link ResultSet} is
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
        Connection connection = connectionAllocator.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(GET_USER_FROM_ID)) {

            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            return buildUserObject(resultSet);
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format("{0} using user id", REQUEST_EXEC_EXCEPTION), e);
        }
    }

    @Override
    public void save(User object) throws DaoException {
        AbstractDaoManager daoManager = (AbstractDaoManager) GlobalContext.getValue(GlobalContextConstant.DAO_MANAGER);
        AbstractMobilePhoneDao mobilePhoneDao = daoManager.getInstance(AbstractMobilePhoneDao.class);

        Connection connection = connectionAllocator.allocateConnection();
        try {
            connection.setAutoCommit(false);
            String query = "INSERT INTO USER (name, last_name, login, password, idUserType) VALUES (?,?,?,?,?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(1, object.getName());
                preparedStatement.setString(2, object.getLastName());
                preparedStatement.setString(3, object.getLogin());
                preparedStatement.setString(4, object.getPassword());
                preparedStatement.setInt(5, object.getUserType().getId());
                preparedStatement.executeUpdate();
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    object.setIdUser(generatedKeys.getInt(1));
                } else {
                    throw new DaoException("Exception was thrown during the process of user registration");
                }

                for (MobilePhone mobilePhone : object.getMobilePhoneList()) {
                    mobilePhone.setUserId(object.getIdUser());
                    mobilePhoneDao.save(mobilePhone);
                }
                generatedKeys.close();
            } catch (DaoException e) {
                connection.rollback();
                throw e;
            } catch (SQLException e1) {
                connection.rollback();
                throw e1;
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public boolean update(User object) throws DaoException {
        Connection connection = connectionAllocator.allocateConnection();
        String updateUser = "UPDATE User SET idUserType=?, name=?, last_name=? WHERE idUser=?";
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateUser)) {
                preparedStatement.setInt(4, object.getIdUser());
                preparedStatement.setInt(1, object.getUserType().getId());
                preparedStatement.setString(2, object.getName());
                preparedStatement.setString(3, object.getLastName());
                if (preparedStatement.executeUpdate() == 0) {
                    connection.rollback();
                    return false;
                }
                updateMobilePhoneList(object.getMobilePhoneList(), connection);
                connection.commit();
                connection.setAutoCommit(true);
                return true;
            } catch (SQLException e1) {
                connection.rollback();
                throw new DaoException(MessageFormat.format("Exception was thrown during updating User''s object with params: {0}", object.toString()), e1);
            }
        } catch (SQLException e) {
            throw new DaoException("Exception was thrown during setting settin autocommit or rollbacking.");
        }
    }

    private void updateMobilePhoneList(List<MobilePhone> mobilePhoneList, Connection connection) throws SQLException {
        String query = "UPDATE Mobile_Phone SET phone_number=? WHERE idMobilePhone=?";
        try (PreparedStatement updateStatement = connection.prepareStatement(query)) {
            for (MobilePhone mobilePhone : mobilePhoneList) {
                updateStatement.setString(1, mobilePhone.getMobilePhone());
                updateStatement.setInt(2, mobilePhone.getIdMobilePhone());
                if (updateStatement.executeUpdate() == 0)
                    throw new SQLException();
            }
        } catch (SQLException e) {
            throw new SQLException(MessageFormat.format("Exception was cauesd during the process of updating list of mobile phones.List:{0}",
                    Arrays.toString(mobilePhoneList.toArray())), e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Inits statement {@link #GET_USER_FROM_LOGIN_AND_PASSWORD}, sets params to it and exectutes.
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
        Connection connection = connectionAllocator.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(GET_USER_FROM_LOGIN_AND_PASSWORD)) {

            statement.setString(1, login);
            statement.setString(2, password);
            resultSet = statement.executeQuery();
            return buildUserObject(resultSet);
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format("{0} using signIn and password", REQUEST_EXEC_EXCEPTION), e);
        }
    }

    @Override
    public boolean isValidLogin(String login) throws DaoException {
        Connection connection = connectionAllocator.getConnection();
        String query = "SELECT COUNT(*) FROM User WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public void updatePassword(String login, String password) throws DaoException {
        String query = "UPDATE User SET password = ? WHERE login = ?";
        Connection connection = connectionAllocator.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, login);
            if (preparedStatement.executeUpdate() == 0) {
                throw new DaoException();
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage(), e);
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
            throw new DaoException(USER_OBJECT_BUILD_PROCESS_EXCEPTION, e);
        }
    }

    public ConnectionAllocator getConnectionAllocator() {
        return connectionAllocator;
    }

    public void setConnectionAllocator(ConnectionAllocator connectionAllocator) {
        this.connectionAllocator = connectionAllocator;
    }
}
