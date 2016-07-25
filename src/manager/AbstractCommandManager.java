package manager;

import app.constants.CommandConstant;
import controller.command.AbstractCommand;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class AbstractCommandManager extends GenericCachingManager<CommandConstant, AbstractCommand> {

    public AbstractCommandManager(Map<CommandConstant, Class<? extends AbstractCommand>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }

}
