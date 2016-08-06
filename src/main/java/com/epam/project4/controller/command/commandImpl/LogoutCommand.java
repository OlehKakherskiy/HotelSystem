package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.exception.SystemException;
import main.java.com.epam.project4.model.entity.User;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class LogoutCommand extends AbstractCommand {

    private static Logger logger = Logger.getLogger(LogoutCommand.class);

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(GlobalContextConstant.USER.getName());
        request.getSession(false).invalidate();

        logger.info(MessageFormat.format("User (id = {0}) is logged out", user.getIdUser()));
        try {
            response.sendRedirect(String.format("%s%s", request.getContextPath(), WebPageConstant.LOGIN.getPath()));
        } catch (IOException e) {
            throw new SystemException(MessageCode.GENERAL_SYSTEM_EXCEPTION);
        }
        return "";
    }
}
