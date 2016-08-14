package main.java.com.hotelSystem.dao.daoImpl;

import main.java.com.hotelSystem.dao.AbstractMobilePhoneDao;
import main.java.com.hotelSystem.exception.DaoException;
import main.java.com.hotelSystem.manager.managerImpl.daoManagerImpl.ConnectionAllocator;
import main.java.com.hotelSystem.model.MobilePhone;
import main.java.com.hotelSystem.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class represents implementation of {@link AbstractMobilePhoneDao} for relational databases, represented
 * via {@link DataSource}. It uses {@link ConnectionAllocator} for getting connection from datasource.
 * Restriction: do NOT close allocated connection.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see DataSource
 * @see ConnectionAllocator
 */
public class AbstractMobilePhoneDaoImpl implements AbstractMobilePhoneDao {

    private static final String SQL_REQUEST_EXCEPTION = "Exception was occurred while was executing sql request " +
            "for getting mobilePhoneList";

    private static final String BUILD_MOBILE_PHONE_EXCEPTION = "Exception was occurred while was building operation of " +
            "MobilePhone entity from ResultSet";

    private static final String GET_NEXT_DATA_FOR_MOBILE_PHONE_BUILDING_EXCEPTION = "Exception was occurred because next() " +
            "method was called from resultSet object while MobilePhone list was being built";

    /**
     * db select for gettin all mobile phones of specific user
     */
    private static final String GET_MOBILE_PHONE_LIST = "SELECT idMobilePhone, phone_number " +
            "FROM MOBILE_PHONE " +
            "WHERE id_User=?";

    /**
     * allocator, from which {@link Connection} will be gotten for processing operations with persistent storage
     */
    private ConnectionAllocator connectionAllocator;

    /**
     * {@inheritDoc}
     * <p>
     * <p>
     * Inits statement {@link #GET_MOBILE_PHONE_LIST}, sets param to it and exectutes it. While {@link ResultSet} is
     * get, calls {@link #buildList(ResultSet)}
     * </p>
     *
     * @param userId id of target {@link User} object
     * @return {@inheritDoc}
     * @throws DaoException {@inheritDoc}
     */
    @Override
    public List<MobilePhone> getAll(int userId) throws DaoException {
        ResultSet resultSet = null;
        Connection connection = connectionAllocator.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(GET_MOBILE_PHONE_LIST)) {
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();
            return buildList(resultSet);
        } catch (SQLException e) {
            throw new DaoException(SQL_REQUEST_EXCEPTION, e);
        }
    }

    /**
     * Builds list of {@link MobilePhone} from data, stored in {@link ResultSet}
     *
     * @param resultSet set with {@link MobilePhone}'s data
     * @return list of specific user's mobile phones
     * @throws DaoException if exception was thrown because of {@link ResultSet#next()} or
     *                      {@link #buildMobilePhone(ResultSet)} methods
     */
    private List<MobilePhone> buildList(ResultSet resultSet) throws DaoException {
        try {
            List<MobilePhone> resultList = new ArrayList<>();
            while (resultSet.next()) {
                resultList.add(buildMobilePhone(resultSet));
            }
            return resultList;
        } catch (SQLException e) {
            throw new DaoException(GET_NEXT_DATA_FOR_MOBILE_PHONE_BUILDING_EXCEPTION, e);
        }

    }

    /**
     * Builds mobile phone object from current {@link ResultSet}'s row.
     *
     * @param resultSet set, containing mobile phone's data
     * @return {@link MobilePhone}, built from ResultSet's current row
     * @throws DaoException if exception was thrown because of reading data from result set
     */
    private MobilePhone buildMobilePhone(ResultSet resultSet) throws DaoException {
        MobilePhone mobilePhone = new MobilePhone();

        try {
            mobilePhone.setIdMobilePhone(resultSet.getInt(1));
            mobilePhone.setMobilePhone(resultSet.getString(2));
        } catch (SQLException e) {
            throw new DaoException(BUILD_MOBILE_PHONE_EXCEPTION, e);
        }

        return mobilePhone;
    }

    public ConnectionAllocator getConnectionAllocator() {
        return connectionAllocator;
    }

    public void setConnectionAllocator(ConnectionAllocator connectionAllocator) {
        this.connectionAllocator = connectionAllocator;
    }
}
