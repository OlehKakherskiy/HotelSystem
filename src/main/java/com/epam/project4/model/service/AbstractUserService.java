package main.java.com.epam.project4.model.service;

import main.java.com.epam.project4.model.entity.User;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public interface AbstractUserService extends AbstractService {

    User login(String login, String password);

    User getSimpleUserInfo(int ID);

}