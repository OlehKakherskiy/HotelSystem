package main.java.com.hotelSystem.manager;

import main.java.com.hotelSystem.service.IService;

import java.util.Map;

/**
 * Class manages {@link IService} hierarchy's lifecycle. The key object is {@link Class} object,
 * which subclass instance user want to get. Manager uses intermediate type - target object's {@link Class} type.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see IService
 */
public abstract class AbstractServiceManager extends GenericClassCachingManager<Class<? extends IService>, IService> {

    public AbstractServiceManager(Map<Class<? extends IService>, Class<? extends IService>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }
}