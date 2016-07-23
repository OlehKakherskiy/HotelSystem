package model.service.serviceImpl;

import model.dao.GenericUserDao;
import model.entity.User;
import model.service.GenericUserService;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class UserService extends GenericUserService {

    public UserService(GenericUserDao dao) {
        super(dao);
    }

    @Override
    public User login(String login, String password) {
        int userID = dao.tryLogin(login, password);
        return (userID == -1) ? null : getSimpleUserInfo(userID); //TODO: or exception
    }

    @Override
    public User getSimpleUserInfo(int ID) {
        return dao.read(ID);
    }
}
