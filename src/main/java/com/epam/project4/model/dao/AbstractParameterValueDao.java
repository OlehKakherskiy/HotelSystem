package main.java.com.epam.project4.model.dao;

import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.model.entity.roomParameter.Parameter;
import main.java.com.epam.project4.model.entity.roomParameter.ParameterValue;
import main.java.com.epam.project4.model.entity.roomParameter.Value;

import java.util.List;

/**
 * Interface extends basic CRUD operations which are performed whith {@link ParameterValue} entity.
 * Defines additional operation, which allows to get all {@link ParameterValue} objects of specific user.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see ParameterValue
 * @see Parameter
 * @see Value
 */
public interface AbstractParameterValueDao extends GenericDao<ParameterValue> {

    /**
     * maps all data, connected to {@link ParameterValue}, to list of this entity.
     * Inits all fields of this entity type
     *
     * @return list of {@link ParameterValue}
     * @throws DaoException if there was any type of exceptions during processing operations
     *                      with persistent storage or mapping data from storage format to
     *                      object representation
     */
    List<ParameterValue> getAllFullInfo() throws DaoException;
}