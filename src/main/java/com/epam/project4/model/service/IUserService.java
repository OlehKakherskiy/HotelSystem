package main.java.com.epam.project4.model.service;

import main.java.com.epam.project4.model.entity.User;

/**
 * Interface represents API for executing operations, assosiated with {@link User} entity
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see User
 */
public interface IUserService extends IService {

    /**
     * returns {@link User} object, assosiated with target login and password.
     * If there's no user with target combination -  throws
     * {@link main.java.com.epam.project4.exception.RequestException}.
     * Also adds mobile phones, assosiated with current user
     *
     * @param login    user's login
     * @param password user's password
     * @return {@link User} object, assosiated with current login and password
     * @throws main.java.com.epam.project4.exception.RequestException if there's no
     *                                                                combination of login and password
     * @throws main.java.com.epam.project4.exception.SystemException  if exception was
     *                                                                thrown during processing any underlying operation
     */
    User login(String login, String password);

    /**
     * Returns {@link User} object, assosiated with target id parameter.
     * If there's no User with target id - throws
     * {@link main.java.com.epam.project4.exception.RequestException}.
     * Also adds mobile phones, assosiated with current user.
     *
     * @param id User's id
     * @return {@link User} object, assosiated with target id parameter.
     * @throws main.java.com.epam.project4.exception.RequestException if there's no
     *                                                                user with current id
     * @throws main.java.com.epam.project4.exception.SystemException  if exception was
     *                                                                thrown during processing any underlying operation
     */
    User getUserInfo(int id);

}