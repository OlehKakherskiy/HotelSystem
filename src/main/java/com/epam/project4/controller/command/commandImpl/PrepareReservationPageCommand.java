package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.model.service.IParameterValueService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command prepares jsp with path {@link WebPageConstant#RESERVATION_PROFILE} for inputting information
 * by user.
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class PrepareReservationPageCommand extends AbstractCommand {

    private static final String NEW_RESERVATION = "newReservation";

    private static final String REQUEST_PARAMETERS = "requestParameters";

    /**
     * {@inheritDoc}
     * <p>
     * Adds two parameters to request scope: boolean parameter with the key {@link #NEW_RESERVATION} and value = true,
     * that indicates to jsp page's logic, that creating new reservation operation was requested, and {@link java.util.Map}
     * parameter, containing possible request parameters, with the key {@link #REQUEST_PARAMETERS}
     * </p>
     *
     * @param request  http request
     * @param response http response
     * @return path of the jsp {@link WebPageConstant#RESERVATION_PROFILE}
     */
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(NEW_RESERVATION, true);
        IParameterValueService parameterValueService = serviceManager.getInstance(IParameterValueService.class);
        request.setAttribute(REQUEST_PARAMETERS, parameterValueService.getParameterValueMap());
        return WebPageConstant.RESERVATION_PROFILE.getPath();
    }
}
