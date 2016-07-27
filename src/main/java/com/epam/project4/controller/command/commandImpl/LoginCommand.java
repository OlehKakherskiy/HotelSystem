package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.GlobalContext;
import main.java.com.epam.project4.app.constants.CommandConstant;
import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.manager.AbstractCommandManager;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.enums.UserType;
import main.java.com.epam.project4.model.service.AbstractUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class LoginCommand extends AbstractCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        AbstractUserService userService = serviceManager.getInstance(AbstractUserService.class);

        String login = request.getParameter(GlobalContextConstant.LOGIN.getName());
        String password = request.getParameter(GlobalContextConstant.PASSWORD.getName());

        System.out.println("login = " + login);
        System.out.println("password = " + password);

        User user = userService.login(login, password);

        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute(GlobalContextConstant.USER.getName(), user);

        if (user.getUserType() == UserType.ADMIN) {
            request.setAttribute("monthDate", LocalDate.now());
            request.setAttribute("reservationStatus", ReservationStatus.PROCESSING);
        } else {
            request.setAttribute("reservationStatus", ReservationStatus.ALL);
        }

        return ((AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY))
                .getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND).process(request, response);

    }
}
