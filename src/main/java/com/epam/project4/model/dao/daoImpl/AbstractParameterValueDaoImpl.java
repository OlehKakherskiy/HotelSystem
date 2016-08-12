package main.java.com.epam.project4.model.dao.daoImpl;

import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.model.dao.AbstractParameterValueDao;
import main.java.com.epam.project4.model.entity.roomParameter.Parameter;
import main.java.com.epam.project4.model.entity.roomParameter.ParameterValueTuple;
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
 * Class represents implementation of {@link AbstractParameterValueDao} for relational databases, represented
 * via {@link DataSource}.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see DataSource
 */
public class AbstractParameterValueDaoImpl implements AbstractParameterValueDao {

    /**
     * db request for selecting all {@link Value} data
     */
    private static final String allValues = "SELECT * FROM Value_Pool";

    /**
     * db request for selecting all {@link Parameter} data
     */
    private static final String allParams = "SELECT * FROM Room_Parameter";

    /**
     * db request for selecting all {@link ParameterValueTuple} data
     */
    private static final String allParamValues = "SELECT * FROM Parameter_Values";

    private static final String buildEntityListException = "Exception caused while was request of selecting data for " +
            "building list of {0}";

    private static final String buildingNextEntityException = "Exception was occurred because of building next {0} " +
            "object from ResultSet";

    private static final String getParameterValueMapDbRequestException = "Exception was occurred while was request of " +
            "selecting parameter-value map data from DB";

    private static final String openConnectionException = "Exception was occurred while was attempt to get connection " +
            "from datasource object in class {0}";

    /**
     * datasource, from wich {@link Connection} will be get for processing operations with persistent storage
     */
    private DataSource dataSource;

