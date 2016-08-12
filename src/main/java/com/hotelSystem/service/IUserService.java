package main.java.com.hotelSystem.service;

import main.java.com.hotelSystem.exception.RequestException;
import main.java.com.hotelSystem.exception.SystemException;
import main.java.com.hotelSystem.model.User;

/**
 * Interface represents API for executing operations, assosiated with {@link User} entity
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see User
 */
public interface IUserService extends IService {

    /**
     * returns {@link User} object, assosiated with target signIn and password.
     * If there's no user with target combination -  throws
     * {@link RequestException}.
     * Also adds mobile phones, assosiated with current user
     *
     * @param login    user's signIn
     * @param password user's password
     * @return {@link User} object, assosiated with current signIn and password
     * @throws RequestException if there's no
     *                                                                combination of signIn and password
     * @throws SystemException  if exception was
     *                                                                thrown during processing any underlying operation
     */
    User signIn(String login, String password);

    /**
     * Returns {@link User} object, assosiated with target id parameter.
     * If there's no User with target id - throws
     * {@link RequestException}.
     * Also adds mobile phones, assosiated with current user.
     *
     * @param id User's id
     * @return {@link User} object, assosiated with target id parameter.
     * @throws RequestException if there's no
     *                                                                user with current id
     * @throws SystemException  if exception was
     *                                                                thrown during processing any underlying operation
     */
    User getUserInfo(int id);

}