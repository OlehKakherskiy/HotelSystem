package controller;

import app.constants.CommandConstant;
import controller.command.GenericCommand;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface CommandFactory {

    GenericCommand getCommandInstance(CommandConstant commandID);
}
