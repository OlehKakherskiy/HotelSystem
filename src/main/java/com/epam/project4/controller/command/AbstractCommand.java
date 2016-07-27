package main.java.com.epam.project4.controller.command;

import main.java.com.epam.project4.manager.GenericServiceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class AbstractCommand {

    protected GenericServiceManager serviceManager;

    public abstract String process(HttpServletRequest request, HttpServletResponse response);

    public void setServiceManager(GenericServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }
}
