package main.java.com.epam.project4.model.dao;

import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.model.entity.User;

/**
 * Interface extends basic CRUD operations which are performed whith {@link User} entity.
 * Defines additional operation, which allows to get {@link User} object using inputted
 * login and password. This operation can be used in login operation implementation
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see User
 */
public interface AbstractUserDao extends GenericDao<User> {

    /**
     * Reads {@link User} object using inputted login and password params.
     *
     * @param login    user's login
     * @param password user's password
     * @return User object, that is mapped to target login and password
     * @throws DaoException if there was an exception during processing
     *                      operation with target persistence storage, or if login or password is null
     */
    User tryLogin(String login, String password) throws DaoException;
}
