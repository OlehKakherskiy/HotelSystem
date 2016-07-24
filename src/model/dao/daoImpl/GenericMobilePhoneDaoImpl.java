package model.dao.daoImpl;

import model.dao.GenericMobilePhoneDao;
import model.entity.MobilePhone;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GenericMobilePhoneDaoImpl extends GenericMobilePhoneDao {

    private static final String getMobilePhoneList =
            "SELECT idMobilePhone, phone_number " +
                    "FROM MOBILE_PHONE " +
                    "WHERE id_User=?";


    @Resource(name = "datasource")
    private DataSource dataSource;

    @Override
    public List<MobilePhone> getAll(int userID) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(getMobilePhoneList);
            statement.setInt(1, userID);
            return buildList(statement.executeQuery());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


    private List<MobilePhone> buildList(ResultSet resultSet) throws SQLException {
        List<MobilePhone> resultList = new ArrayList<>();
        while (resultSet.next()) {
            resultList.add(buildMobilePhone(resultSet));
        }
        return resultList;
    }


    private MobilePhone buildMobilePhone(ResultSet resultSet) throws SQLException {
        MobilePhone mobilePhone = new MobilePhone();

        mobilePhone.setIdMobilePhone(resultSet.getInt(1));
        mobilePhone.setMobilePhone(resultSet.getString(2));

        return mobilePhone;
    }

}