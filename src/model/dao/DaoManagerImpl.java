package model.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class DaoManagerImpl implements DaoManager {

    private Map<Class<? extends GenericDao>, Class<? extends GenericDao>> daoTemplateMap;

    private Map<Class<? extends GenericDao>, GenericDao> daoInstances;

    public DaoManagerImpl(Set<Class<? extends GenericDao>> daoTypes) {
        //TODO: nullCheck, emptiness check.
        this.daoTemplateMap = reformatToMap(daoTypes);
        daoInstances = new HashMap<>();

    }

    public <T extends GenericDao> T getDAO(Class<T> daoType) throws InstantiationException, IllegalAccessException {
        Class<T> template = (Class<T>) daoTemplateMap.get(daoType);

        if (template == null) {
            throw new IllegalArgumentException(); //TODO: ввести свою ошибку - нет такого класса в шаблонах
        }

        T result = (T) daoInstances.get(daoType);
        return (result == null) ? newInstance(template) : result;
    }

    private <T extends GenericDao> T newInstance(Class<T> daoClass) throws IllegalAccessException, InstantiationException {
        T instance = daoClass.newInstance();
        daoInstances.put(daoClass, instance);
        return instance;
    }

    private Map<Class<? extends GenericDao>, Class<? extends GenericDao>> reformatToMap(Set<Class<? extends GenericDao>> templateList) {
        Map<Class<? extends GenericDao>, Class<? extends GenericDao>> result = new HashMap<>();
        templateList.forEach(clazz -> result.put(clazz, clazz));
        return result;
    }
}
