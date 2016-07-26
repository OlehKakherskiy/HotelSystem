package model.service.serviceImpl;

import app.GlobalContext;
import app.constants.GlobalContextConstant;
import model.dao.GenericParameterValueDao;
import model.entity.roomParameter.Parameter;
import model.entity.roomParameter.ParameterValue;
import model.entity.roomParameter.Value;
import model.exception.SystemException;
import model.service.AbstractParameterValueService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ParameterValueServiceImpl implements AbstractParameterValueService {

    private GenericParameterValueDao parameterValueDao;

    public ParameterValueServiceImpl(GenericParameterValueDao parameterValueDao) {
        this.parameterValueDao = parameterValueDao;
    }

    @Override
    public List<ParameterValue> getAllParams(List<Integer> ids) { //возвращаем paramValue со списка по id
        List<ParameterValue> fullParamList = getAllParams();
        List<ParameterValue> result = new ArrayList<>();
        ids.stream()
                .forEach(id -> fullParamList.stream()
                        .filter(parameterValue -> parameterValue.getId() == id)
                        .findFirst().
                                ifPresent(result::add)
                );
        return result;
    }

    @Override
    public Map<Parameter, List<Value>> getParameterValueMap() {
        Map<Parameter, List<Value>> result = (Map<Parameter, List<Value>>) GlobalContext.getValue(GlobalContextConstant.PARAMETER_VALUE_MAP);

        if (result != null) {
            return result;
        } else {
            result = parameterValueMapFromList(getAllParams());
            GlobalContext.addToGlobalContext(GlobalContextConstant.PARAMETER_VALUE_MAP, result);
        }

        return result;
    }

    private Map<Parameter, List<Value>> parameterValueMapFromList(List<ParameterValue> list) {
        Map<Parameter, List<Value>> parameterListMap = new HashMap<>();

        list.forEach(parameterValue -> getTargetList(parameterListMap, parameterValue.getParameter())
                .add(parameterValue.getValue()));

        return parameterListMap;
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
        List<ParameterValue> parameterValues = (List<ParameterValue>) GlobalContext.getValue(GlobalContextConstant.PARAMETER_VALUE_LIST); //TODO: тут нужно аккуратно - многопоточность
        return (parameterValues != null) ? parameterValues : addToCacheAndReturn();
    }

    private List<ParameterValue> addToCacheAndReturn() {
        List<ParameterValue> parameterValues = parameterValueDao.getAllFullInfo();
        if (parameterValues == null) {
            throw new SystemException(); //TODO: Системная ошибка при загрузке возможных параметров комнат
        }
        GlobalContext.addToGlobalContext(GlobalContextConstant.PARAMETER_VALUE_LIST, parameterValues);
        return parameterValues;
    }
}