package main.java.com.epam.project4.model.service;

import main.java.com.epam.project4.app.GlobalContext;
import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.model.dao.GenericParameterValueDao;
import main.java.com.epam.project4.model.entity.roomParameter.Parameter;
import main.java.com.epam.project4.model.entity.roomParameter.ParameterValue;
import main.java.com.epam.project4.model.entity.roomParameter.Value;
import main.java.com.epam.project4.model.exception.SystemException;
import main.java.com.epam.project4.model.service.serviceImpl.ParameterValueServiceImpl;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class AbstractParameterValueServiceTest {

    static AbstractParameterValueService parameterValueService;


    static Map<Parameter, List<ParameterValue>> pvMap = new HashMap<>();

    static List<ParameterValue> parameterValueList;

    @BeforeClass
    public static void beforeClass() throws Exception {
        Value v1 = new Value(1, "1");
        Value v2 = new Value(2, "2");
        Value v3 = new Value(3, "3");
        Value v4 = new Value(4, "4");
        Value v5 = new Value(5, "5");

        Value v6 = new Value(6, "6");
        Value v7 = new Value(7, "7");
        Value v8 = new Value(8, "8");
        Value v9 = new Value(9, "9");
        Value v10 = new Value(10, "10");

        Parameter p1 = new Parameter(1, false, v3, "p1");
        Parameter p2 = new Parameter(2, false, v5, "p2");

        ParameterValue pv1 = new ParameterValue(1, p1, v1, 1);
        ParameterValue pv2 = new ParameterValue(2, p1, v2, 100);
        ParameterValue pv3 = new ParameterValue(3, p1, v3, 100);
        ParameterValue pv4 = new ParameterValue(4, p1, v4, 100);
        ParameterValue pv5 = new ParameterValue(5, p1, v5, 100);
        ParameterValue pv6 = new ParameterValue(6, p2, v6, 100);
        ParameterValue pv7 = new ParameterValue(7, p2, v7, 100);
        ParameterValue pv8 = new ParameterValue(8, p2, v8, 100);
        ParameterValue pv9 = new ParameterValue(9, p2, v9, 100);
        ParameterValue pv10 = new ParameterValue(10, p2, v10, 100);
        ParameterValue pv11 = new ParameterValue(11, p2, v4, 100);
        ParameterValue pv12 = new ParameterValue(12, p2, v2, 100);
        ParameterValue pv13 = new ParameterValue(13, p2, v1, 100);
        parameterValueList = Arrays.asList(pv1, pv2, pv3, pv4, pv5, pv6, pv7, pv8, pv9, pv10, pv11, pv12, pv13);


        pvMap.put(p1, Arrays.asList(pv1, pv2, pv3, pv4, pv5));
        pvMap.put(p2, Arrays.asList(pv6, pv7, pv8, pv9, pv10, pv11, pv12, pv13));
    }

    @After
    public void after() {
        GlobalContext.removeValue(GlobalContextConstant.PARAMETER_VALUE_LIST);
        GlobalContext.removeValue(GlobalContextConstant.PARAMETER_VALUE_MAP);
    }

    @Test
    public void getAllParamsAllId() throws Exception {
        GenericParameterValueDao parameterValueDao = getParameterValueDao();

        parameterValueService = new ParameterValueServiceImpl(parameterValueDao);
        Assert.assertEquals(parameterValueList, parameterValueService.getParamValueList(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)));
        Assert.assertEquals(parameterValueList, GlobalContext.getValue(GlobalContextConstant.PARAMETER_VALUE_LIST));

        EasyMock.verify(parameterValueDao);
    }

    @Test
    public void getAllParamsSeveralId() {
        GenericParameterValueDao parameterValueDao = getParameterValueDao();
        parameterValueService = new ParameterValueServiceImpl(parameterValueDao);

        ParameterValue pv1 = parameterValueList.get(3);
        ParameterValue pv2 = parameterValueList.get(4);

        Assert.assertEquals(Arrays.asList(pv1, pv2), parameterValueService.getParamValueList(Arrays.asList(pv1.getId(), pv2.getId())));
        EasyMock.verify(parameterValueDao);
    }

    @Test
    public void getAllParamsNoIdInList() {
        GenericParameterValueDao parameterValueDao = getParameterValueDao();
        parameterValueService = new ParameterValueServiceImpl(parameterValueDao);

        ParameterValue pv1 = parameterValueList.get(3);
        ParameterValue pv2 = parameterValueList.get(4);
        ParameterValue pvExtra = new ParameterValue(100, null, null, 100); //null - just stub

        Assert.assertEquals(Arrays.asList(pv1, pv2), parameterValueService.getParamValueList(Arrays.asList(pv1.getId(), pv2.getId(), pvExtra.getId())));
        EasyMock.verify(parameterValueDao);
    }

    @Test
    public void getAllParamsFromCache() throws Exception {
        GlobalContext.addToGlobalContext(GlobalContextConstant.PARAMETER_VALUE_LIST, parameterValueList);
        GenericParameterValueDao parameterValueDao = EasyMock.createMock(GenericParameterValueDao.class);

        EasyMock.expect(parameterValueDao.getAllFullInfo()).andThrow(new AssertionError()).anyTimes();
        EasyMock.replay(parameterValueDao);
        parameterValueService = new ParameterValueServiceImpl(parameterValueDao);
        parameterValueService.getParamValueList(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
        Assert.assertEquals(parameterValueList, parameterValueList);
    }

    @Test(expected = SystemException.class)
    public void getAllParamsReturnNull() throws Exception {
        GenericParameterValueDao dao = EasyMock.createMock(GenericParameterValueDao.class);
        EasyMock.expect(dao.getAllFullInfo()).andReturn(null);
        EasyMock.replay(dao);

        parameterValueService = new ParameterValueServiceImpl(dao);
        parameterValueService.getParamValueList(Arrays.asList(100));
        EasyMock.verify(dao);
    }

    @Test
    public void getParameterValueMapFromCache() throws Exception {
        GlobalContext.addToGlobalContext(GlobalContextConstant.PARAMETER_VALUE_MAP, pvMap);
        GenericParameterValueDao dao = EasyMock.createMock(GenericParameterValueDao.class);
        EasyMock.expect(dao.getAllFullInfo()).andThrow(new AssertionError()).anyTimes();
//        EasyMock.expect(dao.getAllFullInfo()).andReturn(parameterValueList);
        EasyMock.replay(dao);

        parameterValueService = new ParameterValueServiceImpl(dao);
        Assert.assertEquals(pvMap, parameterValueService.getParameterValueMap());
        Assert.assertEquals(pvMap, GlobalContext.getValue(GlobalContextConstant.PARAMETER_VALUE_MAP));
//        EasyMock.verify(dao);
    }

    @Test
    public void getParameterValueMapLoad() throws Exception {
        GenericParameterValueDao dao = EasyMock.createMock(GenericParameterValueDao.class);
        EasyMock.expect(dao.getAllFullInfo()).andReturn(parameterValueList);
        EasyMock.replay(dao);

        parameterValueService = new ParameterValueServiceImpl(dao);
        Assert.assertEquals(pvMap, parameterValueService.getParameterValueMap());
        Assert.assertEquals(pvMap, GlobalContext.getValue(GlobalContextConstant.PARAMETER_VALUE_MAP));
        EasyMock.verify(dao);
    }

    @Test(expected = SystemException.class)
    public void getParameterValueMapDaoReturnsNull() throws Exception {
        GenericParameterValueDao dao = EasyMock.createMock(GenericParameterValueDao.class);
        EasyMock.expect(dao.getAllFullInfo()).andReturn(null).andThrow(new AssertionError()).times(2, 100000);
        EasyMock.replay(dao);

        parameterValueService = new ParameterValueServiceImpl(dao);
        Assert.assertEquals(pvMap, parameterValueService.getParameterValueMap());
    }

    private GenericParameterValueDao getParameterValueDao() {
        GenericParameterValueDao parameterValueDao = EasyMock.createMock(GenericParameterValueDao.class);
        EasyMock.expect(parameterValueDao.getAllFullInfo()).andReturn(parameterValueList);
        EasyMock.replay(parameterValueDao);

        return parameterValueDao;
    }
}