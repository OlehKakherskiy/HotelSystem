package model.service;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class ServiceManagerImpl implements ServiceManager {

    Map<Class<? extends GenericService>, Class<? extends GenericService>> serviceTemplates;

    Map<Class<? extends GenericService>, ? extends GenericService> serviceInstance;

    @Override
    public <T extends GenericService> T getService(Class<T> serviceType) {
        return null;
    }

}
