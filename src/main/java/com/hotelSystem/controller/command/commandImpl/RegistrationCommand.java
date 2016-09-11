package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.app.constants.MessageCode;
import main.java.com.hotelSystem.controller.command.AbstractCommand;
import main.java.com.hotelSystem.exception.RequestException;
import main.java.com.hotelSystem.manager.AbstractCommandManager;
import main.java.com.hotelSystem.model.MobilePhone;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.model.enums.UserType;
import main.java.com.hotelSystem.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Oleg on 07.09.2016.
 */
public class RegistrationCommand extends AbstractCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        String password = request.getParameter("password");
        String passwordConfirmation = request.getParameter("passwordConfirmation");
        checkPassword(password, passwordConfirmation);
        String name = request.getParameter("name");
        String lastName = request.getParameter("lastName");
        String login = request.getParameter("login");

        User user = new User(name, lastName, UserType.REGISTERED_USER, login, password, getMobilePhones(request));

        IUserService userService = serviceManager.getInstance(IUserService.class);
        userService.register(user);
        request.getSession(false).setAttribute(GlobalContextConstant.USER.getName(), user);
        return ((AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY))
                .getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND).process(request, response);
    }

    private List<MobilePhone> getMobilePhones(HttpServletRequest request) {
        Map<String, Object> paramsMap = request.getParameterMap();
        return paramsMap.entrySet()
                .stream().map(Map.Entry::getKey)
                .filter(key -> key.startsWith("mobilePhone"))
                .map(request::getParameter).map(MobilePhone::new)
                .collect(Collectors.toList());
    }

    private void checkPassword(String password, String passwordConfirmation) {
        if (password == null || password.isEmpty()
                || passwordConfirmation == null || passwordConfirmation.isEmpty()
                || !password.equals(passwordConfirmation)) {
            throw new RequestException(MessageCode.PASSWORD_CONFIRMATION_EXCEPTION_MESSAGE);
        }
    }
}
