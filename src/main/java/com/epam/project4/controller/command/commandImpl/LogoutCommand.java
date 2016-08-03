package main.java.com.epam.project4.controller.command.commandImpl;

import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.exception.SystemException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class LogoutCommand extends AbstractCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        request.getSession(false).invalidate();

        try {
            response.sendRedirect(String.format("%s%s", request.getContextPath(), WebPageConstant.LOGIN.getPath()));
        } catch (IOException e) {
            throw new SystemException(MessageCode.GENERAL_SYSTEM_EXCEPTION);
        }
        return "";
    }
}
