package controller.command.commandImpl;

import app.constants.WebPageConstant;
import controller.command.AbstractCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class LogoutCommand extends AbstractCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        request.getSession(false).invalidate();
        return WebPageConstant.LOGIN.getPath();
    }
}
