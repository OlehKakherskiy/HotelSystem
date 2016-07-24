package controller.command.commandImpl;

import app.constants.GlobalContextConstant;
import app.constants.WebPageConstant;
import controller.manager.GenericCommand;
import model.entity.Reservation;
import model.entity.User;
import model.entity.enums.ReservationStatus;
import model.entity.enums.UserType;
import model.service.AbstractReservationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GetReservationListCommand extends GenericCommand {

    AbstractReservationService reservationService = serviceManager.getObject(AbstractReservationService.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute(GlobalContextConstant.USER.getName());

        ReservationStatus reservationStatus = (ReservationStatus) request.getAttribute("reservationStatus");

        List<Reservation> reservationList;

        if (currentUser.getUserType() == UserType.ADMIN) {
            LocalDate current = (LocalDate) request.getAttribute("monthDate");
            reservationList = reservationService.getShortInfoAboutAllReservationsForAdminInPeriod(reservationStatus,
                    getStartDateOfMonth(current), getEndDateOfMonth(current));
        }

        reservationList = reservationService.getShortInfoAboutAllReservations(currentUser,
                reservationStatus);

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
