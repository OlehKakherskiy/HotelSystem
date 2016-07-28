package main.java.com.epam.project4.model.service;

import main.java.com.epam.project4.model.entity.roomParameter.ParameterValue;
import main.java.com.epam.project4.model.entity.roomParameter.Value;
import main.java.com.epam.project4.model.entity.roomParameter.Parameter;

import java.util.List;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface AbstractParameterValueService extends AbstractService {

    List<ParameterValue> getParamValueList(List<Integer> ids);

//    List<ParameterValue> getParamValueListFromParamIdList(List<Integer> ids);

    Map<Parameter, List<Value>> getParameterValueMap();
}
