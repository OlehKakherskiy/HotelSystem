package controller.commandManager;

import app.constants.CommandConstant;
import controller.command.GenericCommand;
import model.manager.GenericCachingManager;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class AbstractCommandManager extends GenericCachingManager<CommandConstant, GenericCommand> {

    public AbstractCommandManager(Map<CommandConstant, Class<? extends GenericCommand>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }

}
