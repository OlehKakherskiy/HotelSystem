package main.java.com.epam.project4.model.service.serviceImpl;

import main.java.com.epam.project4.model.dao.AbstractHotelRoomDao;
import main.java.com.epam.project4.model.dao.AbstractReservationDao;
import main.java.com.epam.project4.model.entity.HotelRoom;
import main.java.com.epam.project4.model.entity.MobilePhone;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.enums.UserType;
import main.java.com.epam.project4.model.service.AbstractParameterValueService;
import main.java.com.epam.project4.model.service.AbstractReservationService;
import main.java.com.epam.project4.model.service.AbstractUserService;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class ReservationServiceTest {

    private static AbstractReservationDao reservationDao;

    private static AbstractHotelRoomDao hotelRoomDao;

    private static AbstractParameterValueService parameterValueService;

    private static AbstractUserService userService;

    private static AbstractReservationService reservationService;

    @Test
    public void getShortInfoAboutAllReservations() throws Exception {

    }

    @Test
    public void getShortInfoAboutAllReservationsForAdminInPeriod() throws Exception {

    }

    @Test
    public void getReservationDetailInfo_forRegUser() throws Exception {
        User userStub = new User(1, "name", "surname", UserType.REGISTERED_USER, new ArrayList() {{
            add(new MobilePhone(1, "stub"));
        }});
        User adminStub = new User(1, "name", "surname", UserType.ADMIN, new ArrayList() {{
            add(new MobilePhone(1, "stub"));
        }});

        HotelRoom roomStub = new HotelRoom(1, "101", true, new ArrayList<>(), 2);

        Reservation expected = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDate.now(), ReservationStatus.PROCESSING, roomStub, userStub, "comment", new ArrayList<>());
        expected.setHotelRoomID(roomStub.getRoomID());
        expected.setRequestParametersIds(new ArrayList<>());
        expected.setUserID(userStub.getIdUser());

        parameterValueService = EasyMock.createMock(AbstractParameterValueService.class);
        EasyMock.expect(parameterValueService.getParamValueList(new ArrayList<>())).andReturn(new ArrayList<>());
        reservationDao = EasyMock.createMock(AbstractReservationDao.class);
        EasyMock.expect(reservationDao.read(expected.getId())).andReturn(expected);

        EasyMock.replay(parameterValueService, reservationDao);

        reservationService = new ReservationService(reservationDao, hotelRoomDao, userService, parameterValueService);
        Assert.assertEquals(expected, reservationService.getReservationDetailInfo(expected.getId()));

        EasyMock.verify(parameterValueService, reservationDao);

        hotelRoomDao = EasyMock.createMock(AbstractHotelRoomDao.class);
        EasyMock.expect(hotelRoomDao.read(roomStub.getRoomID())).andReturn(roomStub);

        userService = EasyMock.createMock(UserService.class);
        EasyMock.expect(userService.getUserInfo(userStub.getIdUser())).andReturn(userStub);
        EasyMock.replay(hotelRoomDao, userService);
        EasyMock.reset(reservationDao, parameterValueService);


        reservationService = new ReservationService(reservationDao, hotelRoomDao, userService, parameterValueService);
        Assert.assertEquals(expected, reservationService.getReservationDetailInfo(expected.getId()));

        EasyMock.verify(hotelRoomDao, userService, reservationDao, parameterValueService);
    }

    @Test
    public void getReservationDetailInfo_forAdmin() throws Exception {

    }

    @Test
    public void offerHotelRoom() throws Exception {

    }

    @Test
    public void setStatusToRefused() throws Exception {

    }

    @Test
    public void setStatusToSubmitted() throws Exception {

    }

    @Test
    public void addReservation() throws Exception {

    }

}