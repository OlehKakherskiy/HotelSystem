package model.dao.manager;

import model.dao.GenericDao;
import model.manager.GenericCachingManager;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class DaoManager extends GenericCachingManager<Class<? extends GenericDao>, GenericDao> {

    public DaoManager(Map<Class<? extends GenericDao>, Class<? extends GenericDao>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }

}