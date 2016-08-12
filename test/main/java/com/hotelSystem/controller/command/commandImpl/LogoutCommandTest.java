package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.app.constants.WebPageConstant;
import main.java.com.hotelSystem.controller.command.ICommand;
import main.java.com.hotelSystem.model.User;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class LogoutCommandTest {

    @Test
    public void process() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);

        HttpSession session = EasyMock.createMock(HttpSession.class);
        EasyMock.expect(session.getAttribute(GlobalContextConstant.USER.getName())).andReturn(new User());
        session.invalidate();
        EasyMock.expectLastCall();

        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getSession(false)).andReturn(session);
        EasyMock.expect(request.getContextPath()).andReturn("/hotelSystem/");

        HttpServletResponse response = EasyMock.createMock(HttpServletResponse.class);
        response.sendRedirect("/hotelSystem/" + WebPageConstant.LOGIN.getPath());
        EasyMock.expectLastCall();

        ICommand logoutCommand = new LogoutCommand();

        EasyMock.replay(session, request, response);
        logoutCommand.process(request, response);
        EasyMock.verify(session, request, response);
    }

}