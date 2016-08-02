package main.java.com.epam.project4.manager;

import main.java.com.epam.project4.model.dao.GenericDao;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class AbstractDaoManager extends GenericClassCachingManager<Class<? extends GenericDao>, GenericDao> {

    public AbstractDaoManager(Map<Class<? extends GenericDao>, Class<? extends GenericDao>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }
}