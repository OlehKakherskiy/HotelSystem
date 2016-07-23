package controller;

import app.constants.CommandConstant;
import controller.command.GenericCommand;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class CommandFactoryImpl implements CommandFactory {

    private Map<CommandConstant, GenericCommand> commandMap;

    @Override
    public GenericCommand getCommandInstance(CommandConstant commandID) {
        return null;
    }
}
