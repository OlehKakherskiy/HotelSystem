package main.java.com.epam.project4.model.dao.daoImpl;

import main.java.com.epam.project4.model.dao.GenericUserDao;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.UserType;

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

    private static final User noUserStub = new User();

    static {
        noUserStub.setIdUser(-1);
    }

    private DataSource dataSource;

    @Override
    public User read(Integer id) {
        User user = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUserFromID)) {

            statement.setInt(1, id);

            user = buildUserObject(statement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace(); //TODO: тут добавить лог
        }
        return user;
    }

    @Override
    public User tryLogin(String login, String password) {
        User result = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(getUserFromLoginAndPassword)) {

            statement.setString(1, login);
            statement.setString(2, password);

            result = buildUserObject(statement.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace(); //TODO: тут добавить лог
        }
        return result;
    }

    private User buildUserObject(ResultSet resultSet) throws SQLException {
        User user = null;
        while (resultSet.next()) {
            user = new User();
            user.setIdUser(resultSet.getInt(1));
            user.setName(resultSet.getString(2));
            user.setLastName(resultSet.getString(3));
            user.setUserType(UserType.fromID(resultSet.getInt(4)));
        }
        resultSet.close();
        return user != null ? user : noUserStub;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
