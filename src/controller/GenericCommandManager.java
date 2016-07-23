package controller;

import app.constants.CommandConstant;
import controller.manager.GenericCommand;
import model.manager.GenericCachingManager;
import model.manager.GenericManager;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericCommandManager extends GenericCachingManager<CommandConstant, GenericCommand> {

    public GenericCommandManager(Map<CommandConstant, Class<? extends GenericCommand>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }

}
