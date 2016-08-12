package main.java.com.hotelSystem.service.serviceImpl;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.app.constants.MessageCode;
import main.java.com.hotelSystem.dao.AbstractParameterValueDao;
import main.java.com.hotelSystem.exception.DaoException;
import main.java.com.hotelSystem.exception.SystemException;
import main.java.com.hotelSystem.model.roomParameter.Parameter;
import main.java.com.hotelSystem.model.roomParameter.ParameterValueTuple;
import main.java.com.hotelSystem.service.IParameterValueService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class represents implementation of {@link IParameterValueService} and
 * uses {@link AbstractParameterValueDao} for executing business-logic operations.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see AbstractParameterValueDao
 */
public class ParameterValueServiceImpl implements IParameterValueService {

    /**
     * for executing operations with {@link ParameterValueTuple}
     */
    private AbstractParameterValueDao parameterValueDao;

    /**
     * Inits all fields. Each parameter MUSTN'T be null.
     *
     * @param parameterValueDao inits {@link #parameterValueDao}
     */
    public ParameterValueServiceImpl(AbstractParameterValueDao parameterValueDao) {
        this.parameterValueDao = parameterValueDao;
    }

    /**
     * {@inheritDoc}
     *
     * @param parameterValueIdList {@inheritDoc}
     * @return {@inheritDoc}
     * @throws SystemException if exception was thrown during the process of selecting data from persistent storage
     */
    @Override
    public List<ParameterValueTuple> getParamValueList(List<Integer> parameterValueIdList) {
        List<ParameterValueTuple> fullParamList = getAllParams();
        List<ParameterValueTuple> result = new ArrayList<>();
        parameterValueIdList.stream()
                .forEach(id -> fullParamList
                        .stream()
                        .filter(parameterValue -> parameterValue.getId() == id)
                        .findFirst()
                        .ifPresent(result::add));
        return result;
    }


    /**
     * {@inheritDoc}
     * <p>
     * Tries to get cached map from the {@link GlobalContext}, if null - returns map as
     * a result of {@link #parameterValueMapFromList(List)}
     * </p>
     *
     * @return {@inheritDoc}
     * @throws SystemException if exception was thrown during the process of selecting data from persistent storage
     */
    @Override
    public Map<Parameter, List<ParameterValueTuple>> getParameterValueMap() {
        Map<Parameter, List<ParameterValueTuple>> result = (Map<Parameter, List<ParameterValueTuple>>)
                GlobalContext.getValue(GlobalContextConstant.PARAMETER_VALUE_MAP);

        if (result != null) {
            return result;
        } else {
            result = parameterValueMapFromList(getAllParams());
            GlobalContext.addToGlobalContext(GlobalContextConstant.PARAMETER_VALUE_MAP, result);
        }

        return result;
    }

    /**
     * Reformat {@link ParameterValueTuple} list to Map, where key is {@link Parameter} object, and value is
     * list of {@link ParameterValueTuple} and key is equal to each {@link ParameterValueTuple#parameter} object.
     *
     * @param list list, whose params will be regrouped to map
     * @return reformatted list's data, where key is {@link Parameter} object, and value is
     * list of {@link ParameterValueTuple} and key is equal to each {@link ParameterValueTuple#parameter} object.
     */
    private Map<Parameter, List<ParameterValueTuple>> parameterValueMapFromList(List<ParameterValueTuple> list) {
        return list.stream().collect(Collectors.groupingBy(ParameterValueTuple::getParameter));
    }

    /**
     * Returns {@link ParameterValueTuple} list, cached from {@link GlobalContext} or as a result of
     * {@link #addToCacheAndReturn()}.
     *
     * @return list of {@link ParameterValueTuple}
     */
    private List<ParameterValueTuple> getAllParams() {
        List<ParameterValueTuple> parameterValueTuples = (List<ParameterValueTuple>)
                GlobalContext.getValue(GlobalContextConstant.PARAMETER_VALUE_LIST);
        return (parameterValueTuples != null) ? parameterValueTuples : addToCacheAndReturn();
    }

    /**
     * adds to {@link GlobalContext} result of {@link AbstractParameterValueDao#getAllFullInfo()}
     * and returns list of {@link ParameterValueTuple}.
     *
     * @return list of {@link ParameterValueTuple}
     * @throws SystemException if exception was thrown during the process of selecting data from persistent storage
     */
    private List<ParameterValueTuple> addToCacheAndReturn() {
        try {
            List<ParameterValueTuple> parameterValueTuples = parameterValueDao.getAllFullInfo();

            GlobalContext.addToGlobalContext(GlobalContextConstant.PARAMETER_VALUE_LIST, parameterValueTuples);
            return parameterValueTuples;

        } catch (DaoException e) {
            throw new SystemException(MessageCode.GET_PARAMETER_VALUE_LIST_SYSTEM_EXCEPTION, e);
        }
    }
}