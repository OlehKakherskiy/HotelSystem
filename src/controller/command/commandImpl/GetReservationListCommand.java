package controller.command.commandImpl;

import app.constants.GlobalContextConstant;
import controller.command.GenericCommand;
import model.entity.User;
import model.service.GenericReservationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GetReservationListCommand extends GenericCommand {

    GenericReservationService reservationService = serviceManager.getService(GenericReservationService.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute(GlobalContextConstant.USER.getName());
        currentUser.setReservations(reservationService.getShortInfoAboutAllReservations(currentUser));
        return null;
    }

}
