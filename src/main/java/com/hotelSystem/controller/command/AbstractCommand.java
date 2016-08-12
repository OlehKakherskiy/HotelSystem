package main.java.com.hotelSystem.controller.command;

import main.java.com.hotelSystem.manager.AbstractServiceManager;
import main.java.com.hotelSystem.service.IService;

/**
 * Basic command, which just implements {@link ICommand#setServiceManager(AbstractServiceManager)}
 * method
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see AbstractServiceManager
 */
public abstract class AbstractCommand implements ICommand {

    /**
     * service manager is used for instantiating {@link IService}
     * object via dependency lookup principle
     */
    protected AbstractServiceManager serviceManager;

    /**
     * {@inheritDoc}
     *
     * @param serviceManager service manager {@inheritDoc}
     */
    public void setServiceManager(AbstractServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }
}
