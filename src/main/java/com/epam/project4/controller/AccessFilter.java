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
 * Filter checks users rights for
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class AccessFilter implements Filter {

    /**
     * application's security map, that maps user's type to acessed commands
     */
    private Map<UserType, List<CommandConstant>> accessRights;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Checks user's rights for invoking command with target name. If there's no client's
     * session, returns client to {@link WebPageConstant#LOGIN} jsp. Otherwise checks rights.
     * Takes from request parameter with the key {@link GlobalContextConstant#COMMAND_NAME},
     * if it's null or epmpty or user doesn't have rights for inoking this type of command,
     * {@link #redirectToIndex(ServletRequest, ServletResponse)} will be executed.
     *
     * @param request  {@inheritDoc}
     * @param response {@inheritDoc}
     * @param chain    {@inheritDoc}
     * @throws IOException      {@inheritDoc}
     * @throws ServletException {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        if (session == null) {
            ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + WebPageConstant.LOGIN);
        } else {
            User user = (User) session.getAttribute(GlobalContextConstant.USER.getName());
            String commandName = request.getParameter(GlobalContextConstant.COMMAND_NAME.getName());
            if (commandName != null && !commandName.isEmpty() && (CommandConstant.fromValue(commandName) == CommandConstant.LOGIN_COMMAND || hasRights(user, CommandConstant.fromValue(commandName)))) {
                chain.doFilter(request, response);
            } else {
                System.out.println("access denied for " + user.getUserType() + " for command " + commandName);
                redirectToIndex(request, response);
            }
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * Checks whether target user has rigths for invoking command mapped to target commandName.
     * <p>
     * Gets security configuration from {@link GlobalContext}.
     * </p>
     *
     * @param user        user, which rights will be checked
     * @param commandName command's key, for which target user's rights will be checked
     * @return true, if user can invoke command, mapped to target commandName, otherwise - false
     */
    private boolean hasRights(User user, CommandConstant commandName) {
        accessRights = (accessRights != null) ? accessRights : (Map<UserType, List<CommandConstant>>)
                GlobalContext.getValue(GlobalContextConstant.SECURITY_CONFIGURATION);
        System.out.println(accessRights);
        if (!accessRights.containsKey(user.getUserType()))
            return false;
        CommandConstant command = accessRights.get(user.getUserType()).stream()
                .filter(commandConstant -> commandConstant == commandName)
                .findFirst()
                .orElse(null);
        return command != null;
    }

    /**
     * Redirects client to jsp, returned as a result of command with key {@link CommandConstant#GET_RESERVATION_LIST_COMMAND}
     *
     * @param request  http request
     * @param response http response
     * @throws ServletException if the target resource throws this exception
     * @throws IOException      if the target resource throws this exception
     */
    private void redirectToIndex(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        AbstractCommand indexPageCommand = ((AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY))
                .getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND);
        request.getRequestDispatcher(indexPageCommand.process((HttpServletRequest) request, (HttpServletResponse) response)).forward(request, response);
    }
}
