package main.java.com.epam.project4.controller.command;

import main.java.com.epam.project4.manager.AbstractServiceManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public interface ICommand {

    String process(HttpServletRequest request, HttpServletResponse response);

    void setServiceManager(AbstractServiceManager serviceManager);
}