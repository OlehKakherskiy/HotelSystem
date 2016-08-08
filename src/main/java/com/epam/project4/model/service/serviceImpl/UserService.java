package main.java.com.epam.project4.model.service.serviceImpl;

import main.java.com.epam.project4.app.constants.MessageCode;
import main.java.com.epam.project4.exception.DaoException;
import main.java.com.epam.project4.exception.RequestException;
import main.java.com.epam.project4.exception.SystemException;
import main.java.com.epam.project4.model.dao.AbstractMobilePhoneDao;
import main.java.com.epam.project4.model.dao.AbstractUserDao;
import main.java.com.epam.project4.model.entity.MobilePhone;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.service.IUserService;

import java.util.List;

/**
 * Class represents implementation of {@link IUserService} and uses {@link AbstractUserDao}
 * and {@link AbstractMobilePhoneDao} for performing business-logic operations.
 *
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 * @see AbstractUserDao
 * @see AbstractMobilePhoneDao
 */
public class UserService implements IUserService {

    /**
     * for performing operations with {@link User}
     */
    private AbstractUserDao userDao;

    /**
     * for performing operations with {@link MobilePhone}
     */
    private AbstractMobilePhoneDao mobilePhoneDao;

    /**
     * Inits DAOs' fields. MUSTN'T be null
     *
     * @param userDao        inits {@link #userDao}
     * @param mobilePhoneDao inits {@link #mobilePhoneDao}
     */
    public UserService(AbstractUserDao userDao, AbstractMobilePhoneDao mobilePhoneDao) {
        this.userDao = userDao;
        this.mobilePhoneDao = mobilePhoneDao;
    }

    /**
     * {@inheritDoc}
     *
     * @param login    user's login
     * @param password user's password
     * @return {@inheritDoc}
     * @throws RequestException {@inheritDoc}
     * @throws SystemException  {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     *
     * @param id User's id
     * @return {@inheritDoc}
     * @throws RequestException {@inheritDoc}
     * @throws SystemException  {@inheritDoc}
     */
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

    /**
     * Appends mobile phone list, assosiated with target user object, to this object
     *
     * @param user target user object, to which assosiated mobile phone's list will be injected
     * @throws main.java.com.epam.project4.exception.SystemException if exception was
     *                                                               thrown during processing any underlying operation
     */
    private void appendMobilePhoneList(User user) {
        try {
            List<MobilePhone> mobilePhones = mobilePhoneDao.getAll(user.getIdUser());
            user.setMobilePhoneList(mobilePhones);
        } catch (DaoException e) {
            throw new SystemException(MessageCode.GET_MOBILE_PHONE_LIST_SYSTEM_EXCEPTION, e);
        }
    }

    /**
     * checks whether the user is exists (whether {@link User#idUser} != -1}).
     *
     * @param user             target user
     * @param exceptionMessage exception message if user doesn't exist
     * @param requestUserId    requested user id
     * @throws RequestException if user doesn't exist
     */
    private void noSuchUserCheck(User user, MessageCode exceptionMessage, int requestUserId) {
        if (user.getIdUser() == -1) {
            throw new RequestException(exceptionMessage, requestUserId);
        }
    }

    /**
     * checks whether the user is exists (whether {@link User#idUser} != -1}).
     *
     * @param user             target user
     * @param exceptionMessage exception message if user doesn't exist
     * @throws RequestException if user doesn't exist
     */
    private void noSuchUserCheck(User user, MessageCode exceptionMessage) {
        if (user.getIdUser() == -1) {
            throw new RequestException(exceptionMessage);
        }
    }
}
