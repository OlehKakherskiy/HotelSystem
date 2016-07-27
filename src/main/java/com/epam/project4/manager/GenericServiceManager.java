package main.java.com.epam.project4.manager;

import main.java.com.epam.project4.model.service.AbstractService;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericServiceManager extends GenericCachingClassManager<Class<? extends AbstractService>, AbstractService> {

    public GenericServiceManager(Map<Class<? extends AbstractService>, Class<? extends AbstractService>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }
}
//TODO: мб убрать?