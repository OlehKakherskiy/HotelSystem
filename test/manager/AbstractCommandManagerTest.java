package manager;

import app.constants.CommandConstant;
import controller.command.AbstractCommand;
import controller.command.commandImpl.FillNewReservationCommand;
import controller.command.commandImpl.GetReservationListCommand;
import controller.command.commandImpl.LogoutCommand;
import controller.command.commandImpl.RefuseHotelRoomOfferCommand;
import manager.managerImpl.CommandManagerImpl;
import org.junit.Assert;
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

    private static Map<CommandConstant, Class<? extends AbstractCommand>> map;

    @BeforeClass
    public static void init() {

        map = new HashMap<>();
        map.put(CommandConstant.LOGOUT_COMMAND, LogoutCommand.class);
        map.put(CommandConstant.REFUSE_HOTEL_ROOM_OFFER_COMMAND, RefuseHotelRoomOfferCommand.class);
        map.put(CommandConstant.GET_RESERVATION_LIST_COMMAND, GetReservationListCommand.class);
        map.put(CommandConstant.FILL_NEW_RESERVATION_COMMAND, FillNewReservationCommand.class);

        commandManager = new CommandManagerImpl(map);
    }

    @Test
    public void testGetInstance() throws Exception {
        for (Map.Entry<CommandConstant, Class<? extends AbstractCommand>> entry : map.entrySet()) {
            System.out.println(entry.getKey());
            Assert.assertEquals(entry.getValue(), commandManager.getInstance(entry.getKey()).getClass());
        }
    }

    @Test(expected = model.exceptions.SystemException.class)
    public void testGetInstanceOfNull() throws Exception {
        commandManager.getInstance(null);
    }

    @Test(expected = model.exceptions.SystemException.class)
    public void testGetInstanceWithoutDefaultConstructor() throws Exception {
        Map<CommandConstant, Class<? extends AbstractCommand>> dirtiedMap = new HashMap<>();
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