package manager.managerImpl;

import app.constants.CommandConstant;
import manager.AbstractCommandManager;
import controller.command.commandImpl.*;
import controller.command.AbstractCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class StubCommandFactory extends AbstractCommandManager {

    Map<CommandConstant, AbstractCommand> commandMap;

    public StubCommandFactory() {
        super(null);
        commandMap = new HashMap<CommandConstant, AbstractCommand>() {{
            put(CommandConstant.LOGIN_COMMAND, new LoginCommand());
            put(CommandConstant.FILL_NEW_RESERVATION_COMMAND, new FillNewReservationCommand());
            put(CommandConstant.GET_RESERVATION_FULL_INFO_COMMAND, new GetReservationFullInfoCommand());
            put(CommandConstant.GET_RESERVATION_LIST_COMMAND, new GetReservationListCommand());
            put(CommandConstant.LOGOUT_COMMAND, new LogoutCommand());
            put(CommandConstant.REFUSE_HOTEL_ROOM_OFFER_COMMAND, new RefuseHotelRoomOfferCommand());
            put(CommandConstant.SUBMIT_HOTEL_ROOM_OFFER_COMMAND, new SubmitHotelRoomOfferCommand());
        }};
    }

    @Override
    public <V extends AbstractCommand> V getInstance(CommandConstant key) {
        return (V) commandMap.get(key);
    }

    @Override
    protected <V extends AbstractCommand> V getObjectHook(Class<V> objectClass){
        return null;
    }
}
