package controller.command;

import model.service.ServiceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericCommand {

    protected ServiceManager serviceManager;

    public abstract String process(HttpServletRequest request, HttpServletResponse response);

    public final void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }
}
