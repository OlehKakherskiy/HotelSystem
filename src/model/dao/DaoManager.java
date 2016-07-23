package model.dao;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface DaoManager {

    <T extends GenericDao> T getDAO(Class<T> daoType) throws InstantiationException, IllegalAccessException;
}
