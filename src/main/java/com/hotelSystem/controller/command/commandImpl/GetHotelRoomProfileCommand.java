package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.app.constants.WebPageConstant;
import main.java.com.hotelSystem.controller.command.AbstractCommand;
import main.java.com.hotelSystem.model.HotelRoom;
import main.java.com.hotelSystem.model.Reservation;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.model.enums.ReservationStatus;
import main.java.com.hotelSystem.model.enums.UserType;
import main.java.com.hotelSystem.service.IHotelRoomService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.time.Month;
import java.time.Year;

/**
 * Command returns hotel room profile with target id and adds month reservation details to it (if
 * user is admin).
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class GetHotelRoomProfileCommand extends AbstractCommand {

    private static final Logger logger = Logger.getLogger(GetHotelRoomProfileCommand.class);

    /**
     * request parameter, that indicates hotel room's id
     */
    private static final String HOTEL_ROOM_ID = "hotelRoomId";

    /**
     * request parameter, that indicates {@link HotelRoom} object.
     */
    private static final String HOTEL_ROOM = "hotelRoom";

    /**
     * {@inheritDoc}
     * <p>
     * For right servicing must be called with request parameter with the key "hotelRoomId"
     * </p>
     * <p>
     * Returns hotel room profile with month reservation details (if user is admin)
     * </p>
     *
     * @param request  http request
     * @param response http response
     * @return path of {@link WebPageConstant#HOTEL_ROOM_PROFILE}
     */
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        IHotelRoomService hotelRoomService = serviceManager.getInstance(IHotelRoomService.class);
        Integer roomId = Integer.valueOf(request.getParameter(HOTEL_ROOM_ID));
        User user = (User) request.getSession(false).getAttribute(GlobalContextConstant.USER.getName());
        HotelRoom hotelRoom = hotelRoomService.getFullDetails(roomId);
        if (user.getUserType() == UserType.ADMIN) {
            appendReservationInfo(request, hotelRoom, hotelRoomService);
        }
        request.setAttribute(HOTEL_ROOM, hotelRoom);

        logger.info(MessageFormat.format("User (id = {0}) requested hotel room info (room id = {1})",
                user.getIdUser(), hotelRoom.getRoomId()));

        request.getSession(false).setAttribute(GlobalContextConstant.CURRENT_HOTEL_ROOM.getName(), hotelRoom);
        return WebPageConstant.HOTEL_ROOM_PROFILE.getPath();
    }


    /**
     * Appends reservations of target month and year with status {@link ReservationStatus#SUBMITTED}.
     * If there's no reservations - returns empty list.
     * <p>
     * For rigth operation executing should be called with parameters with such keys in request scope, as
     * {@link GlobalContextConstant#RESERVATION_MONTH}, {@link GlobalContextConstant#RESERVATION_YEAR}. These
     * parameters will have String type. After processing operation stores object with these keys, but with
     * types {@link Month} and {@link Year}.
     * </p>
     *
     * @param request          http request object
     * @param hotelRoom        target room, to which month's reservation list will be added
     * @param hotelRoomService service object, that will provide operation execution
     */
    private void appendReservationInfo(HttpServletRequest request, HotelRoom hotelRoom, IHotelRoomService hotelRoomService) {
        Reservation reservation = (Reservation) request.getSession(false).getAttribute(GlobalContextConstant.CURRENT_RESERVATION.getName());
        String monthStr = request.getParameter(GlobalContextConstant.RESERVATION_MONTH.getName());
        Month month = (monthStr == null || monthStr.isEmpty()) ? reservation.getDateFrom().getMonth() : Month.of(Integer.valueOf(monthStr));

        String yearStr = request.getParameter(GlobalContextConstant.RESERVATION_YEAR.getName());
        Year year = (yearStr == null || yearStr.isEmpty()) ? Year.of(reservation.getDateTo().getYear()) : Year.of(Integer.valueOf(yearStr));

        hotelRoomService.appendReservations(hotelRoom, month, year, ReservationStatus.SUBMITTED);
        request.setAttribute(GlobalContextConstant.RESERVATION_MONTH.getName(), month);
        request.setAttribute(GlobalContextConstant.RESERVATION_YEAR.getName(), year);
    }
}
