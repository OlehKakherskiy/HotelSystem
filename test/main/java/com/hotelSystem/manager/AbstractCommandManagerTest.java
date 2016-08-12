package main.java.com.hotelSystem.manager;

import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.controller.command.AbstractCommand;
import main.java.com.hotelSystem.controller.command.ICommand;
import main.java.com.hotelSystem.controller.command.commandImpl.FillNewReservationCommand;
import main.java.com.hotelSystem.controller.command.commandImpl.GetReservationListCommand;
import main.java.com.hotelSystem.controller.command.commandImpl.LogoutCommand;
import main.java.com.hotelSystem.controller.command.commandImpl.RefuseHotelRoomOfferCommand;
import main.java.com.hotelSystem.exception.SystemException;
import main.java.com.hotelSystem.manager.managerImpl.CommandManagerImpl;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class AbstractCommandManagerTest {

    private static AbstractCommandManager commandManager;

    private static Map<CommandConstant, Class<? extends ICommand>> map;

    @BeforeClass
    public static void init() {
        map = new HashMap<>();
        map.put(CommandConstant.LOGOUT_COMMAND, LogoutCommand.class);
        map.put(CommandConstant.REFUSE_HOTEL_ROOM_OFFER_COMMAND, RefuseHotelRoomOfferCommand.class);
        map.put(CommandConstant.GET_RESERVATION_LIST_COMMAND, GetReservationListCommand.class);
        map.put(CommandConstant.FILL_NEW_RESERVATION_COMMAND, FillNewReservationCommand.class);

        commandManager = new CommandManagerImpl(map);
    }

    @Test(expected = SystemException.class)
    public void testGetInstanceOfNull() throws Exception {
        commandManager.getInstance(null);
    }

    @Test(expected = SystemException.class)
    public void testGetInstanceWithoutDefaultConstructor() throws Exception {
        Map<CommandConstant, Class<? extends ICommand>> dirtiedMap = new HashMap<>();
        map.entrySet().stream().forEach(entry -> dirtiedMap.put(entry.getKey(), entry.getValue()));

        class StubCommand extends AbstractCommand{
            public StubCommand(HashMap<String, Object> stub) {
            }

            @Override
            public String process(HttpServletRequest request, HttpServletResponse response) {
                return null;
            }
        }
        dirtiedMap.put(CommandConstant.LOGIN_COMMAND, StubCommand.class);

        AbstractCommandManager newCommandManager = new CommandManagerImpl(dirtiedMap);

        newCommandManager.getInstance(CommandConstant.LOGIN_COMMAND);
    }

}