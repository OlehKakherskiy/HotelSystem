package model.dao.daoImpl;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class JdbcTemplate {

    private static DataSource dataSource;

    public static <V> V executePreparedStatementQuery(Connection c, String query, Map<Object, Class> parameters,
                                                      Function<List<List<Object>>, V> toObjectMapper, SqlOperationType type) {
        V res = null;
        try (PreparedStatement preparedStatement = (type == SqlOperationType.INSERT)
                ? c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
                : c.prepareStatement(query)) {

            insertParams(preparedStatement, parameters);
            preparedStatement.execute();
            return toObjectMapper.apply(reformatResultSet(preparedStatement.getResultSet()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    private static List<List<Object>> reformatResultSet(ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        List<List<Object>> resultTable = new ArrayList<>();
        while (resultSet.next()) {
            resultTable.add(reformatRow(resultSet, columnCount));
        }
        resultSet.close();
        return resultTable;
    }

    private static List<Object> reformatRow(ResultSet resultSet, int columnCount) throws SQLException {
        List<Object> row = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            row.add(resultSet.getObject(i));
        }
        return row;
    }
}
