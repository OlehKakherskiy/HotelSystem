package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.entity.HotelRoom;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import main.java.com.epam.project4.model.entity.enums.UserType;
import main.java.com.epam.project4.model.service.AbstractHotelRoomService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GetHotelRoomProfileCommand extends AbstractCommand {

    private static final Logger logger = Logger.getLogger(GetHotelRoomProfileCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        AbstractHotelRoomService hotelRoomService = serviceManager.getInstance(AbstractHotelRoomService.class);
        Integer roomId = Integer.valueOf(request.getParameter("hotelRoomId"));
        User user = (User) request.getSession(false).getAttribute(GlobalContextConstant.USER.getName());
        HotelRoom hotelRoom = hotelRoomService.getFullDetails(roomId);
        if (user.getUserType() == UserType.ADMIN) {
            appendReservationInfo(request, hotelRoom, hotelRoomService);
        }
        request.setAttribute("hotelRoom", hotelRoom);

        logger.info(MessageFormat.format("User (id = {0}) requested hotel room info (room id = {1})",
                user.getIdUser(), hotelRoom.getRoomID()));

        request.getSession(false).setAttribute(GlobalContextConstant.CURRENT_HOTEL_ROOM.getName(), hotelRoom);
        return WebPageConstant.HOTEL_ROOM_PROFILE.getPath();
    }


    private void appendReservationInfo(HttpServletRequest request, HotelRoom hotelRoom, AbstractHotelRoomService hotelRoomService) {
        LocalDate now = LocalDate.now();
        String monthStr = request.getParameter("reservationMonth");
        Month month = (monthStr == null || monthStr.isEmpty()) ? now.getMonth() : Month.of(Integer.valueOf(monthStr));

        String yearStr = request.getParameter("reservationYear");
        Year year = (yearStr == null || yearStr.isEmpty()) ? Year.of(now.getYear()) : Year.of(Integer.valueOf(yearStr));


        hotelRoomService.appendSubmittedReservations(hotelRoom, month, year, ReservationStatus.SUBMITTED);
        request.setAttribute("reservationMonth", month);
        request.setAttribute("reservationYear", year);
    }
}
