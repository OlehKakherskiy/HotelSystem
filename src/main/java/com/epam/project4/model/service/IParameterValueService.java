package main.java.com.epam.project4.model.service;

import main.java.com.epam.project4.model.entity.roomParameter.Parameter;
import main.java.com.epam.project4.model.entity.roomParameter.ParameterValue;

import java.util.List;
import java.util.Map;

/**
 * Interface represents API for executing operations, assosiated with {@link ParameterValue} entity
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see ParameterValue
 */
public interface IParameterValueService extends IService {

    /**
     * Returns list of {@link ParameterValue}, each one is assosiated with id from list parameter.
     * If list parameter is empty - returns empty list. Parameter MUSTN'T be null.
     *
     * @param ids list of {@link ParameterValue}'s id
     * @return list of {@link ParameterValue}, assosiated with id's list or empty list
     * @throws main.java.com.epam.project4.exception.SystemException if exception was
     *                                                               thrown during processing any underlying operation
     */
    List<ParameterValue> getParamValueList(List<Integer> ids);

    /**
     * Returns map, which keys are {@link Parameter} and values - list of {@link ParameterValue}, assosiated
     * with current key (key is equal to each {@link ParameterValue}'s field {@link ParameterValue#parameter}.
     * Map can be empty.
     *
     * @return map, which keys are {@link Parameter} and values - list of {@link ParameterValue}, assosiated
     * with current key (key is equal to each {@link ParameterValue}'s field {@link ParameterValue#parameter}.
     * @throws main.java.com.epam.project4.exception.SystemException if exception was
     *                                                               thrown during processing any underlying operation
     */
    Map<Parameter, List<ParameterValue>> getParameterValueMap();
}
