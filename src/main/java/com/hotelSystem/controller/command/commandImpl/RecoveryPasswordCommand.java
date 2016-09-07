package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.controller.command.AbstractCommand;
import main.java.com.hotelSystem.manager.AbstractCommandManager;
import main.java.com.hotelSystem.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Oleg on 07.09.2016.
 */
public class RecoveryPasswordCommand extends AbstractCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter(GlobalContextConstant.LOGIN.getName());
        String newPassword = request.getParameter("newPassword");
        String passwordConfirmation = request.getParameter("passwordConfirmation");
        IUserService userService = serviceManager.getInstance(IUserService.class);
        userService.updatePassword(login, newPassword, passwordConfirmation);

        return ((AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY))
                .getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND).process(request, response);
    }
}