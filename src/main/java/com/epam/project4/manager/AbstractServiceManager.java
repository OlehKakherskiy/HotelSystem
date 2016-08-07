package main.java.com.epam.project4.manager;

import main.java.com.epam.project4.model.service.AbstractService;

import java.util.Map;

/**
 * Class manages {@link AbstractService} hierarchy's lifecycle. The key object is {@link Class} object,
 * which subclass instance user want to get. Manager uses intermediate type - target object's {@link Class} type.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see AbstractService
 */
public abstract class AbstractServiceManager extends GenericClassCachingManager<Class<? extends AbstractService>, AbstractService> {

    public AbstractServiceManager(Map<Class<? extends AbstractService>, Class<? extends AbstractService>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }
}