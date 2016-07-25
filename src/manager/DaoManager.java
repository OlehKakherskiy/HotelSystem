package manager;

import model.dao.GenericDao;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class DaoManager extends GenericCachingClassManager<Class<? extends GenericDao>, GenericDao> {

    public DaoManager(Map<Class<? extends GenericDao>, Class<? extends GenericDao>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }
}