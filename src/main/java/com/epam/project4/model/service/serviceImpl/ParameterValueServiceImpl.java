package main.java.com.epam.project4.model.service.serviceImpl;

import main.java.com.epam.project4.app.GlobalContext;
import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.exception.SystemException;
import main.java.com.epam.project4.model.dao.AbstractParameterValueDao;
import main.java.com.epam.project4.model.entity.roomParameter.Parameter;
import main.java.com.epam.project4.model.entity.roomParameter.ParameterValue;
import main.java.com.epam.project4.model.service.IParameterValueService;

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
     * for executing operations with {@link ParameterValue}
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
    public List<ParameterValue> getParamValueList(List<Integer> parameterValueIdList) {
        List<ParameterValue> fullParamList = getAllParams();
        List<ParameterValue> result = new ArrayList<>();
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
    public Map<Parameter, List<ParameterValue>> getParameterValueMap() {
        Map<Parameter, List<ParameterValue>> result = (Map<Parameter, List<ParameterValue>>)
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
     * Reformat {@link ParameterValue} list to Map, where key is {@link Parameter} object, and value is
     * list of {@link ParameterValue} and key is equal to each {@link ParameterValue#parameter} object.
     *
     * @param list list, whose params will be regrouped to map
     * @return reformatted list's data, where key is {@link Parameter} object, and value is
     * list of {@link ParameterValue} and key is equal to each {@link ParameterValue#parameter} object.
     */
    private Map<Parameter, List<ParameterValue>> parameterValueMapFromList(List<ParameterValue> list) {
        return list.stream().collect(Collectors.groupingBy(ParameterValue::getParameter));
    }

    /**
     * Returns {@link ParameterValue} list, cached from {@link GlobalContext} or as a result of
     * {@link #addToCacheAndReturn()}.
     *
     * @return list of {@link ParameterValue}
     */
    private List<ParameterValue> getAllParams() {
        List<ParameterValue> parameterValues = (List<ParameterValue>)
                GlobalContext.getValue(GlobalContextConstant.PARAMETER_VALUE_LIST); //TODO: тут нужно аккуратно - многопоточность
        return (parameterValues != null) ? parameterValues : addToCacheAndReturn();
    }

    /**
     * adds to {@link GlobalContext} result of {@link AbstractParameterValueDao#getAllFullInfo()}
     * and returns list of {@link ParameterValue}.
     *
     * @return list of {@link ParameterValue}
     * @throws SystemException if exception was thrown during the process of selecting data from persistent storage
     */
    private List<ParameterValue> addToCacheAndReturn() {
        try {
            List<ParameterValue> parameterValues = parameterValueDao.getAllFullInfo();

            GlobalContext.addToGlobalContext(GlobalContextConstant.PARAMETER_VALUE_LIST, parameterValues);
            return parameterValues;

        } catch (DaoException e) {
            throw new SystemException(MessageCode.GET_PARAMETER_VALUE_LIST_SYSTEM_EXCEPTION, e);
        }
    }
}