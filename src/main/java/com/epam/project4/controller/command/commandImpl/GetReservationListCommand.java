package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.enums.UserType;
import main.java.com.epam.project4.model.service.AbstractReservationService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GetReservationListCommand extends AbstractCommand {

    private static final Logger logger = Logger.getLogger(GetReservationListCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        AbstractReservationService reservationService = serviceManager.getInstance(AbstractReservationService.class);
        HttpSession session = request.getSession(false);
        clearSessionExceptUser(session);
        User currentUser = (User) session.getAttribute(GlobalContextConstant.USER.getName());

        System.out.println("currentUser = " + currentUser);

        String parameterID = request.getParameter("reservationStatus");

        ReservationStatus reservationStatus = (parameterID == null)
                ? ReservationStatus.PROCESSING
                : ReservationStatus.fromId(Integer.parseInt(parameterID));

        System.out.println("reservationStatus = " + reservationStatus);
        List<Reservation> reservationList;
        if (currentUser.getUserType() == UserType.ADMIN) {
            reservationList = reservationService.getShortInfoAboutAllReservations(reservationStatus);
        } else {
            reservationList = reservationService.getShortInfoAboutAllReservations(currentUser, reservationStatus);
        }
        System.out.println(Arrays.toString(reservationList.toArray()));
        addRequestInfoToLog(currentUser, reservationStatus, reservationList);
        request.setAttribute("reservationList", reservationList);
        return WebPageConstant.INDEX.getPath();
    }

    private void clearSessionExceptUser(HttpSession session) {
        Enumeration attrs = session.getAttributeNames();
        while (attrs.hasMoreElements()) {
            String att = (String) attrs.nextElement();
            if (!att.equals(GlobalContextConstant.USER.getName()) && !att.equals("lang")) {
                session.removeAttribute(att);
            }
        }
    }

    private void addRequestInfoToLog(User user, ReservationStatus status, List<Reservation> reservations) {
        logger.info(MessageFormat.format("User (id = {0}) requested " +
                        "reservation list info with status = {1}. Reservations ids = {2}", user.getIdUser(), status,
                Arrays.toString(reservations.stream().map(Reservation::getId).collect(Collectors.toList()).toArray())));
    }
}