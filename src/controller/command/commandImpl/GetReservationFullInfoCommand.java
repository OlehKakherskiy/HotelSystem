package controller.command.commandImpl;

import app.constants.GlobalContextConstant;
import controller.manager.GenericCommand;
import model.entity.Reservation;
import model.entity.User;
import model.service.AbstractReservationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GetReservationFullInfoCommand extends GenericCommand {


    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        Reservation currentReservation = (Reservation) request.getSession(false)
                .getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName());
        AbstractReservationService reservationService = serviceManager.getObject(AbstractReservationService.class);

        User user = (User) request.getSession(false).getAttribute(GlobalContextConstant.USER.getName());
        currentReservation = reservationService.getReservationDetailInfo(currentReservation.getId(), user);

        return null;
    }
}
