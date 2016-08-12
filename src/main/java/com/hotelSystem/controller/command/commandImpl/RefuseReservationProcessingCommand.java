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
 * Command refuses reservation processing. Can be called only by
 * {@link UserType#ADMIN}.
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class RefuseReservationProcessingCommand extends AbstractCommand {

    private static final Logger logger = Logger.getLogger(RefuseReservationProcessingCommand.class);

    /**
     * Refuses reservation processing. For right operation executing target {@link Reservation} object
     * should be placed in user's session with the key {@link GlobalContextConstant#CURRENT_RESERVATION}.
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
//        reservation.setHotelRoomId(-1);
        reservationService.refuseReservationProcessing(reservation);

        addInfoToLog(session, reservation);
        return ((AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY))
                .getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND).process(request, response);
    }

    private void addInfoToLog(HttpSession session, Reservation reservation) {
        User user = (User) session.getAttribute(GlobalContextConstant.USER.getName());
        logger.info(MessageFormat.format("User id = {0} refused reservation (id = {1}) processing.",
                user.getIdUser(), reservation.getId()));
    }

}