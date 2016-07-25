package controller.command.commandImpl;

import app.constants.WebPageConstant;
import controller.command.GenericCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class LogoutCommand extends GenericCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        request.getSession(false).invalidate();
        return WebPageConstant.LOGIN.getPath();
    }
}
