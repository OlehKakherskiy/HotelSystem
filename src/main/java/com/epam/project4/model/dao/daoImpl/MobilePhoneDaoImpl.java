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
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class MobilePhoneDaoImpl extends AbstractMobilePhoneDao {

    private static final String sqlRequestException = "Exception was occurred while was executing sql request " +
            "for getting mobilePhoneList";

    private static final String buildMobilePhoneException = "Exception was occurred while was building operation of " +
            "MobilePhone entity from ResultSet";

    private static final String getNextDataForMobilePhoneBuildingException = "Exception was occurred because next() " +
            "method was called from resultSet object while MobilePhone list was being built";

    private static final String getMobilePhoneList = "SELECT idMobilePhone, phone_number " +
            "FROM MOBILE_PHONE " +
            "WHERE id_User=?";

    private DataSource dataSource;

    @Override
    public List<MobilePhone> getAll(int userID) throws DaoException {
        ResultSet resultSet = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getMobilePhoneList)) {
            resultSet = statement.executeQuery();
            statement.setInt(1, userID);
            return buildList(resultSet);
        } catch (SQLException e) {
            throw new DaoException(sqlRequestException, e);
        }
    }


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
