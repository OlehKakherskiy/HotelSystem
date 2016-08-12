package main.java.com.hotelSystem.service;

import main.java.com.hotelSystem.exception.SystemException;
import main.java.com.hotelSystem.model.roomParameter.Parameter;
import main.java.com.hotelSystem.model.roomParameter.ParameterValueTuple;

import java.util.List;
import java.util.Map;

/**
 * Interface represents API for executing operations, assosiated with {@link ParameterValueTuple} entity
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see ParameterValueTuple
 */
public interface IParameterValueService extends IService {

    /**
     * Returns list of {@link ParameterValueTuple}, each one is assosiated with id from list parameter.
     * If list parameter is empty - returns empty list. Parameter MUSTN'T be null.
     *
     * @param ids list of {@link ParameterValueTuple}'s id
     * @return list of {@link ParameterValueTuple}, assosiated with id's list or empty list
     * @throws SystemException if exception was
     *                                                               thrown during processing any underlying operation
     */
    List<ParameterValueTuple> getParamValueList(List<Integer> ids);

    /**
     * Returns map, which keys are {@link Parameter} and values - list of {@link ParameterValueTuple}, assosiated
     * with current key (key is equal to each {@link ParameterValueTuple}'s field {@link ParameterValueTuple#parameter}.
     * Map can be empty.
     *
     * @return map, which keys are {@link Parameter} and values - list of {@link ParameterValueTuple}, assosiated
     * with current key (key is equal to each {@link ParameterValueTuple}'s field {@link ParameterValueTuple#parameter}.
     * @throws SystemException if exception was
     *                                                               thrown during processing any underlying operation
     */
    Map<Parameter, List<ParameterValueTuple>> getParameterValueMap();
}
