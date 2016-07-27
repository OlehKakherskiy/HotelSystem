package main.java.com.epam.project4.controller;

import main.java.com.epam.project4.app.GlobalContext;
import main.java.com.epam.project4.app.constants.CommandConstant;
import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.manager.AbstractCommandManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class UniversalServlet extends HttpServlet {

    private AbstractCommandManager commandFactory = (AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String newUrl = processCommand(req, resp);
        System.out.println("doGet");
        req.getRequestDispatcher(newUrl).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPost");
        String newUrl = processCommand(req, resp);
        req.getRequestDispatcher(newUrl).forward(req, resp);
    }

    private String processCommand(HttpServletRequest req, HttpServletResponse resp) {
        String commandName = (String) req.getAttribute(GlobalContextConstant.COMMAND_NAME.getName());
        if (commandName == null) {
            return WebPageConstant.LOGIN.getPath();
        } else {
            return commandFactory.getInstance(CommandConstant.fromValue(commandName)).process(req, resp);
        }
    }
}

