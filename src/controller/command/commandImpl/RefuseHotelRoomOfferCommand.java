package controller.command.commandImpl;

import app.constants.GlobalContextConstant;
import controller.command.GenericCommand;
import model.entity.Reservation;
import model.entity.User;
import model.service.GenericReservationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class RefuseHotelRoomOfferCommand extends GenericCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession(false).getAttribute(GlobalContextConstant.USER.getName());
        Reservation reservation = (Reservation) request.getSession(false)
                .getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName());
        GenericReservationService genericReservationService = serviceManager.getService(GenericReservationService.class);
        genericReservationService.setStatusToRefused(reservation);

        return null;
        //TODO: redirect
    }
}
