package main.java.com.hotelSystem.dao.daoImpl;

import main.java.com.hotelSystem.dao.AbstractParameterValueDao;
import main.java.com.hotelSystem.exception.DaoException;
import main.java.com.hotelSystem.manager.managerImpl.daoManagerImpl.ConnectionAllocator;
import main.java.com.hotelSystem.model.roomParameter.Parameter;
import main.java.com.hotelSystem.model.roomParameter.ParameterValueTuple;
import main.java.com.hotelSystem.model.roomParameter.Value;

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
 * via {@link DataSource}.It uses {@link ConnectionAllocator} for getting connection from datasource.
 * Restriction: do NOT close allocated connection.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see DataSource
 * @see ConnectionAllocator
 */
public class AbstractParameterValueDaoImpl implements AbstractParameterValueDao {

    /**
     * db request for selecting all {@link Value} data
     */
    private static final String ALL_VALUES = "SELECT * FROM Value_Pool";

    /**
     * db request for selecting all {@link Parameter} data
     */
    private static final String ALL_PARAMS = "SELECT * FROM Room_Parameter";

    /**
     * db request for selecting all {@link ParameterValueTuple} data
     */
    private static final String ALL_PARAM_VALUES = "SELECT * FROM Parameter_Values";

    private static final String BUILD_ENTITY_LIST_EXCEPTION = "Exception caused while was request of selecting data for " +
            "building list of {0}";

    private static final String BUILDING_NEXT_ENTITY_EXCEPTION = "Exception was occurred because of building next {0} " +
            "object from ResultSet";

    private static final String GET_PARAMETER_VALUE_MAP_DB_REQUEST_EXCEPTION = "Exception was occurred while was request of " +
            "selecting parameter-value map data from DB";

    /**
     * datasource, from which {@link Connection} will be gotten for processing operations with persistent storage
     */
    private ConnectionAllocator connectionAllocator;

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
        Connection connection = connectionAllocator.getConnection();
        List<Value> valuePool = selectValuePool(connection);
        List<Parameter> parameters = selectParameterList(connection, valuePool);

        return selectParameterValueMapAndBuild(connection, parameters, valuePool);
    }

    /**
     * Map data, representing {@link ParameterValueTuple}, from relational representation to list of {@link ParameterValueTuple}.
     * If there are no values in DB, returns empty list
     *
     * @param connection connection, through which select {@link #ALL_PARAMS} will be executed
     * @param parameters list of {@link Parameter}, that is used to init {@link ParameterValueTuple#parameter}
     * @param valuePool  list of {@link Value}, that is used to init {@link ParameterValueTuple#value}
     * @return list of {@link ParameterValueTuple} or empty list
     * @throws DaoException if there was an exception during the object-relational mapping process
     */
    private List<ParameterValueTuple> selectParameterValueMapAndBuild(Connection connection, List<Parameter> parameters,
                                                                      List<Value> valuePool) throws DaoException {
        List<ParameterValueTuple> parameterValueTuples = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(ALL_PARAM_VALUES);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            parameterValueTuples = buildResultList(resultSet, parameters, valuePool);
        } catch (SQLException e) {
            throw new DaoException(GET_PARAMETER_VALUE_MAP_DB_REQUEST_EXCEPTION, e);
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
            throw new DaoException(MessageFormat.format(BUILD_ENTITY_LIST_EXCEPTION, ParameterValueTuple.class.getName()));
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
     * @param connection connection, through which select {@link #ALL_PARAMS} will be executed
     * @param values     list of {@link Value}, that is used to init {@link Parameter#defaultValue}
     * @return list of {@link Value} or empty list
     * @throws DaoException if there was an exception during the object-relational mapping process
     */
    private List<Parameter> selectParameterList(Connection connection, List<Value> values) throws DaoException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ALL_PARAMS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return buildParameterList(resultSet, values);
        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format(BUILD_ENTITY_LIST_EXCEPTION, Parameter.class.getName()), e);
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
            throw new DaoException(MessageFormat.format(BUILDING_NEXT_ENTITY_EXCEPTION, Parameter.class.getName()));
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
     * @param connection connection, through which select {@link #ALL_VALUES} will be executed
     * @return list of {@link Value} or empty list
     * @throws DaoException if there was an exception during the object-relational mapping process
     */
    private List<Value> selectValuePool(Connection connection) throws DaoException {
        List<Value> result = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(ALL_VALUES);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            result = buildValueList(resultSet);

        } catch (SQLException e) {
            throw new DaoException(MessageFormat.format(BUILD_ENTITY_LIST_EXCEPTION, Value.class.getName()), e);
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
            throw new DaoException(MessageFormat.format(BUILDING_NEXT_ENTITY_EXCEPTION, Value.class.getName()), e);
        }
        return list;
    }

    public ConnectionAllocator getConnectionAllocator() {
        return connectionAllocator;
    }

    public void setConnectionAllocator(ConnectionAllocator connectionAllocator) {
        this.connectionAllocator = connectionAllocator;
    }
}
