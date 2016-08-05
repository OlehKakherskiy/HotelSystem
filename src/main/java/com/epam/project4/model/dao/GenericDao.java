package main.java.com.epam.project4.model.dao;

import main.java.com.epam.project4.exception.DaoException;

import java.text.MessageFormat;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface GenericDao<T> {

    default T read(int id) throws DaoException {
        throw new DaoException(MessageFormat.format("Read operation is not supported by {1} type of dao",
                this.getClass().getName()));
    }

    default int save(T object) throws DaoException {
        throw new DaoException(MessageFormat.format("Save operation is not supported by {1} type of dao",
                this.getClass().getName()));
    }

    default boolean delete(int id) throws DaoException {
        throw new DaoException(MessageFormat.format("Delete operation is not supported by {1} type of dao",
                this.getClass().getName()));
    }

    default boolean update(T object) throws DaoException {
        throw new DaoException(MessageFormat.format("Update operation is not supported by {1} type of dao",
                this.getClass().getName()));
    }
}
