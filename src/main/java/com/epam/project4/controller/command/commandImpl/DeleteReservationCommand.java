package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.GlobalContext;
import main.java.com.epam.project4.app.constants.CommandConstant;
import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.exception.RequestException;
import main.java.com.epam.project4.manager.AbstractCommandManager;
import main.java.com.epam.project4.model.service.IReservationService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command removes target reservation from system, according to business-process there's no need
 * to service target reservation, because user-owner has removed it.
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class DeleteReservationCommand extends AbstractCommand {

    private static Logger logger = Logger.getLogger(SubmitHotelRoomOfferCommand.class);

    /**
     * {@inheritDoc}
     * <p>
     * For right servicing must be called with {@link GlobalContextConstant#RESERVATION_ID} parameter,
     * that has {@link Integer type}. If reservation was deleted successfully, calls command with the key
     * {@link CommandConstant#GET_RESERVATION_LIST_COMMAND}
     * </p>
     *
     * @param request  http request
     * @param response http response
     * @return see process operation return of {@link CommandConstant#GET_RESERVATION_LIST_COMMAND}
     */
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        String reservationId = request.getParameter(GlobalContextConstant.RESERVATION_ID.getName());
        if (reservationId == null) {
            throw new RequestException(MessageCode.NO_RESERVATION_ID_FOR_DELETE);
        }

        IReservationService reservationService = serviceManager.getInstance(IReservationService.class);
        reservationService.deleteReservation(Integer.parseInt(reservationId));

        return ((AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY))
                .getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND).process(request, response);
    }
}