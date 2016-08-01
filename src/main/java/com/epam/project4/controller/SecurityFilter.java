package main.java.com.epam.project4.controller;

import main.java.com.epam.project4.app.GlobalContext;
import main.java.com.epam.project4.app.constants.CommandConstant;
import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.app.constants.WebPageConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.manager.AbstractCommandManager;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.UserType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class SecurityFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        if (session == null) {
            ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + WebPageConstant.LOGIN);
        } else {
            User user = (User) session.getAttribute(GlobalContextConstant.USER.getName());
            String commandName = request.getParameter(GlobalContextConstant.COMMAND_NAME.getName());
            if (commandName != null && !commandName.isEmpty() && CommandConstant.fromValue(commandName) != CommandConstant.LOGIN_COMMAND
                    && !hasRights(user, CommandConstant.fromValue(commandName))) {
                redirectToIndex(request, response);
            } else {
                chain.doFilter(request, response);
            }
        }
    }

    @Override
    public void destroy() {

    }

    private boolean hasRights(User user, CommandConstant commandName) {
        Map<UserType, List<CommandConstant>> secureRights = (Map<UserType, List<CommandConstant>>) GlobalContext.getValue(GlobalContextConstant.SECURE_CONFIGURATION);
        if (!secureRights.containsKey(user.getUserType()))
            return false;
        CommandConstant command = secureRights.get(user.getUserType()).stream().filter(commandConstant -> commandConstant == commandName).findFirst().orElse(null);
        return command != null;
    }

    private void redirectToIndex(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        AbstractCommand indexPageCommand = ((AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY))
                .getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND);
        request.getRequestDispatcher(indexPageCommand.process((HttpServletRequest) request, (HttpServletResponse) response)).forward(request, response);
    }
}
