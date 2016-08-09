package main.java.com.epam.project4.controller.command;

import main.java.com.epam.project4.manager.AbstractServiceManager;

/**
 * Basic command, which just implements {@link ICommand#setServiceManager(AbstractServiceManager)}
 * method
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see AbstractServiceManager
 */
public abstract class AbstractCommand implements ICommand {

    /**
     * service manager is used for instantiating {@link main.java.com.epam.project4.model.service.IService}
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
