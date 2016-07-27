package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.Reservation;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.enums.UserType;
import main.java.com.epam.project4.model.service.AbstractReservationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GetReservationListCommand extends AbstractCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        AbstractReservationService reservationService = serviceManager.getInstance(AbstractReservationService.class);
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute(GlobalContextConstant.USER.getName());

        ReservationStatus reservationStatus = (ReservationStatus) request.getAttribute("reservationStatus");

        List<Reservation> reservationList;

        if (currentUser.getUserType() == UserType.ADMIN) {
            LocalDate current = (LocalDate) request.getAttribute("monthDate");
//            reservationList = reservationService.getShortInfoAboutAllReservationsForAdminInPeriod(reservationStatus,
//                    getStartDateOfMonth(current), getEndDateOfMonth(current));
        }

//        reservationList = reservationService.getShortInfoAboutAllReservations(currentUser,
//                reservationStatus);
//TODO:раскомментировать строки - BL!!
        reservationList = new ArrayList<>();
        reservationList.add(new Reservation());
        request.setAttribute("reservationList", reservationList);
        return WebPageConstant.HOTEL_ROOM_LIST.getPath();
    }


    private LocalDate getStartDateOfMonth(LocalDate currentDate) {
        return currentDate.with(firstDayOfMonth());
    }

    private LocalDate getEndDateOfMonth(LocalDate currentDate) {
        return currentDate.with(lastDayOfMonth());
    }

}
