package model.service;

import model.entity.User;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface AbstractUserService extends AbstractService {

    User login(String login, String password);

    User getSimpleUserInfo(int ID);

}