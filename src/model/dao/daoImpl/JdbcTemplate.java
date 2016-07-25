package model.dao.daoImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class JdbcTemplate {

    private static DataSource dataSource;

    public static <T, V> V executePreparedStatementQuery(String query, Map<Object, Class> parameters,
                                                         Function<PreparedStatement, V> toObjectMapper, SqlOperationType type) {
        V res = null;
        try (Connection c = dataSource.getConnection()) {
            PreparedStatement preparedStatement = (type == SqlOperationType.INSERT) ? buildInsertStatement(c, query)
                    : c.prepareStatement(query);

            preparedStatement.execute();
            return toObjectMapper.apply(preparedStatement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static PreparedStatement buildInsertStatement(Connection connection, String query) throws SQLException {
        return connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    }

    private static void insertParams(PreparedStatement preparedStatement, Map<Object, Class> params) throws SQLException {
        int currentPos = 1;
        for (Map.Entry<Object, Class> entry : params.entrySet()) {
            if (entry.getValue() == Integer.class) {
                preparedStatement.setInt(currentPos++, (Integer) entry.getKey());
            } else if (entry.getValue() == String.class) {
                preparedStatement.setString(currentPos++, (String) entry.getKey());
            } else {
                preparedStatement.setObject(currentPos++, entry.getKey());//TODO: хз что тут будет
            }
        }
    }
}
