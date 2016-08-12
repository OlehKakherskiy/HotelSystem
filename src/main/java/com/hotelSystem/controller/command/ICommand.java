package main.java.com.hotelSystem.controller.command;

import main.java.com.hotelSystem.manager.AbstractServiceManager;
import main.java.com.hotelSystem.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface represents command, that services user's requests via http.
 * Each command calls specific {@link IService} objects
 * for providing business process, requested by user, execution. Each command reformats and validates
 * inputted parameters and calls {@link IService}' methods,
 * after retruns result of operations to JSP
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 * @see IService
 */
public interface ICommand {

    /**
     * Services user's request, parameters are sent via {@link HttpServletRequest} and
     * result of business-logic can be retrurned via the session object, or using request/response params
     *
     * @param request  http request
     * @param response http response
     * @return path to next jsp
     */
    String process(HttpServletRequest request, HttpServletResponse response);

    /**
     * sets service manager
     *
     * @param serviceManager service manager
     */
    void setServiceManager(AbstractServiceManager serviceManager);
}