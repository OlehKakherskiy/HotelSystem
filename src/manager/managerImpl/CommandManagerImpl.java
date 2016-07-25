package manager.managerImpl;

import app.GlobalContext;
import app.constants.CommandConstant;
import app.constants.GlobalContextConstant;
import manager.AbstractCommandManager;
import controller.command.AbstractCommand;
import model.exceptions.ManagerConfigException;
import model.service.manager.GenericServiceManager;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class CommandManagerImpl extends AbstractCommandManager {

    public CommandManagerImpl(Map<CommandConstant, Class<? extends AbstractCommand>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }

    @Override
    protected <V extends AbstractCommand> V getObjectHook(Class<V> objectClass) throws ManagerConfigException{
        V res = null;
        try {
            res = objectClass.newInstance();
        } catch (InstantiationException e) {
            throw new ManagerConfigException("Exception caused while was attempt to instantiate object from no-params constructor of class "+objectClass,e);
        } catch (IllegalAccessException e) {
            throw new ManagerConfigException("Exception caused because was attempt to instantiate object from inaccessible no-params constructor of class "+objectClass,e);
        }
        res.setServiceManager((GenericServiceManager) GlobalContext.getValue(GlobalContextConstant.SERVICE_MANAGER));
        return res;
    }
}
