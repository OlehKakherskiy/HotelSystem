package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.app.constants.MessageCode;
import main.java.com.hotelSystem.controller.command.AbstractCommand;
import main.java.com.hotelSystem.exception.RequestException;
import main.java.com.hotelSystem.manager.AbstractCommandManager;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.service.IUserService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;

/**
 * Command providing user signIn operation in system.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class LoginCommand extends AbstractCommand {

    private static Logger logger = Logger.getLogger(LoginCommand.class);

    private static final String LANGUAGE_PARAM = "language";

    /**
     * {@inheritDoc}
     * <p>
     * For right servicing must be called with such input params,
     * as {@link GlobalContextConstant#LOGIN} and {@link GlobalContextConstant#PASSWORD}.
     * </p>
     * <p>
     * If signIn operation was successfull (there's user with input signIn/password combination
     * in system), creates user's session, adds {@link User} as attribute with key {@link GlobalContextConstant#USER}
     * and calls command with the key {@link CommandConstant#GET_RESERVATION_LIST_COMMAND}
     * </p>
     *
     * @param request  http request
     * @param response http response
     * @return see {@link CommandConstant#GET_RESERVATION_LIST_COMMAND} process' operation return
     */
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        String login = request.getParameter(GlobalContextConstant.LOGIN.getName());
        String password = request.getParameter(GlobalContextConstant.PASSWORD.getName());

        if (login == null || login.isEmpty() || password == null || password.isEmpty()) {
            throw new RequestException(MessageCode.WRONG_LOGIN_OR_PASSWORD);
        }

        IUserService userService = serviceManager.getInstance(IUserService.class);
        User user = userService.signIn(login, password);

        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute(GlobalContextConstant.USER.getName(), user);
        String lang = request.getParameter(LANGUAGE_PARAM);
        httpSession.setAttribute(GlobalContextConstant.CURRENT_LOCALE.getName(),
                (lang != null) ? lang : request.getLocale().toString());

        logger.info(MessageFormat.format("User id = {0} is signed in", user.getIdUser()));

        return ((AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY))
                .getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND).process(request, response);
    }
}