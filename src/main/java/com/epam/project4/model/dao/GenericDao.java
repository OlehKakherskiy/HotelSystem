package main.java.com.epam.project4.model.dao;

import main.java.com.epam.project4.exception.DaoException;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface GenericDao<T> {

    T read(int id) throws DaoException;

    int save(T object) throws DaoException;

    boolean delete(int id) throws DaoException;

    boolean update(T object) throws DaoException;
}
