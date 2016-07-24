package model.service;

import model.entity.roomParameter.Parameter;
import model.entity.roomParameter.ParameterValue;
import model.entity.roomParameter.Value;

import java.util.List;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface AbstractParameterValueService {

    List<ParameterValue> getAllParams(List<Integer> ids);

    Map<Parameter, List<Value>> getParameterValueMap();
}
