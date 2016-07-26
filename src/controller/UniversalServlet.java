package controller;

import app.GlobalContext;
import app.constants.CommandConstant;
import app.constants.GlobalContextConstant;
import manager.AbstractCommandManager;

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

        //TODO: или переход или не переход
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        String newUrl = processCommand(req, resp);
        //TODO: или переход или не переход
    }

    private String processCommand(HttpServletRequest req, HttpServletResponse resp) {
        String commandName = (String) req.getAttribute(GlobalContextConstant.COMMAND_NAME.getName());

        return commandFactory.getInstance(CommandConstant.fromValue(commandName)).process(req, resp);
    }
}

