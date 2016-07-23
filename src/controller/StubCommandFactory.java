package controller;

import app.constants.CommandConstant;
import controller.command.GenericCommand;
import controller.command.commandImpl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class StubCommandFactory implements CommandFactory {

    Map<CommandConstant, GenericCommand> commandMap;

    public StubCommandFactory(Map<CommandConstant, GenericCommand> commandMap) {
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
    public GenericCommand getCommandInstance(CommandConstant commandID) {
        return commandMap.get(commandID);
    }

}
