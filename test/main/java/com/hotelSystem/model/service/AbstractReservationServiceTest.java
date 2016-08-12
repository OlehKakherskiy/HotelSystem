package main.java.com.hotelSystem.model.service;

import main.java.com.hotelSystem.dao.AbstractReservationDao;
import main.java.com.hotelSystem.exception.RequestException;
import main.java.com.hotelSystem.exception.SystemException;
import main.java.com.hotelSystem.model.HotelRoom;
import main.java.com.hotelSystem.model.MobilePhone;
import main.java.com.hotelSystem.model.Reservation;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.model.enums.ReservationStatus;
import main.java.com.hotelSystem.model.enums.UserType;
import main.java.com.hotelSystem.service.IParameterValueService;
import main.java.com.hotelSystem.service.IReservationService;
import main.java.com.hotelSystem.service.serviceImpl.ReservationService;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class AbstractReservationServiceTest {

    private static AbstractReservationDao reservationDao;

    private static IParameterValueService parameterValueService;

    private static IReservationService reservationService;

    private static User userStub;

    private static User adminStub;

    @BeforeClass
    public static void beforeClass() throws Exception {
        userStub = new User(1, "name", "surname", UserType.REGISTERED_USER, new ArrayList() {{
            add(new MobilePhone(1, "stub"));
        }});

        adminStub = new User(1, "name", "surname", UserType.ADMIN, new ArrayList() {{
            add(new MobilePhone(1, "stub"));
        }});
    }

    @Test
    public void getShortInfoAboutAllReservationsWithUserParam() throws Exception {
        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.getAllUserReservationsShortInfo(userStub.getIdUser(), ReservationStatus.ALL))
                .andReturn(Collections.emptyList()); //no user with id = 1 or user doesn't have target reservations
        EasyMock.replay(reservationDao);
        reservationService = new ReservationService(reservationDao, parameterValueService);
        Assert.assertArrayEquals(Collections.emptyList().toArray(),
                reservationService.getShortInfoAboutAllReservations(userStub, ReservationStatus.ALL).toArray());
        EasyMock.verify(reservationDao);
    }

    @Test(expected = NullPointerException.class)
    public void getShortInfoAboutAllReservationsNullUser() throws Exception {
        reservationService = new ReservationService(reservationDao, parameterValueService);
        reservationService.getShortInfoAboutAllReservations(null, ReservationStatus.ALL);
    }

    @Test
    public void getShortInfoAboutAllReservationsForAdmin() throws Exception {
        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.getAllReservationsShortInfo(ReservationStatus.ALL))
                .andReturn(Collections.emptyList());
        EasyMock.replay(reservationDao);
        reservationService = new ReservationService(reservationDao, parameterValueService);
        Assert.assertArrayEquals(Collections.emptyList().toArray(),
                reservationService.getShortInfoAboutAllReservations(adminStub, ReservationStatus.ALL).toArray());
        EasyMock.verify(reservationDao);
    }

    @Test
    public void getShortInfoAboutAllReservations() throws Exception {
        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.getAllReservationsShortInfo(ReservationStatus.ALL))
                .andReturn(Collections.emptyList());
        EasyMock.replay(reservationDao);
        reservationService = new ReservationService(reservationDao, parameterValueService);
        Assert.assertArrayEquals(Collections.emptyList().toArray(),
                reservationService.getShortInfoAboutAllReservations(ReservationStatus.ALL).toArray());
        EasyMock.verify(reservationDao);
    }

    @Test
    public void getReservationDetailInfo() throws Exception {

        HotelRoom roomStub = new HotelRoom(1, "101", true, new ArrayList<>(), 2);

        Reservation expected = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(), ReservationStatus.PROCESSING,
                roomStub.getRoomId(), userStub.getIdUser(), "comment", new ArrayList<>());
        expected.setHotelRoomId(roomStub.getRoomId());
        expected.setRequestParametersIds(new ArrayList<>());
        expected.setUserId(userStub.getIdUser());

        parameterValueService = EasyMock.createMock(IParameterValueService.class);
        EasyMock.expect(parameterValueService.getParamValueList(new ArrayList<>())).andReturn(new ArrayList<>());

        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.read(expected.getId())).andReturn(new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.PROCESSING, roomStub.getRoomId(), userStub.getIdUser(), "comment", new ArrayList<>()));

        EasyMock.replay(parameterValueService, reservationDao);

        reservationService = new ReservationService(reservationDao, parameterValueService);
        Assert.assertEquals(expected, reservationService.getReservationDetailInfo(expected.getId()));

        EasyMock.verify(parameterValueService, reservationDao);
    }

    @Test(expected = RequestException.class)
    public void getReservationDetailInfoRequestExceptionCheck() throws Exception {
        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.read(500)).andReturn(null).times(1);
        EasyMock.replay(reservationDao);
        reservationService = new ReservationService(reservationDao, null);
        reservationService.getReservationDetailInfo(500);
    }

    @Test
    public void offerHotelRoom() throws Exception {
        HotelRoom roomStub = new HotelRoom(1, "101", true, new ArrayList<>(), 2);
        Reservation expected = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.WAITING_FOR_ANSWER, roomStub.getRoomId(), userStub.getIdUser(), "comment",
                Collections.emptyList());
        expected.setHotelRoom(roomStub);
        expected.setUser(userStub);
        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.update(expected)).andReturn(true);
        EasyMock.replay(reservationDao);

        reservationService = new ReservationService(reservationDao, null);
        Reservation actual = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.PROCESSING, 0, userStub.getIdUser(), "comment",
                Collections.emptyList());
        actual.setUser(userStub);
        reservationService.offerHotelRoom(actual, roomStub);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected.getHotelRoom(), actual.getHotelRoom());
        EasyMock.verify(reservationDao);
    }

    @Test(expected = SystemException.class)
    public void offerHotelRoomSystemException() throws Exception {
        HotelRoom roomStub = new HotelRoom(1, "101", true, new ArrayList<>(), 2);
        Reservation expected = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.WAITING_FOR_ANSWER, roomStub.getRoomId(), userStub.getIdUser(), "comment",
                Collections.emptyList());
        expected.setHotelRoom(roomStub);
        expected.setUser(userStub);
        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.update(expected)).andReturn(false);
        EasyMock.replay(reservationDao);

        reservationService = new ReservationService(reservationDao, null);
        Reservation actual = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.PROCESSING, -1, userStub.getIdUser(), "comment",
                Collections.emptyList());
        actual.setUser(userStub);
        try {
            reservationService.offerHotelRoom(actual, roomStub);
        } finally {
            Assert.assertNull(actual.getHotelRoom());
            Assert.assertEquals(ReservationStatus.PROCESSING, actual.getStatus());
            Assert.assertEquals(-1, actual.getHotelRoomId());
            EasyMock.verify(reservationDao);
        }
    }

    @Test
    public void refuseReservationOffer() throws Exception {
        HotelRoom roomStub = new HotelRoom(1, "101", true, new ArrayList<>(), 2);

        Reservation expected = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.PROCESSING, -1, userStub.getIdUser(), "comment",
                Collections.emptyList());
        expected.setHotelRoom(null);
        expected.setUser(userStub);

        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.update(expected)).andReturn(true);
        EasyMock.replay(reservationDao);


        Reservation actual = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.WAITING_FOR_ANSWER, roomStub.getRoomId(), userStub.getIdUser(), "comment",
                Collections.emptyList());
        actual.setUser(userStub);
        actual.setHotelRoom(roomStub);

        reservationService = new ReservationService(reservationDao, null);
        reservationService.refuseReservationOffer(actual);
        Assert.assertEquals(expected, actual);
        Assert.assertNull(actual.getHotelRoom());
        Assert.assertEquals(-1, actual.getHotelRoomId());
        EasyMock.verify(reservationDao);
    }

    @Test(expected = SystemException.class)
    public void refuseReservationOfferSystemException() throws Exception {
        HotelRoom roomStub = new HotelRoom(1, "101", true, new ArrayList<>(), 2);

        Reservation expected = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.WAITING_FOR_ANSWER, roomStub.getRoomId(), userStub.getIdUser(), "comment",
                Collections.emptyList());
        expected.setHotelRoom(roomStub);
        expected.setUser(userStub);

        Reservation updatedEntity = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.PROCESSING, -1, userStub.getIdUser(), "comment",
                Collections.emptyList());
        updatedEntity.setHotelRoom(null);
        updatedEntity.setUser(userStub);

        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.update(updatedEntity)).andReturn(false);
        EasyMock.replay(reservationDao);


        Reservation actual = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.WAITING_FOR_ANSWER, roomStub.getRoomId(), userStub.getIdUser(), "comment",
                Collections.emptyList());
        actual.setUser(userStub);
        actual.setHotelRoom(roomStub);

        reservationService = new ReservationService(reservationDao, null);
        try {
            reservationService.refuseReservationOffer(actual);
        } finally {
            Assert.assertEquals(expected.getHotelRoomId(), actual.getHotelRoomId());
            Assert.assertEquals(expected.getHotelRoom(), actual.getHotelRoom());
            Assert.assertEquals(expected.getUser(), actual.getUser());
            EasyMock.verify(reservationDao);
        }
    }

    @Test
    public void submitReservationOffer() throws Exception {
        HotelRoom roomStub = new HotelRoom(1, "101", true, new ArrayList<>(), 2);

        Reservation expected = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.SUBMITTED, roomStub.getRoomId(), userStub.getIdUser(), "comment",
                Collections.emptyList());
        expected.setHotelRoom(roomStub);
        expected.setUser(userStub);

        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.update(expected)).andReturn(true);
        EasyMock.replay(reservationDao);


        Reservation actual = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.WAITING_FOR_ANSWER, roomStub.getRoomId(), userStub.getIdUser(), "comment",
                Collections.emptyList());
        actual.setUser(userStub);
        actual.setHotelRoom(roomStub);

        reservationService = new ReservationService(reservationDao, null);
        reservationService.submitReservationOffer(actual);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(roomStub, actual.getHotelRoom());
        Assert.assertEquals(roomStub.getRoomId(), actual.getHotelRoomId());
        EasyMock.verify(reservationDao);
    }

    @Test(expected = SystemException.class)
    public void submitReservationOfferSystemException() throws Exception {
        HotelRoom roomStub = new HotelRoom(1, "101", true, new ArrayList<>(), 2);

        Reservation expected = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.WAITING_FOR_ANSWER, roomStub.getRoomId(), userStub.getIdUser(), "comment",
                Collections.emptyList());
        expected.setHotelRoom(roomStub);
        expected.setUser(userStub);

        Reservation updatedEntity = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.SUBMITTED, roomStub.getRoomId(), userStub.getIdUser(), "comment",
                Collections.emptyList());
        updatedEntity.setHotelRoom(roomStub);
        updatedEntity.setUser(userStub);

        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.update(updatedEntity)).andReturn(false);
        EasyMock.replay(reservationDao);


        Reservation actual = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.WAITING_FOR_ANSWER, roomStub.getRoomId(), userStub.getIdUser(), "comment",
                Collections.emptyList());
        actual.setUser(userStub);
        actual.setHotelRoom(roomStub);

        reservationService = new ReservationService(reservationDao, null);
        try {
            reservationService.submitReservationOffer(actual);
        } finally {
            Assert.assertEquals(expected.getHotelRoomId(), actual.getHotelRoomId());
            Assert.assertEquals(expected.getHotelRoom(), actual.getHotelRoom());
            Assert.assertEquals(expected.getUser(), actual.getUser());
            EasyMock.verify(reservationDao);
        }
    }

    @Test
    public void refuseReservationProcessing() throws Exception {

        Reservation expected = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.REFUSED_FROM_PROCESSING, -1, userStub.getIdUser(), "comment",
                Collections.emptyList());
        expected.setHotelRoom(null);
        expected.setUser(userStub);

        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.update(expected)).andReturn(true);
        EasyMock.replay(reservationDao);


        Reservation actual = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.PROCESSING, -1, userStub.getIdUser(), "comment",
                Collections.emptyList());
        actual.setUser(userStub);
        actual.setHotelRoom(null);

        reservationService = new ReservationService(reservationDao, null);
        reservationService.refuseReservationProcessing(actual);
        Assert.assertEquals(expected, actual);
        Assert.assertNull(actual.getHotelRoom());
        Assert.assertEquals(-1, actual.getHotelRoomId());
        EasyMock.verify(reservationDao);
    }

    @Test(expected = SystemException.class)
    public void refuseHotelRoomProcessingSystemException() throws Exception {

        Reservation expected = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.PROCESSING, -1, userStub.getIdUser(), "comment",
                Collections.emptyList());
        expected.setHotelRoom(null);
        expected.setUser(userStub);

        Reservation changed = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.REFUSED_FROM_PROCESSING, -1, userStub.getIdUser(), "comment",
                Collections.emptyList());
        changed.setUser(userStub);
        changed.setHotelRoom(null);

        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.update(changed)).andReturn(false);
        EasyMock.replay(reservationDao);


        Reservation actual = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.PROCESSING, -1, userStub.getIdUser(), "comment",
                Collections.emptyList());
        actual.setUser(userStub);
        actual.setHotelRoom(null);

        reservationService = new ReservationService(reservationDao, null);
        reservationService.refuseReservationProcessing(actual);
        Assert.assertEquals(expected, actual);
        Assert.assertNull(actual.getHotelRoom());
        Assert.assertEquals(-1, actual.getHotelRoomId());
        EasyMock.verify(reservationDao);
    }

    @Test
    public void addReservation() throws Exception {
        Reservation expected = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.PROCESSING, -1, userStub.getIdUser(), "comment",
                Collections.emptyList());
        expected.setHotelRoom(null);
        expected.setUser(userStub);
        expected.setUserId(userStub.getIdUser());

        Reservation actual = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.PROCESSING, -1, userStub.getIdUser(), "comment",
                Collections.emptyList());
        actual.setHotelRoom(null);

        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        reservationDao.save(actual);
        EasyMock.expectLastCall();
        EasyMock.replay(reservationDao);

        reservationService = new ReservationService(reservationDao, null);
        reservationService.addReservation(actual, userStub);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected.getUser(), actual.getUser());
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        EasyMock.verify(reservationDao);
    }

    @Test(expected = SystemException.class)
    public void addReservationSystemException() throws Exception {
        Reservation expected = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.PROCESSING, -1, 0, "comment",
                Collections.emptyList());
        expected.setHotelRoom(null);
        expected.setUser(userStub);
        expected.setUserId(userStub.getIdUser());

        Reservation actual = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(),
                ReservationStatus.PROCESSING, -1, userStub.getIdUser(), "comment",
                Collections.emptyList());
        actual.setHotelRoom(null);

        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        reservationDao.save(actual);
        EasyMock.expectLastCall().andThrow(new SystemException(null));
        EasyMock.replay(reservationDao);

        reservationService = new ReservationService(reservationDao, null);
        reservationService.addReservation(actual, userStub);
        Assert.assertEquals(expected, actual);
        Assert.assertNull(actual.getUser());
        Assert.assertEquals(-1, actual.getUserId());
        EasyMock.verify(reservationDao);
    }

    @Test
    public void deleteReservation() throws Exception {
        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.delete(1)).andReturn(true);
        EasyMock.replay(reservationDao);

        reservationService = new ReservationService(reservationDao, null);
        reservationService.deleteReservation(1);

        EasyMock.verify(reservationDao);

    }

    @Test(expected = RequestException.class)
    public void deleteReservationInvalidId() throws Exception {
        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.delete(1)).andReturn(false);
        EasyMock.replay(reservationDao);

        reservationService = new ReservationService(reservationDao, null);
        try {
            reservationService.deleteReservation(1);
        } finally {
            EasyMock.verify(reservationDao);
        }
    }

    @Test(expected = SystemException.class)
    public void deleteReservationSystemException() throws Exception {
        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.delete(1)).andThrow(new SystemException(null));
        EasyMock.replay(reservationDao);

        reservationService = new ReservationService(reservationDao, null);
        try {
            reservationService.deleteReservation(1);
        } finally {
            EasyMock.verify(reservationDao);
        }
    }
}