package model.dao;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface GenericDao<T, K> {

    T read(K id);

    T save(T object);

    boolean delete(K id);

    boolean update(T object);

    List<T> getAll();

}
