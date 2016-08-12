package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.app.constants.WebPageConstant;
import main.java.com.hotelSystem.controller.command.ICommand;
import main.java.com.hotelSystem.manager.AbstractCommandManager;
import main.java.com.hotelSystem.manager.AbstractServiceManager;
import main.java.com.hotelSystem.model.HotelRoom;
import main.java.com.hotelSystem.model.Reservation;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.model.enums.ReservationStatus;
import main.java.com.hotelSystem.service.IReservationService;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Collections;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class OfferHotelRoomCommandTest {

    @Test
    public void process() throws Exception {
        LogManager.getRootLogger().setLevel(Level.OFF);
        Reservation reservationStub = new Reservation(1, LocalDate.now().plusDays(5), LocalDate.now().plusDays(10),
                LocalDate.now(), ReservationStatus.PROCESSING, 0, 0, "comment", Collections.emptyList());
        HotelRoom hotelRoomStub = new HotelRoom(1, "name", true, Collections.emptyList(), 1);
        HttpSession session = EasyMock.createMock(HttpSession.class);

        EasyMock.expect(session.getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName()))
                .andReturn(reservationStub);
        EasyMock.expect(session.getAttribute(GlobalContextConstant.CURRENT_HOTEL_ROOM.getName()))
                .andReturn(hotelRoomStub);
        EasyMock.expect(session.getAttribute(GlobalContextConstant.USER.getName()))
                .andReturn(new User());

        session.removeAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName());
        EasyMock.expectLastCall();
        session.removeAttribute(GlobalContextConstant.CURRENT_HOTEL_ROOM.getName());
        EasyMock.expectLastCall();

        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getSession(false)).andReturn(session);

        IReservationService reservationService = EasyMock.createMock(IReservationService.class);
        reservationService.offerHotelRoom(reservationStub, hotelRoomStub);
        EasyMock.expectLastCall();

        AbstractServiceManager serviceManager = EasyMock.createMock(AbstractServiceManager.class);
        EasyMock.expect(serviceManager.getInstance(IReservationService.class)).andReturn(reservationService);


        ICommand offerHotelRoomCommand = new OfferHotelRoomCommand();
        offerHotelRoomCommand.setServiceManager(serviceManager);


        ICommand getReservationListCommand = EasyMock.createMock(GetReservationListCommand.class);
        EasyMock.expect(getReservationListCommand.process(request, null)).andReturn(WebPageConstant.INDEX.getPath());

        AbstractCommandManager commandManager = EasyMock.createMock(AbstractCommandManager.class);
        EasyMock.expect(commandManager.getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND))
                .andReturn(getReservationListCommand);

        GlobalContext.addToGlobalContext(GlobalContextConstant.COMMAND_FACTORY, commandManager);

        EasyMock.replay(session, request, reservationService, serviceManager, commandManager, getReservationListCommand);

        Assert.assertEquals(WebPageConstant.INDEX.getPath(), offerHotelRoomCommand.process(request, null));

        EasyMock.verify(session, request, reservationService, serviceManager);
    }

}