package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.service.AbstractReservationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class SubmitHotelRoomOfferCommand extends AbstractCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        Reservation reservation = (Reservation) request.getSession(false)
                .getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName());
        AbstractReservationService reservationService = serviceManager.getInstance(AbstractReservationService.class);
        reservationService.setStatusToSubmitted(reservation);
        //TODO: redirect
        return WebPageConstant.INDEX.getPath();
    }
}
