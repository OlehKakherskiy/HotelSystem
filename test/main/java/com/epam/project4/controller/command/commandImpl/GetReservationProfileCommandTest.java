package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.ICommand;
import main.java.com.epam.project4.exception.SystemException;
import main.java.com.epam.project4.manager.AbstractServiceManager;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.service.IReservationService;
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
public class GetReservationProfileCommandTest {

    @Test
    public void process() throws Exception {
        LogManager.getRootLogger().setLevel(Level.OFF);
        Reservation reservationStub = new Reservation(1, LocalDate.now().plusDays(5), LocalDate.now().plusDays(10),
                LocalDate.now(), ReservationStatus.PROCESSING, 0, 0, "comment", Collections.emptyList());

        HttpSession session = EasyMock.createMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(GlobalContextConstant.USER.getName())).andReturn(new User());
        session.setAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName(), reservationStub);
        EasyMock.expectLastCall();

        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getSession(false)).andReturn(session);
        EasyMock.expect(request.getParameter(GlobalContextConstant.CURRENT_RESERVATION.getName())).andReturn("" + 1);
        request.setAttribute("newReservation", false);
        EasyMock.expectLastCall();


        IReservationService reservationService = EasyMock.createMock(IReservationService.class);
        EasyMock.expect(reservationService.getReservationDetailInfo(1)).andReturn(reservationStub);

        AbstractServiceManager serviceManager = EasyMock.createMock(AbstractServiceManager.class);
        EasyMock.expect(serviceManager.getInstance(IReservationService.class)).andReturn(reservationService);

        EasyMock.replay(session, request, reservationService, serviceManager);

        ICommand command = new GetReservationProfileCommand();
        command.setServiceManager(serviceManager);
        Assert.assertEquals(WebPageConstant.RESERVATION_PROFILE.getPath(), command.process(request, null));

        EasyMock.verify(session, request, reservationService, serviceManager);

    }

    @Test(expected = SystemException.class)
    public void processWithoutCurrentReservation() throws Exception {
        LogManager.getRootLogger().setLevel(Level.OFF);
        Reservation reservationStub = new Reservation(1, LocalDate.now().plusDays(5), LocalDate.now().plusDays(10),
                LocalDate.now(), ReservationStatus.PROCESSING, 0, 0, "comment", Collections.emptyList());

        HttpSession session = EasyMock.createMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(GlobalContextConstant.USER.getName())).andReturn(new User());
        EasyMock.expect(session.getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName())).andReturn(null);
        session.setAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName(), reservationStub);
        EasyMock.expectLastCall();

        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getSession(false)).andReturn(session);
        EasyMock.expect(request.getParameter(GlobalContextConstant.CURRENT_RESERVATION.getName())).andReturn(null);
        request.setAttribute("newReservation", false);
        EasyMock.expectLastCall();

        EasyMock.replay(session, request);

        ICommand command = new GetReservationProfileCommand();
        Assert.assertEquals(WebPageConstant.RESERVATION_PROFILE.getPath(), command.process(request, null));

        EasyMock.verify(session, request);

    }

}