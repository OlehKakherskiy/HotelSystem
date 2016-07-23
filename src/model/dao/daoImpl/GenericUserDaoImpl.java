package model.dao.daoImpl;

import model.dao.GenericUserDao;
import model.entity.User;
import model.entity.enums.UserType;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GenericUserDaoImpl extends GenericUserDao {

    private static final String getUserFromID =
            "SELECT idUser, name, last_name, idUserType, " +
                    "FROM USER " +
                    "WHERE idUser=?";

    private static final String getUserFromLoginAndPassword =
            "SELECT idUser, name, last_name, idUserType " +
                    "FROM USER " +
                    "WHERE login=? AND password=?";


    @Resource(name = "datasource")
    private DataSource dataSource;

    @Override
    public User read(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUserFromID)) {

            statement.setInt(1, id);

            return buildUserObject(statement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User tryLogin(String login, String password) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUserFromLoginAndPassword)) {

            statement.setString(1, login);
            statement.setString(2, password);

            return buildUserObject(statement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User buildUserObject(ResultSet resultSet) throws SQLException {
        User user = new User();
        while (resultSet.next()) {
            user.setIdUser(resultSet.getInt(1));
            user.setName(resultSet.getString(2));
            user.setLastName(resultSet.getString(3));
            user.setUserType(UserType.fromID(resultSet.getInt(4)));
        }
        return (user.getIdUser() != 0) ? user : null;
    }

}
