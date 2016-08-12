package main.java.com.hotelSystem.manager.managerImpl;

import main.java.com.hotelSystem.app.GlobalContext;
import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.app.constants.GlobalContextConstant;
import main.java.com.hotelSystem.controller.command.ICommand;
import main.java.com.hotelSystem.exception.ManagerConfigException;
import main.java.com.hotelSystem.manager.AbstractCommandManager;
import main.java.com.hotelSystem.manager.AbstractServiceManager;

import java.text.MessageFormat;
import java.util.Map;

/**
 * Manager implementation for {@link ICommand} hierarhy.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class CommandManagerImpl extends AbstractCommandManager {

    private static final String INSTANTIATION_EXCEPTION_MESSAGE = "Exception caused while was attempt " +
            "to instantiate object from no-params constructor of class {0}";

    private static final String ILLEGAL_ACCESS_EXCEPTION_MESSAGE = "Exception caused because was attempt " +
            "to instantiate object from inaccessible no-params constructor of class {0}";

    /**
     * Constructor that initializes {@link #keyObjectTemplateMap} field
     *
     * @param keyObjectTemplateMap key/extra_info for instantiation target object map
     */
    public CommandManagerImpl(Map<CommandConstant, Class<? extends ICommand>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }

    /**
     * Creates new instance of the input parameter's class using {@link Class#newInstance()}.
     * Also injects {@link AbstractServiceManager} instance.
     *
     * @param objectClass {@inheritDoc}
     * @param <V>         {@inheritDoc}
     * @return {@link ICommand} instance
     * @throws ManagerConfigException {@inheritDoc} Also if there is no constructor without parameters
     *                                or it has inappropriate access modificator.
     */
    @Override
    protected <V extends ICommand> V instantiate(Class<V> objectClass) throws ManagerConfigException {
        V res;
        try {
            res = objectClass.newInstance();
        } catch (InstantiationException e) {
            throw new ManagerConfigException(MessageFormat.format(INSTANTIATION_EXCEPTION_MESSAGE, objectClass), e);
        } catch (IllegalAccessException e) {
            throw new ManagerConfigException(MessageFormat.format(ILLEGAL_ACCESS_EXCEPTION_MESSAGE, objectClass), e);
        }
        res.setServiceManager((AbstractServiceManager) GlobalContext.getValue(GlobalContextConstant.SERVICE_MANAGER));
        return res;
    }
}
