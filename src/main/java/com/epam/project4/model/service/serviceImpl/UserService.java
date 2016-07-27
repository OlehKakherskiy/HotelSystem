package main.java.com.epam.project4.model.service.serviceImpl;

import main.java.com.epam.project4.model.dao.GenericMobilePhoneDao;
import main.java.com.epam.project4.model.dao.GenericUserDao;
import main.java.com.epam.project4.model.entity.MobilePhone;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.exception.RequestException;
import main.java.com.epam.project4.model.exception.SystemException;
import main.java.com.epam.project4.model.service.AbstractUserService;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class UserService implements AbstractUserService {

    private GenericUserDao userDao;

    private GenericMobilePhoneDao mobilePhoneDao;

    public UserService(GenericUserDao userDao, GenericMobilePhoneDao mobilePhoneDao) {
        this.userDao = userDao;
        this.mobilePhoneDao = mobilePhoneDao;
    }

    @Override
    public User login(String login, String password) {
        User result = userDao.tryLogin(login, password);

        resultIsNullCheck(result);
        noSuchUserCheck(result, ""); //TODO: неверный пользователь или пароль

        appendMobilePhoneList(result);
        return result;
    }

    @Override
    public User getUserInfo(int ID) {
        User user = userDao.read(ID);

        resultIsNullCheck(user);
        noSuchUserCheck(user, ""); //TODO: не существует пользователя с таким ID
        appendMobilePhoneList(user);
        return user;
    }

    private void appendMobilePhoneList(User user) {
        List<MobilePhone> mobilePhones = mobilePhoneDao.getAll(user.getIdUser());

        resultIsNullCheck(mobilePhones);

        user.setMobilePhoneList(mobilePhones);
    }

    private void noSuchUserCheck(User user, String exceptionMessage) {
        if (user.getIdUser() == -1) {
            throw new RequestException(exceptionMessage);
        }
    }

    private void resultIsNullCheck(Object userOrPhones) {
        if (userOrPhones == null) {
            throw new SystemException(); //TODO: текст ошибки
        }
    }
}
