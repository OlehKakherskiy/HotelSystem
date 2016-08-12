package main.java.com.hotelSystem.dao;

import main.java.com.hotelSystem.exception.DaoException;
import main.java.com.hotelSystem.model.roomParameter.Parameter;
import main.java.com.hotelSystem.model.roomParameter.ParameterValueTuple;
import main.java.com.hotelSystem.model.roomParameter.Value;

import java.util.List;

/**
 * Interface extends basic CRUD operations which are performed whith {@link ParameterValueTuple} entity.
 * Defines additional operation, which allows to get all {@link ParameterValueTuple} objects of specific user.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see ParameterValueTuple
 * @see Parameter
 * @see Value
 */
public interface AbstractParameterValueDao extends GenericDao<ParameterValueTuple> {

    /**
     * maps all data, connected to {@link ParameterValueTuple}, to list of this entity.
     * Inits all fields of this entity type
     *
     * @return list of {@link ParameterValueTuple}
     * @throws DaoException if there was any type of exceptions during processing operations
     *                      with persistent storage or mapping data from storage format to
     *                      object representation
     */
    List<ParameterValueTuple> getAllFullInfo() throws DaoException;
}