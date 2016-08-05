package main.java.com.epam.project4.manager;

import main.java.com.epam.project4.app.constants.CommandConstant;
import main.java.com.epam.project4.controller.command.ICommand;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class AbstractCommandManager extends GenericClassCachingManager<CommandConstant, ICommand> {

    public AbstractCommandManager(Map<CommandConstant, Class<? extends ICommand>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }

}
