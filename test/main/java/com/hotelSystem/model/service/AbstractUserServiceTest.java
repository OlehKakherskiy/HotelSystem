package main.java.com.hotelSystem.model.service;

import main.java.com.hotelSystem.dao.AbstractMobilePhoneDao;
import main.java.com.hotelSystem.dao.AbstractUserDao;
import main.java.com.hotelSystem.exception.DaoException;
import main.java.com.hotelSystem.exception.RequestException;
import main.java.com.hotelSystem.exception.SystemException;
import main.java.com.hotelSystem.model.MobilePhone;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.model.enums.UserType;
import main.java.com.hotelSystem.service.IUserService;
import main.java.com.hotelSystem.service.serviceImpl.UserService;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class AbstractUserServiceTest {

    private static IUserService abstractUserService;

    private static User expectation;

    private static User noSuchUserStub;

    private static final String LOGIN = "login";

    private static final String PASSWORD = "password";

    private static final int ID = 1;

    @BeforeClass
    public static void init() {
        expectation = getActualUser();
        expectation.setMobilePhoneList(getActualMobilePhones());
        noSuchUserStub = new User();
        noSuchUserStub.setIdUser(-1);
    }

    private static User getActualUser() {
        User actual = new User();
        actual.setName("name");
        actual.setIdUser(ID);
        actual.setLastName("lastName");
        actual.setUserType(UserType.REGISTERED_USER);
        return actual;
    }

    private static List<MobilePhone> getActualMobilePhones() {
        List<MobilePhone> mobilePhones = new ArrayList<>();
        mobilePhones.addAll(Arrays.asList(new MobilePhone(1, "(136) 605-3124", ID), new MobilePhone(2, "(558) 881-2918", ID), new MobilePhone(3, "(234) 752-9345", ID)));
        return mobilePhones;
    }

    @Test
    public void testLogin() throws Exception {

        AbstractUserDao abstractUserDao = createOkUserDaoLogin();
        AbstractMobilePhoneDao abstractMobilePhoneDao = createOkMobilePhoneDaoAll();
        EasyMock.replay(abstractUserDao, abstractMobilePhoneDao);

        abstractUserService = new UserService(abstractUserDao, abstractMobilePhoneDao);
        Assert.assertEquals(expectation, abstractUserService.signIn(LOGIN, PASSWORD));

        EasyMock.verify(abstractUserDao, abstractMobilePhoneDao);
    }

    @Test(expected = SystemException.class)
    public void testLoginSystemException() throws Exception {
        AbstractUserDao systemExceptionUserDaoLogin = createSystemExceptionUserDaoLogin();
        EasyMock.replay(systemExceptionUserDaoLogin);

        abstractUserService = new UserService(systemExceptionUserDaoLogin, null);
        abstractUserService.signIn(LOGIN, PASSWORD);

        EasyMock.verify(systemExceptionUserDaoLogin);
    }

    @Test(expected = SystemException.class)
    public void testLoginMobilePhoneSystemException() throws Exception {
        AbstractUserDao okUserDao = createOkUserDaoLogin();
        AbstractMobilePhoneDao systemExceptionMobilePhoneDao = createSystemExceptionMobilePhoneDaoAll();
        EasyMock.replay(okUserDao, systemExceptionMobilePhoneDao);

        abstractUserService = new UserService(okUserDao, systemExceptionMobilePhoneDao);
        abstractUserService.signIn(LOGIN, PASSWORD);

        EasyMock.verify(okUserDao, systemExceptionMobilePhoneDao);
    }

    @Test(expected = RequestException.class)
    public void testLoginInvalidLoginPassword() throws Exception {
        AbstractUserDao requestExceptionUserDao = createRequestExceptionUserDaoLogin();
        AbstractMobilePhoneDao okMobilePhoneDao = createOkMobilePhoneDaoAll();
        EasyMock.replay(requestExceptionUserDao, okMobilePhoneDao);

        abstractUserService = new UserService(requestExceptionUserDao, okMobilePhoneDao);
        abstractUserService.signIn(LOGIN, PASSWORD);

        EasyMock.verify(requestExceptionUserDao);
    }

    @Test
    public void testGetSimpleUserInfo() throws Exception {
        User expected = getActualUser();
        expected.setMobilePhoneList(getActualMobilePhones());

        AbstractUserDao okUserDao = createOkUserDaoRead();
        AbstractMobilePhoneDao okMobilePhoneDao = createOkMobilePhoneDaoAll();
        EasyMock.replay(okUserDao, okMobilePhoneDao);

        abstractUserService = new UserService(okUserDao, okMobilePhoneDao);
        Assert.assertEquals(expected, abstractUserService.getUserInfo(ID));

        EasyMock.verify(okUserDao, okMobilePhoneDao);
    }

    @Test(expected = RequestException.class)
    public void testGetSimpleUserInfoNoUserException() throws Exception {

        AbstractUserDao requestExceptionUserDao = createRequestExceptionUserDaoRead();
        EasyMock.replay(requestExceptionUserDao);

        abstractUserService = new UserService(requestExceptionUserDao, null);
        abstractUserService.getUserInfo(ID);

        EasyMock.verify(requestExceptionUserDao);
    }

    @Test(expected = SystemException.class)
    public void testGetSimpleUserInfoUserDaoSystemException() throws Exception {

        AbstractUserDao systemExceptionUserDao = createSystemExceptionUserDaoRead();
        EasyMock.replay(systemExceptionUserDao);

        abstractUserService = new UserService(systemExceptionUserDao, null);
        abstractUserService.getUserInfo(ID);

        EasyMock.verify(systemExceptionUserDao);
    }

    @Test(expected = SystemException.class)
    public void testGetSimpleUserInfoMobilePhonesSystemException() {

        AbstractUserDao okUserDao = createOkUserDaoRead();
        AbstractMobilePhoneDao systemExceptionMobilePhoneDao = createSystemExceptionMobilePhoneDaoAll();
        EasyMock.replay(okUserDao, systemExceptionMobilePhoneDao);

        abstractUserService = new UserService(okUserDao, systemExceptionMobilePhoneDao);
        abstractUserService.getUserInfo(ID);

        EasyMock.verify(okUserDao, systemExceptionMobilePhoneDao);
    }

    private static AbstractUserDao createOkUserDaoLogin() {
        AbstractUserDao okUserDaoLogin = EasyMock.createMock(AbstractUserDao.class);
        try {
            EasyMock.expect(okUserDaoLogin.tryLogin(LOGIN, PASSWORD)).andReturn(getActualUser());
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return okUserDaoLogin;
    }

    private static AbstractUserDao createRequestExceptionUserDaoLogin() {
        AbstractUserDao requestExceptionUserDaoLogin = EasyMock.createMock(AbstractUserDao.class);
        try {
            EasyMock.expect(requestExceptionUserDaoLogin.tryLogin(LOGIN, PASSWORD)).andReturn(noSuchUserStub);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return requestExceptionUserDaoLogin;
    }

    private static AbstractUserDao createSystemExceptionUserDaoLogin() {
        AbstractUserDao systemExceptionUserDaoLogin = EasyMock.createMock(AbstractUserDao.class);
        try {
            EasyMock.expect(systemExceptionUserDaoLogin.tryLogin(LOGIN, PASSWORD)).andThrow(new SystemException(null, null));
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return systemExceptionUserDaoLogin;
    }

    private static AbstractUserDao createOkUserDaoRead() {
        AbstractUserDao okUserDaoRead = EasyMock.createMock(AbstractUserDao.class);
        try {
            EasyMock.expect(okUserDaoRead.read(ID)).andReturn(getActualUser());
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return okUserDaoRead;
    }

    private static AbstractUserDao createRequestExceptionUserDaoRead() {
        AbstractUserDao requestExceptionUserDaoRead = EasyMock.createMock(AbstractUserDao.class);
        try {
            EasyMock.expect(requestExceptionUserDaoRead.read(ID)).andReturn(noSuchUserStub);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return requestExceptionUserDaoRead;
    }

    private static AbstractUserDao createSystemExceptionUserDaoRead() {
        AbstractUserDao systemExceptionUserDaoRead = EasyMock.createMock(AbstractUserDao.class);
        try {
            EasyMock.expect(systemExceptionUserDaoRead.read(ID)).andThrow(new SystemException(null, null));
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return systemExceptionUserDaoRead;
    }

    private static AbstractMobilePhoneDao createOkMobilePhoneDaoAll() {
        AbstractMobilePhoneDao okMobilePhoneDao = EasyMock.createMock(AbstractMobilePhoneDao.class);
        try {
            EasyMock.expect(okMobilePhoneDao.getAll(ID)).andReturn(getActualMobilePhones());
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return okMobilePhoneDao;
    }

    private static AbstractMobilePhoneDao createSystemExceptionMobilePhoneDaoAll() {
        AbstractMobilePhoneDao systemExceptionMobilePhoneDao = EasyMock.createMock(AbstractMobilePhoneDao.class);
        try {
            EasyMock.expect(systemExceptionMobilePhoneDao.getAll(ID)).andThrow(new SystemException(null, null));
        } catch (DaoException e) {
            e.printStackTrace();
        }
        return systemExceptionMobilePhoneDao;
    }
}