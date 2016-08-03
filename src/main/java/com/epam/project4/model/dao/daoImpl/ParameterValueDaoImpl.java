package main.java.com.epam.project4.model.dao.daoImpl;

import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.model.dao.AbstractParameterValueDao;
import main.java.com.epam.project4.model.entity.roomParameter.Parameter;
import main.java.com.epam.project4.model.entity.roomParameter.ParameterValue;
import main.java.com.epam.project4.model.entity.roomParameter.Value;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ParameterValueDaoImpl extends AbstractParameterValueDao {

    private static final String allValues = "SELECT * FROM Value_Pool";

    private static final String allParams = "SELECT * FROM Room_Parameter";

    private static final String allParamValues = "SELECT * FROM Parameter_Values";

    private static final String buildEntityListException = "Exception caused while was request of selecting data for " +
            "building list of {0}";

    private static final String buildingNextEntityException = "Exception was occurred because of building next {0} " +
            "object from ResultSet";

    private static final String getParameterValueMapDbRequestException = "Exception was occurred while was request of " +
            "selecting parameter-value map data from DB";

    private static final String openConnectionException = "Exception was occurred while was attempt to get connection " +
            "from datasource object in class {0}";

    private DataSource dataSource;

    @Override
    public List<ParameterValue> getAllFullInfo() throws DaoException {
        try (Connection connection = dataSource.getConnection()) {

            List<Value> valuePool = getValuePool(connection);
            List<Parameter> parameters = getAllParameters(connection, valuePool);

            return getParameterValueMapAndBuild(connection, parameters, valuePool);
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format(openConnectionException, this.getClass()));
        }
    }

    private List<ParameterValue> getParameterValueMapAndBuild(Connection c, List<Parameter> parameters,
                                                              List<Value> valuePool) throws DaoException {
        List<ParameterValue> parameterValues = null;
        try (PreparedStatement preparedStatement = c.prepareStatement(allParamValues);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            parameterValues = buildResultList(resultSet, parameters, valuePool);
        } catch (SQLException e) {
            throw new DaoException(getParameterValueMapDbRequestException, e);
        }
        return parameterValues;
    }

    private List<ParameterValue> buildResultList(ResultSet resultSet, List<Parameter> parameters,
                                                 List<Value> values) throws DaoException {
        List<ParameterValue> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                ParameterValue parameterValue = new ParameterValue();
                parameterValue.setId(resultSet.getInt(1));
                parameterValue.setValue(getValue(resultSet.getInt(2), values));
                parameterValue.setParameter(getParameter(resultSet.getInt(3), parameters));
                parameterValue.setPrice(resultSet.getInt(4));
                list.add(parameterValue);
            }
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format(buildEntityListException, ParameterValue.class.getName()));
        }
        return list;
    }

    private Parameter getParameter(int paramId, List<Parameter> parameters) {
        return parameters.stream().filter(parameter -> parameter.getId() == paramId).findFirst().orElse(null);
    }

    private List<Parameter> getAllParameters(Connection connection, List<Value> values) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(allParams);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return buildParameters(resultSet, values);
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format(buildEntityListException, Parameter.class.getName()), e);
        }
    }

    private List<Parameter> buildParameters(ResultSet resultSet, List<Value> values) throws DaoException {
        List<Parameter> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Parameter parameter = new Parameter();
                parameter.setId(resultSet.getInt(1));
                parameter.setParamName(resultSet.getString(2));
                parameter.setDefaultValue(getValue(resultSet.getInt(3), values));
                parameter.setOptional(resultSet.getBoolean(4));
                list.add(parameter);
            }
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format(buildingNextEntityException, Parameter.class.getName()));
        }
        return list;
    }

    private Value getValue(int valueId, List<Value> values) {
        return values.stream().filter(value -> value.getId() == valueId).findFirst().orElse(null);
    }

    private List<Value> getValuePool(Connection connection) throws DaoException {
        List<Value> result = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(allValues);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            result = buildList(resultSet);

        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format(buildEntityListException, Value.class.getName()), e); //TODO: executing exception
        }
        return result;
    }

    private List<Value> buildList(ResultSet resultSet) throws DaoException {
        List<Value> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Value value = new Value();
                value.setId(resultSet.getInt(1));
                value.setValue(resultSet.getString(2));
                list.add(value);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format(buildingNextEntityException, Value.class.getName()), e);
        }
        return list;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
