package main.java.com.epam.project4.model.service;

import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.exception.RequestException;
import main.java.com.epam.project4.exception.SystemException;
import main.java.com.epam.project4.model.dao.AbstractHotelRoomDao;
import main.java.com.epam.project4.model.dao.AbstractReservationDao;
import main.java.com.epam.project4.model.entity.HotelRoom;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.roomParameter.Parameter;
import main.java.com.epam.project4.model.entity.roomParameter.ParameterValueTuple;
import main.java.com.epam.project4.model.entity.roomParameter.Value;
import main.java.com.epam.project4.model.service.serviceImpl.HotelRoomService;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class IHotelRoomServiceTest {

    private AbstractHotelRoomDao hotelRoomDao;

    private IParameterValueService parameterValueService;

    private AbstractReservationDao reservationDao;

    private IHotelRoomService hotelRoomService;

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
    }

    @Test
    public void getAllRoomFullDetails() throws Exception {
        hotelRoomDao = EasyMock.createMock(AbstractHotelRoomDao.class);
        EasyMock.expect(hotelRoomDao.getAllFullDetails(true)).andReturn(getActualList());
        parameterValueService = EasyMock.createMock(IParameterValueService.class);
        EasyMock.expect(parameterValueService.getParamValueList(Arrays.asList(1, 10)))
                .andReturn(Arrays.asList(parameterValueTupleList.get(0), parameterValueTupleList.get(9)));
        EasyMock.expect(parameterValueService.getParamValueList(Arrays.asList(2, 11)))
                .andReturn(Arrays.asList(parameterValueTupleList.get(1), parameterValueTupleList.get(10)));
        EasyMock.expect(parameterValueService.getParamValueList(Arrays.asList(3, 12)))
                .andReturn(Arrays.asList(parameterValueTupleList.get(2), parameterValueTupleList.get(11)));
        EasyMock.replay(hotelRoomDao, parameterValueService);

        hotelRoomService = new HotelRoomService(hotelRoomDao, null, parameterValueService);
        checkEquality(getExpectedList(), hotelRoomService.getAllRoomFullDetails(true));
        EasyMock.verify(hotelRoomDao, parameterValueService);
    }

    private List<HotelRoom> getActualList() {
        List<HotelRoom> actualList = new ArrayList<>();
        actualList.add(new HotelRoom(1, "room1", true, Arrays.asList(1, 10), 0));
        actualList.add(new HotelRoom(2, "room2", true, Arrays.asList(2, 11), 0));
        actualList.add(new HotelRoom(3, "room3", true, Arrays.asList(3, 12), 0));
        return actualList;
    }

    private List<HotelRoom> getExpectedList() {
        List<HotelRoom> result = new ArrayList<>();
        result.add(new HotelRoom(1, "room1", true, Arrays.asList(1, 10), 101));
        result.add(new HotelRoom(2, "room2", true, Arrays.asList(2, 11), 200));
        result.add(new HotelRoom(3, "room3", true, Arrays.asList(3, 12), 200));
        result.get(0).setParameters(Arrays.asList(parameterValueTupleList.get(0), parameterValueTupleList.get(9)));
        result.get(1).setParameters(Arrays.asList(parameterValueTupleList.get(1), parameterValueTupleList.get(10)));
        result.get(2).setParameters(Arrays.asList(parameterValueTupleList.get(2), parameterValueTupleList.get(11)));
        return result;
    }

    private void checkEquality(List<HotelRoom> expected, List<HotelRoom> actual) {
        Assert.assertArrayEquals(expected.toArray(), actual.toArray());
        for (int i = 0; i < expected.size(); i++) {
            HotelRoom currentExpected = expected.get(i);
            HotelRoom currentActual = actual.get(i);

            Assert.assertArrayEquals(currentExpected.getParameters().toArray(), currentActual.getParameters().toArray());
            Assert.assertEquals(currentExpected.getPrice(), currentActual.getPrice());
        }
    }

    @Test(expected = SystemException.class)
    public void getAllRoomFullDetailsSystemException() throws Exception {
        hotelRoomDao = EasyMock.createMock(AbstractHotelRoomDao.class);
        EasyMock.expect(hotelRoomDao.getAllFullDetails(true)).andThrow(new DaoException());

        parameterValueService = EasyMock.createMock(IParameterValueService.class);
        EasyMock.expect(parameterValueService.getParamValueList(Arrays.asList(1, 10))).andThrow(new AssertionError());
        EasyMock.replay(hotelRoomDao, parameterValueService);
        hotelRoomService = new HotelRoomService(hotelRoomDao, null, parameterValueService);
        hotelRoomService.getAllRoomFullDetails(true);
    }

    @Test
    public void getFullDetails() throws Exception {

        List<ParameterValueTuple> roomParams = new ArrayList<>();
        roomParams.add(parameterValueTupleList.get(0));
        roomParams.add(parameterValueTupleList.get(9));
        HotelRoom expected = new HotelRoom(1, "name", true, Arrays.asList(1, 10), 101);
        expected.setParameters(roomParams);

        hotelRoomDao = EasyMock.createMock(AbstractHotelRoomDao.class);
        EasyMock.expect(hotelRoomDao.read(1)).andReturn(new HotelRoom(1, "name", true, Arrays.asList(1, 10), 0));

        parameterValueService = EasyMock.createMock(IParameterValueService.class);
        EasyMock.expect(parameterValueService.getParamValueList(Arrays.asList(1, 10))).andReturn(roomParams);
        EasyMock.replay(hotelRoomDao, parameterValueService);

        hotelRoomService = new HotelRoomService(hotelRoomDao, null, parameterValueService);
        HotelRoom actual = hotelRoomService.getFullDetails(1);
        Assert.assertEquals(expected, actual);
        Assert.assertArrayEquals(roomParams.toArray(), actual.getParameters().toArray());
        EasyMock.verify(hotelRoomDao, parameterValueService);
    }

    @Test(expected = RequestException.class)
    public void getFullDetailsNoIdIsSystem() throws Exception {
        hotelRoomDao = EasyMock.createMock(AbstractHotelRoomDao.class);
        EasyMock.expect(hotelRoomDao.read(1)).andReturn(null);
        EasyMock.replay(hotelRoomDao);
        hotelRoomService = new HotelRoomService(hotelRoomDao, reservationDao, parameterValueService);
        try {
            hotelRoomService.getFullDetails(1);
        } finally {
            EasyMock.verify(hotelRoomDao);
        }
    }

    @Test(expected = RequestException.class)
    public void getFullDetailsInvalidId() throws Exception {
        hotelRoomDao = EasyMock.createMock(AbstractHotelRoomDao.class);
        EasyMock.expect(hotelRoomDao.read(-1)).andThrow(new AssertionError());
        EasyMock.replay(hotelRoomDao);
        hotelRoomService = new HotelRoomService(hotelRoomDao, reservationDao, parameterValueService);
        hotelRoomService.getFullDetails(-1);
    }

    @Test
    public void appendReservations() throws Exception {
        Month month = LocalDate.now().getMonth();
        int year = LocalDate.now().getYear();
        LocalDate startDate = LocalDate.of(year, month.getValue(), 1);
        LocalDate endMonthDate = startDate.with(lastDayOfMonth());
        HotelRoom target = new HotelRoom(1, "name", true, Arrays.asList(1, 10), 101);


        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.getAllRoomReservationsInPeriod(1, ReservationStatus.ALL, startDate, endMonthDate))
                .andReturn(Collections.emptyList());
        EasyMock.replay(reservationDao);

        hotelRoomService = new HotelRoomService(null, reservationDao, null);
        hotelRoomService.appendReservations(target, LocalDate.now().getMonth(), Year.now(), ReservationStatus.ALL);
        Assert.assertEquals(0, target.getReservationList().size());
        EasyMock.verify(reservationDao);
    }

    @Test(expected = SystemException.class)
    public void appendReservationsSystemException() throws Exception {
        Month month = LocalDate.now().getMonth();
        int year = LocalDate.now().getYear();
        LocalDate startDate = LocalDate.of(year, month.getValue(), 1);
        LocalDate endMonthDate = startDate.with(lastDayOfMonth());
        HotelRoom target = new HotelRoom(1, "name", true, Arrays.asList(1, 10), 101);


        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.getAllRoomReservationsInPeriod(1, ReservationStatus.ALL, startDate, endMonthDate))
                .andThrow(new DaoException(""));
        EasyMock.replay(reservationDao);

        hotelRoomService = new HotelRoomService(null, reservationDao, null);
        try {
            hotelRoomService.appendReservations(target, LocalDate.now().getMonth(), Year.now(), ReservationStatus.ALL);
        } finally {
            EasyMock.verify(reservationDao);
        }
    }

}