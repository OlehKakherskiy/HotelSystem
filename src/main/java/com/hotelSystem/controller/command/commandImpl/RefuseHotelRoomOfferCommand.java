package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.controller.command.AbstractCommand;
import main.java.com.hotelSystem.manager.AbstractCommandManager;
import main.java.com.hotelSystem.model.Reservation;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.model.enums.UserType;
import main.java.com.hotelSystem.service.IReservationService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;

/**
 * Command refuses hotel room offer for target reservation. Can be called only by
 * {@link UserType#REGISTERED_USER}.
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

        int hotelRoomId = reservation.getHotelRoomId();
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
