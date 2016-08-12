package main.java.com.hotelSystem.model.service;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.dao.AbstractParameterValueDao;
import main.java.com.hotelSystem.exception.DaoException;
import main.java.com.hotelSystem.exception.SystemException;
import main.java.com.hotelSystem.model.roomParameter.Parameter;
import main.java.com.hotelSystem.model.roomParameter.ParameterValueTuple;
import main.java.com.hotelSystem.model.roomParameter.Value;
import main.java.com.hotelSystem.service.IParameterValueService;
import main.java.com.hotelSystem.service.serviceImpl.ParameterValueServiceImpl;
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
public class AbstractParameterValueTupleServiceTest {

    private static IParameterValueService parameterValueService;


    private static Map<Parameter, List<ParameterValueTuple>> pvMap = new HashMap<>();

    private static List<ParameterValueTuple> parameterValueTupleList;

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

        ParameterValueTuple pv1 = new ParameterValueTuple(1, p1, v1, 1);
        ParameterValueTuple pv2 = new ParameterValueTuple(2, p1, v2, 100);
        ParameterValueTuple pv3 = new ParameterValueTuple(3, p1, v3, 100);
        ParameterValueTuple pv4 = new ParameterValueTuple(4, p1, v4, 100);
        ParameterValueTuple pv5 = new ParameterValueTuple(5, p1, v5, 100);
        ParameterValueTuple pv6 = new ParameterValueTuple(6, p2, v6, 100);
        ParameterValueTuple pv7 = new ParameterValueTuple(7, p2, v7, 100);
        ParameterValueTuple pv8 = new ParameterValueTuple(8, p2, v8, 100);
        ParameterValueTuple pv9 = new ParameterValueTuple(9, p2, v9, 100);
        ParameterValueTuple pv10 = new ParameterValueTuple(10, p2, v10, 100);
        ParameterValueTuple pv11 = new ParameterValueTuple(11, p2, v4, 100);
        ParameterValueTuple pv12 = new ParameterValueTuple(12, p2, v2, 100);
        ParameterValueTuple pv13 = new ParameterValueTuple(13, p2, v1, 100);
        parameterValueTupleList = Arrays.asList(pv1, pv2, pv3, pv4, pv5, pv6, pv7, pv8, pv9, pv10, pv11, pv12, pv13);


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
        AbstractParameterValueDao parameterValueDao = getParameterValueDao();

        parameterValueService = new ParameterValueServiceImpl(parameterValueDao);
        Assert.assertEquals(parameterValueTupleList, parameterValueService.getParamValueList(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)));
        Assert.assertEquals(parameterValueTupleList, GlobalContext.getValue(GlobalContextConstant.PARAMETER_VALUE_LIST));

        EasyMock.verify(parameterValueDao);
    }

    @Test
    public void getAllParamsSeveralId() {
        AbstractParameterValueDao parameterValueDao = getParameterValueDao();
        parameterValueService = new ParameterValueServiceImpl(parameterValueDao);

        ParameterValueTuple pv1 = parameterValueTupleList.get(3);
        ParameterValueTuple pv2 = parameterValueTupleList.get(4);

        Assert.assertEquals(Arrays.asList(pv1, pv2), parameterValueService.getParamValueList(Arrays.asList(pv1.getId(), pv2.getId())));
        EasyMock.verify(parameterValueDao);
    }

    @Test
    public void getAllParamsNoIdInList() {
        AbstractParameterValueDao parameterValueDao = getParameterValueDao();
        parameterValueService = new ParameterValueServiceImpl(parameterValueDao);

        ParameterValueTuple pv1 = parameterValueTupleList.get(3);
        ParameterValueTuple pv2 = parameterValueTupleList.get(4);
        ParameterValueTuple pvExtra = new ParameterValueTuple(100, null, null, 100); //null - just stub

        Assert.assertEquals(Arrays.asList(pv1, pv2), parameterValueService.getParamValueList(Arrays.asList(pv1.getId(), pv2.getId(), pvExtra.getId())));
        EasyMock.verify(parameterValueDao);
    }

    @Test
    public void getAllParamsFromCache() throws Exception {
        GlobalContext.addToGlobalContext(GlobalContextConstant.PARAMETER_VALUE_LIST, parameterValueTupleList);
        AbstractParameterValueDao parameterValueDao = EasyMock.createMock(AbstractParameterValueDao.class);

        EasyMock.expect(parameterValueDao.getAllFullInfo()).andThrow(new AssertionError()).anyTimes();
        EasyMock.replay(parameterValueDao);
        parameterValueService = new ParameterValueServiceImpl(parameterValueDao);
        parameterValueService.getParamValueList(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13));
        Assert.assertEquals(parameterValueTupleList, parameterValueTupleList);
    }

    @Test(expected = SystemException.class)
    public void getAllParamsReturnNull() throws Exception {
        AbstractParameterValueDao dao = EasyMock.createMock(AbstractParameterValueDao.class);
        EasyMock.expect(dao.getAllFullInfo()).andThrow(new SystemException(null, null));
        EasyMock.replay(dao);

        parameterValueService = new ParameterValueServiceImpl(dao);
        parameterValueService.getParamValueList(Arrays.asList(100));
        EasyMock.verify(dao);
    }

    @Test
    public void getParameterValueMapFromCache() throws Exception {
        GlobalContext.addToGlobalContext(GlobalContextConstant.PARAMETER_VALUE_MAP, pvMap);
        AbstractParameterValueDao dao = EasyMock.createMock(AbstractParameterValueDao.class);
        EasyMock.expect(dao.getAllFullInfo()).andThrow(new AssertionError()).anyTimes();
//        EasyMock.expect(dao.getAllFullInfo()).andReturn(parameterValueTupleList);
        EasyMock.replay(dao);

        parameterValueService = new ParameterValueServiceImpl(dao);
        Assert.assertEquals(pvMap, parameterValueService.getParameterValueMap());
        Assert.assertEquals(pvMap, GlobalContext.getValue(GlobalContextConstant.PARAMETER_VALUE_MAP));
//        EasyMock.verify(dao);
    }

    @Test
    public void getParameterValueMapLoad() throws Exception {
        AbstractParameterValueDao dao = EasyMock.createMock(AbstractParameterValueDao.class);
        EasyMock.expect(dao.getAllFullInfo()).andReturn(parameterValueTupleList);
        EasyMock.replay(dao);

        parameterValueService = new ParameterValueServiceImpl(dao);
        Assert.assertEquals(pvMap, parameterValueService.getParameterValueMap());
        Assert.assertEquals(pvMap, GlobalContext.getValue(GlobalContextConstant.PARAMETER_VALUE_MAP));
        EasyMock.verify(dao);
    }

    @Test(expected = SystemException.class)
    public void getParameterValueMapDaoReturnsNull() throws Exception {
        AbstractParameterValueDao dao = EasyMock.createMock(AbstractParameterValueDao.class);
        EasyMock.expect(dao.getAllFullInfo()).andThrow(new SystemException(null, null)).andThrow(new AssertionError()).times(2, 100000);
        EasyMock.replay(dao);

        parameterValueService = new ParameterValueServiceImpl(dao);
        dao.getAllFullInfo();
    }

    private AbstractParameterValueDao getParameterValueDao() {
        AbstractParameterValueDao parameterValueDao = EasyMock.createMock(AbstractParameterValueDao.class);
        try {
            EasyMock.expect(parameterValueDao.getAllFullInfo()).andReturn(parameterValueTupleList);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        EasyMock.replay(parameterValueDao);

        return parameterValueDao;
    }
}