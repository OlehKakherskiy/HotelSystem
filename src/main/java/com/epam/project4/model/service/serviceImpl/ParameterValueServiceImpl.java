package main.java.com.epam.project4.model.service.serviceImpl;

import main.java.com.epam.project4.app.GlobalContext;
import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.exception.SystemException;
import main.java.com.epam.project4.model.dao.AbstractParameterValueDao;
import main.java.com.epam.project4.model.entity.roomParameter.Parameter;
import main.java.com.epam.project4.model.entity.roomParameter.ParameterValue;
import main.java.com.epam.project4.model.entity.roomParameter.Value;
import main.java.com.epam.project4.model.service.AbstractParameterValueService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ParameterValueServiceImpl implements AbstractParameterValueService {

    private AbstractParameterValueDao parameterValueDao;

    public ParameterValueServiceImpl(AbstractParameterValueDao parameterValueDao) {
        this.parameterValueDao = parameterValueDao;
    }

    @Override
    public List<ParameterValue> getParamValueList(List<Integer> parameterValueIdList) { //возвращаем paramValue со списка по id
        return getParamValueListUsingIdList(parameterValueIdList,
                (paramValueId, parameterValue) -> parameterValue.getId() == paramValueId);
    }


    private List<ParameterValue> getParamValueListUsingIdList(List<Integer> idList,
                                                              BiPredicate<Integer, ParameterValue> filterCriteria) {
        List<ParameterValue> fullParamList = getAllParams();
        List<ParameterValue> result = new ArrayList<>();
        idList.stream()
                .forEach(id -> fullParamList
                        .stream()
                        .filter(parameterValue -> filterCriteria.test(id, parameterValue))
                        .findFirst()
                        .ifPresent(result::add));
        return result;
    }

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

    private Map<Parameter, List<ParameterValue>> parameterValueMapFromList(List<ParameterValue> list) {
        return list.stream().collect(Collectors.groupingBy(ParameterValue::getParameter));
    }

    private List<Value> getTargetList(Map<Parameter, List<Value>> map, Parameter parameter) {
        List<Value> currentList = map.get(parameter);

        if (currentList == null) {
            currentList = new ArrayList<>();
            map.put(parameter, currentList);
        }

        return currentList;
    }

    private List<ParameterValue> getAllParams() {
        List<ParameterValue> parameterValues = (List<ParameterValue>)
                GlobalContext.getValue(GlobalContextConstant.PARAMETER_VALUE_LIST); //TODO: тут нужно аккуратно - многопоточность
        return (parameterValues != null) ? parameterValues : addToCacheAndReturn();
    }

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