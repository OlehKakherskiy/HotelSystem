package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.controller.command.AbstractCommand;
import main.java.com.hotelSystem.manager.AbstractCommandManager;
import main.java.com.hotelSystem.model.HotelRoom;
import main.java.com.hotelSystem.model.Reservation;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.service.IReservationService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;

/**
 * Command servicing hotel room offer operation for target reservation.
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class OfferHotelRoomCommand extends AbstractCommand {

    private static final Logger logger = Logger.getLogger(OfferHotelRoomCommand.class);

    /**
     * {@inheritDoc}
     * <p>
     * For right servicing target reservation must be stored in user's session with the key {@link GlobalContextConstant#CURRENT_RESERVATION}
     * and room, that will be offered, must be stored in session with the key {@link GlobalContextConstant#CURRENT_HOTEL_ROOM}.
     * After processing operation these object will be removed from session.
     * </p>
     *
     * @param request  http request
     * @param response http response
     * @return see process operation return of {@link CommandConstant#GET_RESERVATION_LIST_COMMAND}
     */
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        IReservationService reservationService = serviceManager.getInstance(IReservationService.class);
        HttpSession session = request.getSession(false);
        Reservation reservation = (Reservation) session.getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName());
        HotelRoom currentHotelRoom = (HotelRoom) session.getAttribute(GlobalContextConstant.CURRENT_HOTEL_ROOM.getName());
        reservationService.offerHotelRoom(reservation, currentHotelRoom);

        addInfoToLog(session, reservation, currentHotelRoom);
        session.removeAttribute(GlobalContextConstant.CURRENT_HOTEL_ROOM.getName());
        session.removeAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName());

        return ((AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY))
                .getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND).process(request, response);
    }

    private void addInfoToLog(HttpSession session, Reservation reservation, HotelRoom hotelRoom) {
        User user = (User) session.getAttribute(GlobalContextConstant.USER.getName());
        logger.info(MessageFormat.format("User id = {0} offer hotel room with id = {1} for reservation id = {2}",
                user.getIdUser(), hotelRoom.getRoomId(), reservation.getId()));
    }
}
