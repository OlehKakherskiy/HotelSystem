package main.java.com.epam.project4.model.dao.daoImpl;

import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.model.dao.AbstractMobilePhoneDao;
import main.java.com.epam.project4.model.entity.MobilePhone;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class represents implementation of {@link AbstractMobilePhoneDao} for relational databases, represented
 * via {@link DataSource}.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see DataSource
 */
public class AbstractMobilePhoneDaoImpl implements AbstractMobilePhoneDao {

    private static final String sqlRequestException = "Exception was occurred while was executing sql request " +
            "for getting mobilePhoneList";

    private static final String buildMobilePhoneException = "Exception was occurred while was building operation of " +
            "MobilePhone entity from ResultSet";

    private static final String getNextDataForMobilePhoneBuildingException = "Exception was occurred because next() " +
            "method was called from resultSet object while MobilePhone list was being built";

    /**
     * db select for gettin all mobile phones of specific user
     */
    private static final String getMobilePhoneList = "SELECT idMobilePhone, phone_number " +
            "FROM MOBILE_PHONE " +
            "WHERE id_User=?";

    /**
     * datasource, from wich {@link Connection} will be get for processing operations with persistent storage
     */
    private DataSource dataSource;

    /**
     * {@inheritDoc}
     * <p>
     * <p>
     * Inits statement {@link #getMobilePhoneList}, sets param to it and exectutes it. While {@link ResultSet} is
     * get, calls {@link #buildList(ResultSet)}
     * </p>
     *
     * @param userId id of target {@link main.java.com.epam.project4.model.entity.User} object
     * @return {@inheritDoc}
     * @throws DaoException {@inheritDoc}
     */
    @Override
    public List<MobilePhone> getAll(int userId) throws DaoException {
        ResultSet resultSet = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getMobilePhoneList)) {
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();
            return buildList(resultSet);
        } catch (SQLException e) {
            throw new DaoException(sqlRequestException, e);
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
            throw new DaoException(getNextDataForMobilePhoneBuildingException, e);
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
            throw new DaoException(buildMobilePhoneException, e);
        }

        return mobilePhone;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
