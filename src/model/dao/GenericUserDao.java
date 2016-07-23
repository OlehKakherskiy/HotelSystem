package model.dao;

import model.entity.User;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericUserDao extends TransparentGenericDao<User, Integer> {

    public abstract User tryLogin(String login, String password);
}
