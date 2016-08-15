package main.java.com.hotelSystem.controller.command.commandImpl;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.controller.command.AbstractCommand;
import main.java.com.hotelSystem.manager.AbstractCommandManager;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class UpdateUserCommand extends AbstractCommand {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession(false).getAttribute(GlobalContextConstant.USER.getName());
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        validate(name);
        validate(surname);

        User userClone = null;
        try {
            userClone = user.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        IUserService userService = serviceManager.getInstance(IUserService.class);
        userService.update(userClone, name, surname, getMobilePhones(request));
        request.getSession(false).setAttribute(GlobalContextConstant.USER.getName(), userClone);

        return ((AbstractCommandManager) GlobalContext.getValue(GlobalContextConstant.COMMAND_FACTORY))
                .getInstance(CommandConstant.GET_RESERVATION_LIST_COMMAND).process(request, response);
    }

    private List<String> getMobilePhones(HttpServletRequest request) {
        List<String> res = new ArrayList<>();
        String current = null;
        int i = 0;
        do {
            current = request.getParameter("mobilePhone" + i++);
            if (current != null) {
                res.add(current);
            } else break;
        } while (current != null);
        return res;
    }

    private void validate(String string) {
        if (string == null || string.isEmpty()) {
            throw new RuntimeException("name or surname can't be null or epmty");
        }
    }
}