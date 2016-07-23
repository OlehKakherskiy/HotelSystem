package controller.command.commandImpl;

import app.constants.GlobalContextConstant;
import app.constants.WebPageConstant;
import controller.command.GenericCommand;
import model.entity.Reservation;
import model.service.GenericReservationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class SubmitHotelRoomOfferCommand extends GenericCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        Reservation reservation = (Reservation) request.getSession(false)
                .getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName());
        GenericReservationService reservationService = serviceManager.getService(GenericReservationService.class);
        reservationService.setStatusToSubmitted(reservation);
        //TODO: redirect
        return WebPageConstant.INDEX.getPath();
    }
}
