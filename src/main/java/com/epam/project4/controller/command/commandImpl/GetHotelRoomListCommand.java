package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.service.AbstractHotelRoomService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class GetHotelRoomListCommand extends AbstractCommand {

    private static final Logger logger = Logger.getLogger(GetHotelRoomListCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session.getAttribute("hotelRoomList") == null) {
            AbstractHotelRoomService hotelRoomService = serviceManager.getInstance(AbstractHotelRoomService.class);
            session.setAttribute("hotelRoomList", hotelRoomService.getAllRoomFullDetails(true));
        }

        User user = (User) session.getAttribute(GlobalContextConstant.USER.getName());
        logger.info(MessageFormat.format("User (id = {0}) requested hotel room list", user.getIdUser()));
        return WebPageConstant.HOTEL_ROOM_LIST.getPath();
    }
}