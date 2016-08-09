package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.service.IReservationService;
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
            addNewRequestInfoToLog(user, id);
            session.setAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName(), currentReservation);
        } else {
            if (session.getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName()) == null) {
                throw new RuntimeException(); //TODO: поменять на RequestException
            }
        }
        request.setAttribute("newReservation", false);
        return WebPageConstant.RESERVATION_PROFILE.getPath();
    }

    private void addNewRequestInfoToLog(User user, int reservationId) {
        logger.info(MessageFormat.format("User (id = {0}) requested info about reservation id = {1}", user.getIdUser(), reservationId));
    }
}