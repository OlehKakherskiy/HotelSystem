package manager.managerImpl;

import app.constants.CommandConstant;
import controller.command.AbstractCommand;
import controller.command.commandImpl.*;
import manager.GenericManager;

import java.util.HashMap;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class StubCommandFactory extends GenericManager<CommandConstant, AbstractCommand, AbstractCommand> {


    public StubCommandFactory() {
        super(null);
        keyObjectTemplateMap = new HashMap<CommandConstant, AbstractCommand>() {{
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
        return (V) keyObjectTemplateMap.get(key);
    }

}