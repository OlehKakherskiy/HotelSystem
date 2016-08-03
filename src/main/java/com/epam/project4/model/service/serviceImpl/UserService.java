package main.java.com.epam.project4.model.service.serviceImpl;

import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.exception.RequestException;
import main.java.com.epam.project4.exception.SystemException;
import main.java.com.epam.project4.model.dao.AbstractMobilePhoneDao;
import main.java.com.epam.project4.model.dao.AbstractUserDao;
import main.java.com.epam.project4.model.entity.MobilePhone;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.service.AbstractUserService;

import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class UserService implements AbstractUserService {

    private AbstractUserDao userDao;

    private AbstractMobilePhoneDao mobilePhoneDao;

    public UserService(AbstractUserDao userDao, AbstractMobilePhoneDao mobilePhoneDao) {
        this.userDao = userDao;
        this.mobilePhoneDao = mobilePhoneDao;
    }

    @Override
    public User login(String login, String password) {
        try {
            User result = userDao.tryLogin(login, password);
            noSuchUserCheck(result, MessageCode.WRONG_LOGIN_OR_PASSWORD);
            appendMobilePhoneList(result);
            return result;
        } catch (DaoException e) {
            throw new SystemException(MessageCode.LOGIN_OPERATION_SYSTEM_EXCEPTION, e);
        }
    }

    @Override
    public User getUserInfo(int id) {
        try {
            User user = userDao.read(id);
            noSuchUserCheck(user, MessageCode.WRONG_USER_ID, id);
            appendMobilePhoneList(user);
            return user;
        } catch (DaoException e) {
            throw new SystemException(MessageCode.READ_USER_OPERATION_SYSTEM_EXCEPTION, e);
        }
    }

    private void appendMobilePhoneList(User user) {
        try {
            List<MobilePhone> mobilePhones = mobilePhoneDao.getAll(user.getIdUser());
            user.setMobilePhoneList(mobilePhones);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.GET_MOBILE_PHONE_LIST_SYSTEM_EXCEPTION, e);
        }
    }

    private void noSuchUserCheck(User user, MessageCode exceptionMessage, int requestUserId) {
        if (user.getIdUser() == -1) {
            throw new RequestException(exceptionMessage, requestUserId);
        }
    }

    private void noSuchUserCheck(User user, MessageCode exceptionMessage) {
        if (user.getIdUser() == -1) {
            throw new RequestException(exceptionMessage);
        }
    }
}
