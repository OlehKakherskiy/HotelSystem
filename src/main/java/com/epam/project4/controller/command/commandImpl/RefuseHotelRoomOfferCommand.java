package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.GlobalContext;
import main.java.com.epam.project4.app.constants.CommandConstant;
import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.manager.AbstractCommandManager;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.service.IReservationService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;

/**
 * Command refuses hotel room offer for target reservation. Can be called only by
 * {@link main.java.com.epam.project4.model.entity.enums.UserType#REGISTERED_USER}.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class RefuseHotelRoomOfferCommand extends AbstractCommand {

    Logger logger = Logger.getLogger(RefuseHotelRoomOfferCommand.class);

    /**
     * {@inheritDoc}
     * <p>
     * Refuses hotel room offer for target reservation. For rigth process executing
     * {@link Reservation} object should be placed in the user's session with the key
     * {@link GlobalContextConstant#CURRENT_RESERVATION}.
     * </p>
     *
     * @param request  http request
     * @param response http response
     * @return see process operation return of {@link CommandConstant#GET_RESERVATION_LIST_COMMAND}
     */
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        Reservation reservation = (Reservation) session.getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName());
        IReservationService abstractReservationService = serviceManager.getInstance(IReservationService.class);

        int hotelRoomId = reservation.getHotelRoomID();
        abstractReservationService.refuseReservationOffer(reservation);
        session.removeAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName());

        addInfoToLog(session, reservation, hotelRoomId);
        request.setAttribute(GlobalContextConstant.RESERVATION_STATUS.getName(), -1);
        return ((AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY)).
                getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND).process(request, response);
    }

    private void addInfoToLog(HttpSession session, Reservation reservation, int hotelRoomId) {
        User user = (User) session.getAttribute(GlobalContextConstant.USER.getName());
        logger.info(MessageFormat.format("User id = {0} refused hotel room (id = {1}) offer for reservation (id = {2}).",
                user.getIdUser(), hotelRoomId, reservation.getId()));
    }
}
