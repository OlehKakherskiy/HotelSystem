package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.exception.RequestException;
import main.java.com.epam.project4.model.service.AbstractReservationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GetReservationProfileCommand extends AbstractCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName()) == null) {

            String reservationId = request.getParameter(GlobalContextConstant.CURRENT_RESERVATION.getName());
            if (reservationId == null) {
                throw new RequestException();
            }

            int id = Integer.parseInt(reservationId);
            AbstractReservationService reservationService = serviceManager.getInstance(AbstractReservationService.class);
            Reservation currentReservation = reservationService.getReservationDetailInfo(id);

            session.setAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName(), currentReservation);
        }
        request.setAttribute("newReservation", false);
        return WebPageConstant.RESERVATION_PROFILE.getPath();
    }
}