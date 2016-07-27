package main.java.com.epam.project4.model.dao;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface GenericDao<T, K> {

    T read(K id);

    K save(T object);

    boolean delete(K id);

    boolean update(T object);
}
