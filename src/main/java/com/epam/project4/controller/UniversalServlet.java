package main.java.com.epam.project4.controller;

import main.java.com.epam.project4.app.ApplicationConfigurer;
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

    static {
        new ApplicationConfigurer();
    }
    private AbstractCommandManager commandFactory = (AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doGet");
        if (req.getParameter(GlobalContextConstant.COMMAND_NAME.getName()) == null) {
            req.getRequestDispatcher(String.format("%s?command=%s", WebPageConstant.LOGIN.getPath(), CommandConstant.LOGIN_COMMAND.name())).include(req, resp);
        }
        String newUrl = processCommand(req, resp);
        req.getRequestDispatcher(newUrl).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPost");
        System.out.println("commandFactory = " + commandFactory);
        String newUrl = processCommand(req, resp);
        req.getRequestDispatcher(newUrl).forward(req, resp);
    }

    private String processCommand(HttpServletRequest req, HttpServletResponse resp) {
        String commandName = req.getParameter(GlobalContextConstant.COMMAND_NAME.getName());
        System.out.println("commandName = " + commandName);
        if (commandName == null) {
            System.out.println(WebPageConstant.LOGIN.getPath());
            return WebPageConstant.LOGIN.getPath();
        } else {
            System.out.println(commandFactory.getInstance(CommandConstant.fromValue(commandName)));
            return commandFactory.getInstance(CommandConstant.fromValue(commandName)).process(req, resp);
        }
    }
}

