package controller.command.commandImpl;

import app.constants.GlobalContextConstant;
import app.constants.WebPageConstant;
import controller.manager.GenericCommand;
import model.entity.User;
import model.entity.enums.ReservationStatus;
import model.service.AbstractReservationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GetReservationListCommand extends GenericCommand {

    AbstractReservationService reservationService = serviceManager.getObject(AbstractReservationService.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute(GlobalContextConstant.USER.getName());

        Integer reservationStatusCode = (Integer) request.getAttribute("reservationStatus");

        request.setAttribute("reservations", reservationService.getShortInfoAboutAllReservations(currentUser,
                ReservationStatus.fromId(reservationStatusCode)));
        return WebPageConstant.HOTEL_ROOM_LIST.getPath();
    }

}
