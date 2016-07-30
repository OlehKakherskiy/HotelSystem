package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.enums.UserType;
import main.java.com.epam.project4.model.service.AbstractReservationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GetReservationListCommand extends AbstractCommand {

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
        request.setAttribute("reservationList", reservationList);
        return WebPageConstant.INDEX.getPath();
    }

    private void clearSessionExceptUser(HttpSession session) {
        Enumeration attrs = session.getAttributeNames();
        while (attrs.hasMoreElements()) {
            String att = (String) attrs.nextElement();
            if (!att.equals(GlobalContextConstant.USER.getName())) {
                session.removeAttribute(att);
            }
        }
    }
}