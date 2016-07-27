package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
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

        Reservation currentReservation = (Reservation) request.getSession(false)
                .getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName());
        AbstractReservationService reservationService = serviceManager.getInstance(AbstractReservationService.class);

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(GlobalContextConstant.USER.getName());

        currentReservation = reservationService.getReservationDetailInfo(currentReservation.getId());

        session.setAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName(), currentReservation);
        return WebPageConstant.RESERVATION_PROFILE.getPath();
    }
}
