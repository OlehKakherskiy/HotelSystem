package model.dao.daoImpl;

import model.dao.GenericParameterValueDao;
import model.entity.roomParameter.Parameter;
import model.entity.roomParameter.ParameterValue;
import model.entity.roomParameter.Value;

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
public class GenericParameterValueDaoImpl extends GenericParameterValueDao {

    private static final String allValues = "SELECT * FROM Value_Pool";

    private static final String allParams = "SELECT * FROM Room_Parameter";

    private static final String allParamValues = "SELECT * FROM Parameter_Values";

    private DataSource dataSource;

    @Override
    public List<ParameterValue> getAllFullInfo() {
        List<ParameterValue> parameterValues = null;
        try (Connection connection = dataSource.getConnection()) {

            List<Value> valuePool = getValuePool(connection);

            List<Parameter> parameters = getAllParameters(connection, valuePool);

            PreparedStatement preparedStatement = connection.prepareStatement(allParamValues);
            ResultSet resultSet = preparedStatement.executeQuery();

            parameterValues = buildResultList(resultSet, parameters, valuePool);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return parameterValues;
    }

    private List<ParameterValue> buildResultList(ResultSet resultSet, List<Parameter> parameters, List<Value> values) throws SQLException {
        List<ParameterValue> list = new ArrayList<>();
        while (resultSet.next()) {
            ParameterValue parameterValue = new ParameterValue();
            parameterValue.setId(resultSet.getInt(1));
            parameterValue.setValue(getValue(resultSet.getInt(2), values));
            parameterValue.setParameter(getParameter(resultSet.getInt(3), parameters));
            parameterValue.setPrice(resultSet.getInt(4));
            list.add(parameterValue);
        }
        return list;
    }

    private List<Parameter> getAllParameters(Connection connection, List<Value> values) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(allParams);
        ResultSet resultSet = preparedStatement.executeQuery();
        return buildParameters(resultSet, values);
    }

    private List<Parameter> buildParameters(ResultSet resultSet, List<Value> values) throws SQLException {
        List<Parameter> list = new ArrayList<>();
        while (resultSet.next()) {
            Parameter parameter = new Parameter();
            parameter.setId(resultSet.getInt(1));
            parameter.setParamName(resultSet.getString(2));
            parameter.setDefaultValue(getValue(resultSet.getInt(3), values));
            parameter.setOptional(resultSet.getBoolean(4));
            list.add(parameter);
        }
        return list;
    }

    private Parameter getParameter(int paramId, List<Parameter> parameters) {
        return parameters.stream().filter(parameter -> parameter.getId() == paramId).findFirst().get();
    }

    private Value getValue(int valueId, List<Value> values) {
        return values.stream().filter(value -> value.getId() == valueId).findFirst().get();
    }

    private List<Value> getValuePool(Connection connection) throws SQLException {
        //TODO: когда закрывать стейтменты, коннекшены и резалт сэты?
        PreparedStatement preparedStatement = connection.prepareStatement(allValues);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Value> result = buildList(resultSet);
        return result;
    }

    private List<Value> buildList(ResultSet resultSet) throws SQLException {
        List<Value> list = new ArrayList<>();
        while (resultSet.next()) {
            Value value = new Value();
            value.setId(resultSet.getInt(1));
            value.setValue(resultSet.getString(2));
            list.add(value);
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
