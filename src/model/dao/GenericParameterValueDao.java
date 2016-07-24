package model.dao;

import model.entity.roomParameter.ParameterValue;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericParameterValueDao extends TransparentGenericDao<ParameterValue, Integer> {

    public abstract List<ParameterValue> getAllFullInfo();

}
