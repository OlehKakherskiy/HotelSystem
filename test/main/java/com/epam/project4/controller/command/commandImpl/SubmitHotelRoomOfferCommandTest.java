package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.GlobalContext;
import main.java.com.epam.project4.app.constants.CommandConstant;
import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.ICommand;
import main.java.com.epam.project4.manager.AbstractCommandManager;
import main.java.com.epam.project4.manager.AbstractServiceManager;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.service.IReservationService;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class SubmitHotelRoomOfferCommandTest {

    @Test
    public void process() throws Exception {
        LogManager.getRootLogger().setLevel(Level.OFF);
        Reservation reservationStub = new Reservation();
        reservationStub.setHotelRoomId(1);

        HttpSession session = EasyMock.createMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName()))
                .andReturn(reservationStub);

        session.removeAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName());
        EasyMock.expectLastCall();

        EasyMock.expect(session.getAttribute(GlobalContextConstant.USER.getName())).andReturn(new User());

        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getSession(false)).andReturn(session);
        request.setAttribute(GlobalContextConstant.RESERVATION_STATUS.getName(), -1);
        EasyMock.expectLastCall();

        IReservationService reservationService = EasyMock.createMock(IReservationService.class);
        reservationService.submitReservationOffer(reservationStub);
        EasyMock.expectLastCall();

        AbstractServiceManager serviceManager = EasyMock.createMock(AbstractServiceManager.class);
        EasyMock.expect(serviceManager.getInstance(IReservationService.class)).andReturn(reservationService);

        ICommand getReservationListCommand = EasyMock.createMock(GetReservationListCommand.class);
        EasyMock.expect(getReservationListCommand.process(request, null)).andReturn(WebPageConstant.INDEX.getPath());

        AbstractCommandManager commandManager = EasyMock.createMock(AbstractCommandManager.class);
        EasyMock.expect(commandManager.getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND))
                .andReturn(getReservationListCommand);

        GlobalContext.addToGlobalContext(GlobalContextConstant.COMMAND_FACTORY, commandManager);
        EasyMock.replay(session, request, reservationService, serviceManager, getReservationListCommand, commandManager);

        ICommand submitReservationOfferCommand = new SubmitHotelRoomOfferCommand();
        submitReservationOfferCommand.setServiceManager(serviceManager);
        Assert.assertEquals(WebPageConstant.INDEX.getPath(), submitReservationOfferCommand.process(request, null));
        EasyMock.verify(session, request, reservationService, serviceManager, getReservationListCommand, commandManager);
    }

}