package controller.command.commandImpl;

import app.GlobalContext;
import app.constants.CommandConstant;
import app.constants.GlobalContextConstant;
import controller.commandManager.GenericCommandManager;
import controller.command.GenericCommand;
import model.entity.User;
import model.entity.enums.ReservationStatus;
import model.entity.enums.UserType;
import model.service.AbstractUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class LoginCommand extends GenericCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        AbstractUserService userService = serviceManager.getInstance(AbstractUserService.class);

        String login = request.getParameter(GlobalContextConstant.LOGIN.getName());
        String password = request.getParameter(GlobalContextConstant.PASSWORD.getName());

        User user = userService.login(login, password);

        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute(GlobalContextConstant.USER.getName(), user);

        if (user.getUserType() == UserType.ADMIN) {
            request.setAttribute("monthDate", LocalDate.now());
            request.setAttribute("reservationStatus", ReservationStatus.PROCESSING);
        } else {
            request.setAttribute("reservationStatus", ReservationStatus.ALL);
        }

        return ((GenericCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY))
                .getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND).process(request, response);

    }
}
