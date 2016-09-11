package main.java.com.hotelSystem.controller.filter;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.app.constants.WebPageConstant;
import main.java.com.hotelSystem.controller.command.AbstractCommand;
import main.java.com.hotelSystem.manager.AbstractCommandManager;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.model.enums.UserType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
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

    private List<CommandConstant> whiteList;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        whiteList = Arrays.asList(CommandConstant.PASSWORD_RECOVERY_COMMAND,
                CommandConstant.REGISTRATION_COMMAND,
                CommandConstant.GET_PASSWORD_RECOVERY_FORM_COMMAND,
                CommandConstant.GET_REGISTRATION_FORM_COMMAND);
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
        String commandName = request.getParameter(GlobalContextConstant.COMMAND_NAME.getName());
        CommandConstant commandConstant = (commandName != null && !commandName.isEmpty())
                ? CommandConstant.fromValue(commandName)
                : null;
        if (commandConstant == null || whiteList.contains(commandConstant)) { //no command - nothing to check (or in white list)
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        if (session == null) {
            ((HttpServletResponse) response).sendRedirect(((HttpServletRequest) request).getContextPath() + WebPageConstant.LOGIN);
        } else {
            User user = (User) session.getAttribute(GlobalContextConstant.USER.getName());
            if (commandConstant == CommandConstant.LOGIN_COMMAND || hasRights(user, commandConstant)) {
                chain.doFilter(request, response);
            } else {
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