    /**
     * {@inheritDoc}
     * <p>
     * <p>
     * Maps list of {@link Value} via {@link #selectValuePool(Connection)} and
     * list of {@link Parameter} via {@link #selectParameterList(Connection, List)}
     * and maps {@link ParameterValueTuple} resultSet's data to list of {@link ParameterValueTuple}, using
     * previous two lists
     * </p>
     *
     * @return {@inheritDoc}
     * @throws DaoException {@inheritDoc}
     */
    @Override
    public List<ParameterValueTuple> getAllFullInfo() throws DaoException {
        try (Connection connection = dataSource.getConnection()) {

            List<Value> valuePool = selectValuePool(connection);
            List<Parameter> parameters = selectParameterList(connection, valuePool);

            return selectParameterValueMapAndBuild(connection, parameters, valuePool);
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format(openConnectionException, this.getClass()));
        }
    }

    /**
     * Map data, representing {@link ParameterValueTuple}, from relational representation to list of {@link ParameterValueTuple}.
     * If there are no values in DB, returns empty list
     *
     * @param connection connection, through which select {@link #allParams} will be executed
     * @param parameters list of {@link Parameter}, that is used to init {@link ParameterValueTuple#parameter}
     * @param valuePool  list of {@link Value}, that is used to init {@link ParameterValueTuple#value}
     * @return list of {@link ParameterValueTuple} or empty list
     * @throws DaoException if there was an exception during the object-relational mapping process
     */
    private List<ParameterValueTuple> selectParameterValueMapAndBuild(Connection connection, List<Parameter> parameters,
                                                                      List<Value> valuePool) throws DaoException {
        List<ParameterValueTuple> parameterValueTuples = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(allParamValues);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            parameterValueTuples = buildResultList(resultSet, parameters, valuePool);
        } catch (SQLException e) {
            throw new DaoException(getParameterValueMapDbRequestException, e);
        }
        return parameterValueTuples;
    }

    /**
     * @param resultSet  set, containing data for mapping to {@link ParameterValueTuple}
     * @param parameters list of {@link Parameter}, that is used to init {@link ParameterValueTuple#parameter}
     * @param values     list of {@link Value}, that is used to init {@link ParameterValueTuple#value}
     * @return list of {@link ParameterValueTuple} or empty list
     * @throws DaoException if exception was thrown during the process of mapping
     *                      data from result set to {@link ParameterValueTuple}
     */
    private List<ParameterValueTuple> buildResultList(ResultSet resultSet, List<Parameter> parameters,
                                                      List<Value> values) throws DaoException {
        List<ParameterValueTuple> list = new ArrayList<>();
        try {
            while (resultSet.next()) {
                ParameterValueTuple parameterValueTuple = new ParameterValueTuple();
                parameterValueTuple.setId(resultSet.getInt(1));
                parameterValueTuple.setValue(getValue(resultSet.getInt(2), values));
                parameterValueTuple.setParameter(getParameter(resultSet.getInt(3), parameters));
                parameterValueTuple.setPrice(resultSet.getInt(4));
                list.add(parameterValueTuple);
            }
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format(buildEntityListException, ParameterValueTuple.class.getName()));
        }
        return list;
    }

    /**
     * Returns the {@link Parameter} with target id or null, if there's no element with specific id.
     *
     * @param paramId    target id
     * @param parameters list, which contains element with target id
     * @return {@link Parameter} which {@link Parameter#id} is equal to target one
     */
    private Parameter getParameter(int paramId, List<Parameter> parameters) {
        return parameters.stream().filter(parameter -> parameter.getId() == paramId).findFirst().orElse(null);
    }

    /**
     * Map data, representing {@link Parameter}, from relational representation to list of {@link Parameter}.
     * If there are no values in DB, returns empty list
     *
     * @param connection connection, through which select {@link #allParams} will be executed
     * @param values     list of {@link Value}, that is used to init {@link Parameter#defaultValue}
     * @return list of {@link Value} or empty list
     * @throws DaoException if there was an exception during the object-relational mapping process
     */
    private List<Parameter> selectParameterList(Connection connection, List<Value> values) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(allParams);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return buildParameterList(resultSet, values);
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format(buildEntityListException, Parameter.class.getName()), e);
        }
    }

    /**
     * Builds list of {@link Parameter} from ResultSet's list of rows. If there's no data
     * in {@link ResultSet} - returns empty list. Also inits {@link Parameter#defaultValue}
     * via {@link #getValue(int, List)}.
     * Builds list of {@link Parameter} from ResultSet's list of rows. If there's no data
     * in {@link ResultSet} - returns empty list
     *
     * @param resultSet set, containing data for mapping to {@link Parameter}
     * @param values    {@link Value} list, need for exceuting {@link #getValue(int, List)}
     * @return list of {@link Parameter} or empty list
     * @throws DaoException if exception was thrown during the process of mapping
     *                      data from result set to {@link Parameter}
     */
    private List<Parameter> buildParameterList(ResultSet resultSet, List<Value> values) throws DaoException {
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

    /**
     * Returns the {@link Value} with target id or null, if there's no element with specific id.
     *
     * @param valueId target id
     * @param values  list, which contains element with target id
     * @return {@link Value} which {@link Value#id} is equal to target one
     */
    private Value getValue(int valueId, List<Value> values) {
        return values.stream().filter(value -> value.getId() == valueId).findFirst().orElse(null);
    }

    /**
     * Map data, representing {@link Value}, from relational representation to list of {@link Value}.
     * If there are no values in DB, returns empty list
     *
     * @param connection connection, through which select {@link #allValues} will be executed
     * @return list of {@link Value} or empty list
     * @throws DaoException if there was an exception during the object-relational mapping process
     */
    private List<Value> selectValuePool(Connection connection) throws DaoException {
        List<Value> result = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(allValues);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            result = buildValueList(resultSet);

        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format(buildEntityListException, Value.class.getName()), e);
        }
        return result;
    }

    /**
     * Builds list of {@link Value} from ResultSet's list of rows. If there's no data
     * in {@link ResultSet} - returns empty list
     *
     * @param resultSet set, containing data for mapping to {@link Value}
     * @return list of {@link Value} or empty list
     * @throws DaoException if exception was thrown during the process of mapping
     *                      data from result set to {@link Value}
     */
    private List<Value> buildValueList(ResultSet resultSet) throws DaoException {
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
