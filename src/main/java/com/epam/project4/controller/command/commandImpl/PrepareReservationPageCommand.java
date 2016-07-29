package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.service.AbstractParameterValueService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class PrepareReservationPageCommand extends AbstractCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("newReservation", true);
        AbstractParameterValueService parameterValueService = serviceManager.getInstance(AbstractParameterValueService.class);
        request.setAttribute("requestParameters", parameterValueService.getParameterValueMap());
        return WebPageConstant.RESERVATION_PROFILE.getPath();
    }
}
