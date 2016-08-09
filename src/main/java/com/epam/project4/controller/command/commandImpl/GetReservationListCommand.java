package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.enums.UserType;
import main.java.com.epam.project4.model.service.IReservationService;
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
 * Returns reservation list. If user is {@link UserType#REGISTERED_USER}, returns
 * reservation list for current user, if user is {@link UserType#ADMIN}, returns
 * all reservation of all registered users. Reservations, returned, can be of
 * specific {@link ReservationStatus} or, if status is {@link ReservationStatus#ALL} - reservations
 * without filtering by status will be returned.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GetReservationListCommand extends AbstractCommand {

    private static final Logger logger = Logger.getLogger(GetReservationListCommand.class);

    /**
     * {@inheritDoc}
     * <p>
     * returns all reservations of specific user (or all users) with specific status, which id
     * is inputted as request parameter (or without it, then will status will be
     * {@link ReservationStatus#PROCESSING}). Result list adds as parameter to request scope
     * with name "reservationList"
     * </p>
     *
     * @param request  http request
     * @param response http response
     * @return path of {@link WebPageConstant#INDEX} jsp
     */
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        IReservationService reservationService = serviceManager.getInstance(IReservationService.class);
        HttpSession session = request.getSession(false);
        clearSessionExceptUser(session);
        User currentUser = (User) session.getAttribute(GlobalContextConstant.USER.getName());

        System.out.println("currentUser = " + currentUser);

        String parameterID = request.getParameter(GlobalContextConstant.RESERVATION_STATUS.getName());

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

    /**
     * clears all session scope attributes except user object and locale
     *
     * @param session user's session, that will be cleared from unused parameters
     */
    private void clearSessionExceptUser(HttpSession session) {
        Enumeration attrs = session.getAttributeNames();
        while (attrs.hasMoreElements()) {
            String att = (String) attrs.nextElement();
            if (!att.equals(GlobalContextConstant.USER.getName()) && !att.equals(GlobalContextConstant.CURRENT_LOCALE.getName())) {
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