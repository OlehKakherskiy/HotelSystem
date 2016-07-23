package model.service.serviceImpl;

import model.dao.GenericMobilePhoneDao;
import model.dao.GenericUserDao;
import model.entity.User;
import model.service.AbstractUserService;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class UserService implements AbstractUserService {

    private GenericUserDao userDao;

    private GenericMobilePhoneDao mobilePhoneDao;

    public UserService(GenericUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User login(String login, String password) {
        User result = userDao.tryLogin(login, password);
        if (result == null) {
            throw new RuntimeException(); //TODO: переделать
        }
        result.setMobilePhoneList(mobilePhoneDao.getAll(result.getIdUser()));
        return result;
    }

    @Override
    public User getSimpleUserInfo(int ID) {
        User user = userDao.read(ID);
        user.setMobilePhoneList(mobilePhoneDao.getAll(user.getIdUser()));
        return user;
    }
}
