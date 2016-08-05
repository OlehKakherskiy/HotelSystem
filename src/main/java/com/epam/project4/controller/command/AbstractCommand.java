package main.java.com.epam.project4.controller.command;

import main.java.com.epam.project4.manager.AbstractServiceManager;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class AbstractCommand implements ICommand {

    protected AbstractServiceManager serviceManager;

    public void setServiceManager(AbstractServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }
}
