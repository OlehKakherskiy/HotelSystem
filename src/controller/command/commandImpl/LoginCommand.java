package controller.command.commandImpl;

import app.constants.GlobalContextConstant;
import controller.command.GenericCommand;
import model.entity.User;
import model.service.GenericUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class LoginCommand extends GenericCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {

        GenericUserService userService = serviceManager.getService(GenericUserService.class);

        String login = request.getParameter(GlobalContextConstant.LOGIN.getName());
        String password = request.getParameter(GlobalContextConstant.PASSWORD.getName());

        User user = userService.login(login, password);
        if (user == null) {
            //TODO: invalid login or password
        } else {
            HttpSession httpSession = request.getSession(true);
            httpSession.setAttribute(GlobalContextConstant.USER.getName(), user);
            //TODO: переходы на страницы и добавление инфы
        }
        return "WEB-INF/index.jsp";
    }
}
