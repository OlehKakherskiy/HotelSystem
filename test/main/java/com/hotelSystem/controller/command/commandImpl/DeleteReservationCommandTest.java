package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.app.constants.WebPageConstant;
import main.java.com.hotelSystem.controller.command.ICommand;
import main.java.com.hotelSystem.exception.RequestException;
import main.java.com.hotelSystem.manager.AbstractCommandManager;
import main.java.com.hotelSystem.manager.AbstractServiceManager;
import main.java.com.hotelSystem.service.IReservationService;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class DeleteReservationCommandTest {

    @Test
    public void process() throws Exception {
        HttpServletRequest request = createRequestMock("1");

        IReservationService reservationService = EasyMock.createMock(IReservationService.class);
        reservationService.deleteReservation(1);
        EasyMock.expectLastCall();

        AbstractServiceManager serviceManager = EasyMock.createMock(AbstractServiceManager.class);
        EasyMock.expect(serviceManager.getInstance(IReservationService.class)).andReturn(reservationService);

        ICommand getReservationListCommand = EasyMock.createMock(GetReservationListCommand.class);
        EasyMock.expect(getReservationListCommand.process(request, null)).andReturn(WebPageConstant.INDEX.getPath());

        AbstractCommandManager commandManager = EasyMock.createMock(AbstractCommandManager.class);
        EasyMock.expect(commandManager.getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND))
                .andReturn(getReservationListCommand);

        GlobalContext.addToGlobalContext(GlobalContextConstant.COMMAND_FACTORY, commandManager);
        EasyMock.replay(reservationService, serviceManager, getReservationListCommand, commandManager);

        ICommand deleteReservationCommand = new DeleteReservationCommand();
        deleteReservationCommand.setServiceManager(serviceManager);
        Assert.assertEquals(WebPageConstant.INDEX.getPath(), deleteReservationCommand.process(request, null));

        EasyMock.verify(request, reservationService, serviceManager, getReservationListCommand, commandManager);

    }

    @Test(expected = RequestException.class)
    public void processNoReservationId() throws Exception {
        HttpServletRequest request = createRequestMock(null);
        DeleteReservationCommand deleteReservationCommand = new DeleteReservationCommand();
        try {
            deleteReservationCommand.process(request, null);
        } finally {
            EasyMock.verify(request);
        }
    }

    private HttpServletRequest createRequestMock(String id) throws Exception {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter(GlobalContextConstant.RESERVATION_ID.getName())).andReturn(id);
        EasyMock.replay(request);
        return request;
    }

}