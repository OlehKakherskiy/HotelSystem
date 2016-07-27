package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.entity.HotelRoom;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.enums.UserType;
import main.java.com.epam.project4.model.service.AbstractHotelRoomService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GetHotelRoomProfileCommand extends AbstractCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        AbstractHotelRoomService hotelRoomService = serviceManager.getInstance(AbstractHotelRoomService.class);
        Integer roomId = Integer.valueOf(request.getParameter("hotelRoomId"));
        User user = (User) request.getSession(false).getAttribute("user");
        HotelRoom hotelRoom = hotelRoomService.getFullDetails(roomId);
        if (user.getUserType() == UserType.ADMIN) {
            appendReservationInfo(request, hotelRoom, hotelRoomService);
        }
        request.setAttribute("hotelRoom", hotelRoom);
        return WebPageConstant.HOTEL_ROOM_PROFILE.getPath();
    }


    private void appendReservationInfo(HttpServletRequest request, HotelRoom hotelRoom, AbstractHotelRoomService hotelRoomService) {
        LocalDate now = LocalDate.now();

        String monthStr = (String) request.getAttribute("month");
        Month month = (monthStr == null || monthStr.isEmpty()) ? now.getMonth() : Month.of(Integer.valueOf(monthStr)); //TODO: разобраться когда месяц = 13

        String yearStr = (String) request.getAttribute("year");

        Year year = (yearStr == null || yearStr.isEmpty()) ? Year.of(now.getYear()) : Year.of(Integer.valueOf(monthStr));
        hotelRoomService.appendSubmittedReservations(hotelRoom, month, year, ReservationStatus.SUBMITTED);
    }
}
