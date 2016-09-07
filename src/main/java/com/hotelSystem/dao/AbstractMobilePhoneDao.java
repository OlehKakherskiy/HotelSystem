package main.java.com.hotelSystem.dao;

import main.java.com.hotelSystem.exception.DaoException;
import main.java.com.hotelSystem.model.MobilePhone;
import main.java.com.hotelSystem.model.User;

import java.util.List;

/**
 * Interface extends basic CRUD operations which are performed whith {@link MobilePhone} entity.
 * Defines additional operation, which allows to get all {@link MobilePhone} objects of specific user.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see MobilePhone
 */
public interface AbstractMobilePhoneDao extends GenericDao<MobilePhone> {

    /**
     * Returns all {@link MobilePhone} objects which are connected with
     * {@link User}, represented
     * by it's id.
     *
     * @param userId id of target {@link User} object
     * @return mobile phone list of user with target user's id
     * @throws DaoException if exception was caused during processing operation with persistent storage
     */
    List<MobilePhone> getAll(int userId) throws DaoException;

}
