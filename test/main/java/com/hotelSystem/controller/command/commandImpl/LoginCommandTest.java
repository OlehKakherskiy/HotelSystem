package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.app.constants.WebPageConstant;
import main.java.com.hotelSystem.controller.command.ICommand;
import main.java.com.hotelSystem.exception.RequestException;
import main.java.com.hotelSystem.exception.SystemException;
import main.java.com.hotelSystem.manager.AbstractCommandManager;
import main.java.com.hotelSystem.manager.AbstractServiceManager;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.model.enums.UserType;
import main.java.com.hotelSystem.service.IService;
import main.java.com.hotelSystem.service.IUserService;
import main.java.com.hotelSystem.service.serviceImpl.UserService;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class LoginCommandTest {

    @BeforeClass
    public static void beforeClass() throws Exception {
        LogManager.getRootLogger().setLevel(Level.OFF);
    }

    @Test
    public void process() throws Exception {
        UserService userService = EasyMock.createMock(UserService.class);
        User stub = new User(1, "name", "userName", UserType.ADMIN, Collections.emptyList());
        EasyMock.expect(userService.signIn("login", "password")).andReturn(stub);

        AbstractServiceManager serviceManager = getServiceManagerMock(IUserService.class, userService);

        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter(GlobalContextConstant.LOGIN.getName())).andReturn("login");
        EasyMock.expect(request.getParameter(GlobalContextConstant.PASSWORD.getName())).andReturn("password");

        ICommand indexCommand = EasyMock.createMock(ICommand.class);
        EasyMock.expect(indexCommand.process(request, null)).andReturn(WebPageConstant.INDEX.getPath());

        AbstractCommandManager commandManager = EasyMock.createMock(AbstractCommandManager.class);
        EasyMock.expect(commandManager.getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND)).andReturn(indexCommand);

        GlobalContext.addToGlobalContext(GlobalContextConstant.COMMAND_FACTORY, commandManager);

        HttpSession session = getSessionMock(new HashMap<String, Object>() {{
            put(GlobalContextConstant.USER.getName(), stub);
            put("lang", "ru_RU");
        }});
        EasyMock.expect(request.getSession(true)).andReturn(session);
        EasyMock.expect(request.getParameter("language")).andReturn(null);
        EasyMock.expect(request.getLocale()).andReturn(new Locale("ru", "RU"));

        ICommand loginCommand = new LoginCommand();
        loginCommand.setServiceManager(serviceManager);
        EasyMock.replay(userService, request, commandManager, indexCommand);
        Assert.assertEquals(WebPageConstant.INDEX.getPath(), loginCommand.process(request, null));
        EasyMock.verify(userService, serviceManager, request, session, commandManager, indexCommand);
    }

    private AbstractServiceManager getServiceManagerMock(Class serviceClass, IService service) {
        AbstractServiceManager serviceManager = EasyMock.createMock(AbstractServiceManager.class);
        EasyMock.expect(serviceManager.getInstance(serviceClass)).andReturn(service);
        EasyMock.replay(serviceManager);
        return serviceManager;
    }

    private HttpSession getSessionMock(Map<String, Object> sessionParams) {
        HttpSession result = EasyMock.createMock(HttpSession.class);
        sessionParams.forEach((o, o2) -> {
            result.setAttribute(o, o2);
            EasyMock.expectLastCall();
        });
        EasyMock.replay(result);
        return result;
    }

    @Test(expected = SystemException.class)
    public void processSystemException() throws Exception {
        UserService userService = EasyMock.createMock(UserService.class);
        EasyMock.expect(userService.signIn("login", "password")).andThrow(new SystemException(null));

        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter(GlobalContextConstant.LOGIN.getName())).andReturn("login");
        EasyMock.expect(request.getParameter(GlobalContextConstant.PASSWORD.getName())).andReturn("password");

        ICommand loginCommand = new LoginCommand();
        AbstractServiceManager serviceManager = getServiceManagerMock(IUserService.class, userService);
        loginCommand.setServiceManager(serviceManager);
        EasyMock.replay(userService, request);

        try {
            loginCommand.process(request, null);
        } finally {
            EasyMock.verify(userService, request, serviceManager);
        }
    }

    @Test(expected = RequestException.class)
    public void processRequestExceptionFromService() throws Exception {
        UserService userService = EasyMock.createMock(UserService.class);
        EasyMock.expect(userService.signIn("login", "password")).andThrow(new RequestException(null));

        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter(GlobalContextConstant.LOGIN.getName())).andReturn("login");
        EasyMock.expect(request.getParameter(GlobalContextConstant.PASSWORD.getName())).andReturn("password");

        ICommand loginCommand = new LoginCommand();
        AbstractServiceManager serviceManager = getServiceManagerMock(IUserService.class, userService);
        loginCommand.setServiceManager(serviceManager);
        EasyMock.replay(userService, request);

        try {
            loginCommand.process(request, null);
        } finally {
            EasyMock.verify(userService, request, serviceManager);
        }
    }

    @Test(expected = RequestException.class)
    public void processRequestExceptionNoLoginParam() throws Exception {
        checkLoginOrPassNullParam(null, "password");
    }

    @Test(expected = RequestException.class)
    public void processRequestExceptionNoPasswordParam() throws Exception {
        checkLoginOrPassNullParam("login", null);
    }

    private void checkLoginOrPassNullParam(String login, String password) throws Exception {
        HttpServletRequest request = EasyMock.createMock(HttpServletRequest.class);
        EasyMock.expect(request.getParameter(GlobalContextConstant.LOGIN.getName())).andReturn(login);
        EasyMock.expect(request.getParameter(GlobalContextConstant.PASSWORD.getName())).andReturn(password);

        ICommand loginCommand = new LoginCommand();
        EasyMock.replay(request);
        try {
            loginCommand.process(request, null);
        } finally {
            EasyMock.verify(request);
        }
    }
}