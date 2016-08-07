package main.java.com.epam.project4.manager;

import main.java.com.epam.project4.model.dao.GenericDao;

import java.util.Map;

/**
 * Class manages {@link GenericDao} hierarchy's lifecycle. The key object is {@link Class} object,
 * which subclass instance user want to get. Manager uses intermediate type - target
 * object's {@link Class} type.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see GenericDao
 */
public abstract class AbstractDaoManager extends GenericClassCachingManager<Class<? extends GenericDao>, GenericDao> {

    public AbstractDaoManager(Map<Class<? extends GenericDao>, Class<? extends GenericDao>> keyObjectTemplateMap) {
        super(keyObjectTemplateMap);
    }
}