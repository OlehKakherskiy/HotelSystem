package model.dao.manager.managerImpl;

import model.dao.GenericDao;
import model.dao.manager.DaoManager;

import java.util.Map;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class DaoManagerImpl extends DaoManager {


    public DaoManagerImpl(Map<Class<? extends GenericDao>, Class<? extends GenericDao>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }

    @Override
    protected <V extends GenericDao> V getObjectHook(Class<V> objectClass) {
        try {
            return objectClass.newInstance(); //TODO: exception handling
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
