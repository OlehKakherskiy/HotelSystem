package main.java.com.epam.project4.controller;

import main.java.com.epam.project4.app.ApplicationConfigurer;
import main.java.com.epam.project4.app.GlobalContext;
import main.java.com.epam.project4.app.constants.CommandConstant;
import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.ICommand;
import main.java.com.epam.project4.exception.RequestException;
import main.java.com.epam.project4.exception.SystemException;
import main.java.com.epam.project4.manager.AbstractCommandManager;
import main.java.com.epam.project4.model.entity.enums.ReservationStatus;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class UniversalServlet extends HttpServlet {

    private static Logger logger = Logger.getLogger(UniversalServlet.class);

    static {
        System.out.println("configuring app");
        new ApplicationConfigurer();
    }

    @Override
    public void init() throws ServletException {
        getServletContext().setAttribute("reservationStatusList", Arrays.stream(ReservationStatus.values()).collect(Collectors.toList()));
    }

    private AbstractCommandManager commandFactory = (AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("doGet");
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("text/html; charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
            if (req.getParameter(GlobalContextConstant.COMMAND_NAME.getName()) == null) {
                req.getRequestDispatcher(String.format("%s?command=%s", WebPageConstant.LOGIN.getPath(), CommandConstant.LOGIN_COMMAND.name())).include(req, resp);
            }
            String newUrl = processCommand(req, resp);
            if (!newUrl.isEmpty()) {
                req.getRequestDispatcher(newUrl).forward(req, resp);
            }
        } catch (SystemException e) {
            logger.error(e.getMessage(), e);
            processSystemException(req, resp, e);
        } catch (RequestException e1) {
            logger.warn(e1.getMessage(), e1);
            processRequestException(req, e1);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setContentType("text/html; charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
//            System.out.println("doPost");
//            System.out.println("commandFactory = " + commandFactory);
            String newUrl = processCommand(req, resp);
            req.getRequestDispatcher(newUrl).forward(req, resp);
        } catch (SystemException e) {
            logger.error(e.getMessage(), e);
            processSystemException(req, resp, e);
        } catch (RequestException e1) {
            logger.warn(e1.getMessage(), e1);
            processRequestException(req, e1);
        }
    }

    private String processCommand(HttpServletRequest req, HttpServletResponse resp) {
        String commandName = req.getParameter(GlobalContextConstant.COMMAND_NAME.getName());
        System.out.println("commandName = " + commandName);
        if (commandName == null) {
            System.out.println(WebPageConstant.LOGIN.getPath());
            return WebPageConstant.LOGIN.getPath();
        } else {
            System.out.println(commandFactory.getInstance(CommandConstant.fromValue(commandName)).toString());
            ICommand command = commandFactory.getInstance(CommandConstant.fromValue(commandName));
            return command.process(req, resp);
        }
    }

    private void processSystemException(HttpServletRequest request, HttpServletResponse response, SystemException e)
            throws IOException {
        Locale currentLocale = getLocale(request);
        e.setLocale(currentLocale);
        System.out.println(e.getLocalizedMessage());
        throw e;
    }

    private void processRequestException(HttpServletRequest request, RequestException e) {
        Locale currentLocale = getLocale(request);
        e.setLocale(currentLocale);
        String message = e.getLocalizedMessage();
        System.out.println(message);

        throw e;
    }

    private Locale getLocale(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String localeString = (String) session.getAttribute("lang");
        if (localeString != null) {
            String[] localeParts = localeString.split("_");
            return new Locale(localeParts[0], localeParts[1]);
        } else return request.getLocale();
    }
}

