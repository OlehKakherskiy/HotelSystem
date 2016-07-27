package main.java.com.epam.project4.model.dao;

import main.java.com.epam.project4.model.entity.User;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericUserDao extends TransparentGenericDao<User, Integer> {

    public abstract User tryLogin(String login, String password);
}
