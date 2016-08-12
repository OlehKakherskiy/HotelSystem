package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.app.constants.WebPageConstant;
import main.java.com.hotelSystem.controller.command.AbstractCommand;
import main.java.com.hotelSystem.model.HotelRoom;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.service.IHotelRoomService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;

/**
 * Command returns list of {@link HotelRoom}.
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class GetHotelRoomListCommand extends AbstractCommand {

    private static final Logger logger = Logger.getLogger(GetHotelRoomListCommand.class);

    /**
     * {@inheritDoc}
     * <p>
     * Adds to session result of {@link IHotelRoomService#getAllRoomFullDetails(boolean)}. If hotel
     * room list has already been in session, do nothing
     * </p>
     *
     * @param request  http request
     * @param response http response
     * @return jsp with path {@link WebPageConstant#HOTEL_ROOM_LIST}
     */
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute(GlobalContextConstant.HOTEL_ROOM_LIST.getName()) == null) {
            IHotelRoomService hotelRoomService = serviceManager.getInstance(IHotelRoomService.class);
            session.setAttribute(GlobalContextConstant.HOTEL_ROOM_LIST.getName(),
                    hotelRoomService.getAllRoomFullDetails(true));
        }

        User user = (User) session.getAttribute(GlobalContextConstant.USER.getName());
        logger.info(MessageFormat.format("User (id = {0}) requested hotel room list", user.getIdUser()));
        return WebPageConstant.HOTEL_ROOM_LIST.getPath();
    }
}