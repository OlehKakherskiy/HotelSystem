package controller.commandManager.managerImpl;

import app.GlobalContext;
import app.constants.CommandConstant;
import app.constants.GlobalContextConstant;
import controller.commandManager.GenericCommandManager;
import controller.command.GenericCommand;
import model.service.manager.GenericServiceManager;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class CommandManagerImpl extends GenericCommandManager {

    public CommandManagerImpl(Map<CommandConstant, Class<? extends GenericCommand>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }

    @Override
    protected <V extends GenericCommand> V getObjectHook(Class<V> objectClass) throws IllegalAccessException, InstantiationException {
        V res = objectClass.newInstance();
        res.setServiceManager((GenericServiceManager) GlobalContext.getValue(GlobalContextConstant.SERVICE_MANAGER));
        return res;
    }
}
