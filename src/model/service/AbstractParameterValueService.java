package model.service;

import model.entity.roomParameter.Parameter;
import model.entity.roomParameter.ParameterValue;
import model.entity.roomParameter.Value;

import java.util.List;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface AbstractParameterValueService extends AbstractService {

    List<ParameterValue> getParamValueList(List<Integer> ids);

    List<ParameterValue> getParamValueListFromParamIdList(List<Integer> ids);

    Map<Parameter, List<Value>> getParameterValueMap();
}
