package controller.commandManager.managerImpl;

import app.constants.CommandConstant;
import controller.commandManager.GenericCommandManager;
import controller.command.commandImpl.*;
import controller.command.GenericCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class StubCommandFactory extends GenericCommandManager {

    Map<CommandConstant, GenericCommand> commandMap;

    public StubCommandFactory() {
        super(null);
        commandMap = new HashMap<CommandConstant, GenericCommand>() {{
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
    public <V extends GenericCommand> V getInstance(CommandConstant key) {
        return (V) commandMap.get(key);
    }

    @Override
    protected <V extends GenericCommand> V getObjectHook(Class<V> objectClass) throws IllegalAccessException, InstantiationException {
        return null;
    }
}
