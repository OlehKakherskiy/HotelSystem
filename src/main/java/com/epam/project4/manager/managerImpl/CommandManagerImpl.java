package main.java.com.epam.project4.manager.managerImpl;

import main.java.com.epam.project4.app.GlobalContext;
import main.java.com.epam.project4.app.constants.CommandConstant;
import main.java.com.epam.project4.app.constants.GlobalContextConstant;
import main.java.com.epam.project4.controller.command.AbstractCommand;
import main.java.com.epam.project4.manager.AbstractCommandManager;
import main.java.com.epam.project4.model.exception.ManagerConfigException;
import main.java.com.epam.project4.manager.GenericServiceManager;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class CommandManagerImpl extends AbstractCommandManager {

    public CommandManagerImpl(Map<CommandConstant, Class<? extends AbstractCommand>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }

    @Override
    protected <V extends AbstractCommand> V instantiate(Class<V> objectClass) throws ManagerConfigException{
        V res;
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
