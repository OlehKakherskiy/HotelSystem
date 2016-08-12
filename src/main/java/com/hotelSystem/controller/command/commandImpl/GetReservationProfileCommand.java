package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.app.constants.MessageCode;
import main.java.com.hotelSystem.app.constants.WebPageConstant;
import main.java.com.hotelSystem.controller.command.AbstractCommand;
import main.java.com.hotelSystem.exception.SystemException;
import main.java.com.hotelSystem.model.Reservation;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.service.IReservationService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GetReservationProfileCommand extends AbstractCommand {

    private static Logger logger = Logger.getLogger(GetReservationProfileCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String reservationId = request.getParameter(GlobalContextConstant.CURRENT_RESERVATION.getName());
        User user = (User) session.getAttribute(GlobalContextConstant.USER.getName());
        if (reservationId != null) {
            int id = Integer.parseInt(reservationId);
            IReservationService reservationService = serviceManager.getInstance(IReservationService.class);
            Reservation currentReservation = reservationService.getReservationDetailInfo(id);
            session.setAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName(), currentReservation);
            addNewRequestInfoToLog(user, id);
        } else {
            if (session.getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName()) == null) {
                throw new SystemException(MessageCode.GENERAL_SYSTEM_EXCEPTION);
            }
        }
        request.setAttribute("newReservation", false);
        return WebPageConstant.RESERVATION_PROFILE.getPath();
    }

    private void addNewRequestInfoToLog(User user, int reservationId) {
        logger.info(MessageFormat.format("User (id = {0}) requested info about reservation id = {1}", user.getIdUser(), reservationId));
    }
}