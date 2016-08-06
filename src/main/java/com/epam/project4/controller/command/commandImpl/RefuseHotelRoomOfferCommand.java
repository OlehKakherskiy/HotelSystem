package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.GlobalContext;
import main.java.com.epam.project4.app.constants.CommandConstant;
import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.manager.AbstractCommandManager;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.service.AbstractReservationService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class RefuseHotelRoomOfferCommand extends AbstractCommand {

    Logger logger = Logger.getLogger(RefuseHotelRoomOfferCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        Reservation reservation = (Reservation) session.getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName());
        AbstractReservationService abstractReservationService = serviceManager.getInstance(AbstractReservationService.class);

        int hotelRoomId = reservation.getHotelRoomID();
        abstractReservationService.refuseReservationOffer(reservation);
        session.removeAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName());

        addInfoToLog(session, reservation, hotelRoomId);
        request.setAttribute("reservationStatus", -1);
        return ((AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY)).
                getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND).process(request, response);
    }

    private void addInfoToLog(HttpSession session, Reservation reservation, int hotelRoomId) {
        User user = (User) session.getAttribute(GlobalContextConstant.USER.getName());
        logger.info(MessageFormat.format("User id = {0} refused hotel room (id = {1}) offer for reservation (id = {2}).",
                user.getIdUser(), hotelRoomId, reservation.getId()));
    }
}
