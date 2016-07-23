package model.service;

import model.dao.GenericUserDao;
import model.entity.User;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public abstract class GenericUserService extends GenericService<GenericUserDao, User, Integer> {

    public GenericUserService(GenericUserDao dao) {
        super(dao);
    }

    public abstract User login(String login, String password);

    public abstract User getSimpleUserInfo(int ID);

}