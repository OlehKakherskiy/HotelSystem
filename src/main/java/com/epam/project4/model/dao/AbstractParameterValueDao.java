package main.java.com.epam.project4.model.dao;

import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.model.entity.roomParameter.ParameterValue;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class AbstractParameterValueDao extends TransparentGenericDao<ParameterValue, Integer> {

    public abstract List<ParameterValue> getAllFullInfo() throws DaoException;

}
