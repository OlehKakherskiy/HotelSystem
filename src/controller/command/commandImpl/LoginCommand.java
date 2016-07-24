package controller.command.commandImpl;

import app.GlobalContext;
import app.constants.CommandConstant;
import app.constants.GlobalContextConstant;
import app.constants.WebPageConstant;
import controller.GenericCommandManager;
import controller.manager.GenericCommand;
import model.entity.User;
import model.entity.enums.ReservationStatus;
import model.entity.enums.UserType;
import model.service.AbstractUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class LoginCommand extends GenericCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        AbstractUserService userService = serviceManager.getObject(AbstractUserService.class);

        String login = request.getParameter(GlobalContextConstant.LOGIN.getName());
        String password = request.getParameter(GlobalContextConstant.PASSWORD.getName());

        User user = userService.login(login, password);
        HttpSession httpSession = request.getSession(true);
        httpSession.setAttribute(GlobalContextConstant.USER.getName(), user);
        request.setAttribute("reservationStatus",
                UserType.fromID(user.getIdUser()) == UserType.ADMIN
                        ? ReservationStatus.PROCESSING.getId()
                        : ReservationStatus.ALL.getId());

        ((GenericCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY))
                .getObject(CommandConstant.GET_RESERVATION_LIST_COMMAND).process(request, response);
        return WebPageConstant.HOTEL_ROOM_LIST.getPath();
    }
}
