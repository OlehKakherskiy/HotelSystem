package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.app.constants.WebPageConstant;
import main.java.com.hotelSystem.controller.command.AbstractCommand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Oleg on 11.09.2016.
 */
public class PreparePasswordRecoveryForm extends AbstractCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession httpSession = request.getSession(true);
        String lang = request.getParameter("language");
        System.out.println("lang = " + lang);
        httpSession.setAttribute(GlobalContextConstant.CURRENT_LOCALE.getName(),
                (lang != null) ? lang : request.getLocale().toString());
        return WebPageConstant.PASSWORD_RECOVERY_FORM.getPath();
    }
}
