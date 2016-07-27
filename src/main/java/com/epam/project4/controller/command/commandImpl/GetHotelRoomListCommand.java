package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.service.AbstractHotelRoomService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class GetHotelRoomListCommand extends AbstractCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        //TODO: нужно права проверять!!
        AbstractHotelRoomService hotelRoomService = serviceManager.getInstance(AbstractHotelRoomService.class);

        request.setAttribute("hotelRoomList", hotelRoomService.getAllRoomFullDetails(true));

        return WebPageConstant.HOTEL_ROOM_LIST.getPath();
    }

}
