package main.java.com.epam.project4.model.dao;

import main.java.com.epam.project4.exception.DaoException;

import java.text.MessageFormat;

/**
 * Interface represents root of the DAO's hierarchy. It declares basic CRUD operations via
 * the interaction with persistence storage and parametrised with generic type,
 * which represents target entity.
 * <p>
 * All methods throw {@link DaoException}, because they don't have default implementation,
 * but there will be a situation, when there's no need to implement all CRUD operations in subtypes.
 * So Java8 defaults method were used to reduce boilerplating throw-exception implementation in
 * subclasses and to remove transparent DAO implementation, that will be an abstract class and
 * will have throw-exception implementation for this methods. Therefore all entities' DAOs can be
 * declared as interfaces (which is preferable because of
 * Dynamic {@link java.lang.reflect.Proxy} restrictions)
 * </p>
 *
 * @param <T> target entity, to which CRUD operations will be performed
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface GenericDao<T> {

    /**
     * Creates object using data read from persistent storage with specific id.
     *
     * @param id target object's id, which persistenced data will be mapped to object representation
     * @return Object representation of data read from persistence storage with current id
     * @throws DaoException if any type of exception will be caused during this operation processing
     */
    default T read(int id) throws DaoException {
        throw new DaoException(MessageFormat.format("Read operation is not supported by {1} type of dao",
                this.getClass().getName()));
    }

    /**
     * Saves target object to persistence storage. Inserts object's created id to it's id field
     *
     * @param object object that will be mapped to persistent storage data format
     * @throws DaoException if any type of exception will be caused during this operation processing
     */
    default void save(T object) throws DaoException {
        throw new DaoException(MessageFormat.format("Save operation is not supported by {1} type of dao",
                this.getClass().getName()));
    }

    /**
     * Removes all data from persistence storage that is connected to target id
     *
     * @param id target id, whose connected data will be removed from storage
     * @return true, if data, connected to target id, was removed. Otherwise - false
     * @throws DaoException if any type of exception will be caused during this operation processing
     */
    default boolean delete(int id) throws DaoException {
        throw new DaoException(MessageFormat.format("Delete operation is not supported by {1} type of dao",
                this.getClass().getName()));
    }

    /**
     * Updates all data saved to the target object in the persistent storage
     *
     * @param object, which data will be mapped to persistent storage format
     * @return true, if data in persistent storage was updated, otherwise - false
     * @throws DaoException if any type of exception will be caused during this operation processing
     */
    default boolean update(T object) throws DaoException {
        throw new DaoException(MessageFormat.format("Update operation is not supported by {1} type of dao",
                this.getClass().getName()));
    }
}
