package main.java.com.epam.project4.model.service;

import main.java.com.epam.project4.model.dao.GenericMobilePhoneDao;
import main.java.com.epam.project4.model.dao.GenericUserDao;
import main.java.com.epam.project4.model.entity.MobilePhone;
import main.java.com.epam.project4.model.entity.User;
import main.java.com.epam.project4.model.entity.enums.UserType;
import main.java.com.epam.project4.model.exception.RequestException;
import main.java.com.epam.project4.model.exception.SystemException;
import main.java.com.epam.project4.model.service.serviceImpl.UserService;
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

    private static AbstractUserService abstractUserService;

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
        mobilePhones.addAll(Arrays.asList(new MobilePhone(1, "(136) 605-3124"), new MobilePhone(2, "(558) 881-2918"), new MobilePhone(3, "(234) 752-9345")));
        return mobilePhones;
    }

    @Test
    public void testLogin() throws Exception {

        GenericUserDao genericUserDao = createOkUserDaoLogin();
        GenericMobilePhoneDao genericMobilePhoneDao = createOkMobilePhoneDaoAll();
        EasyMock.replay(genericUserDao, genericMobilePhoneDao);

        abstractUserService = new UserService(genericUserDao, genericMobilePhoneDao);
        Assert.assertEquals(expectation, abstractUserService.login(LOGIN, PASSWORD));

        EasyMock.verify(genericUserDao, genericMobilePhoneDao);
    }

    @Test(expected = SystemException.class)
    public void testLoginSystemException() throws Exception {
        GenericUserDao systemExceptionUserDaoLogin = createSystemExceptionUserDaoLogin();
        EasyMock.replay(systemExceptionUserDaoLogin);

        abstractUserService = new UserService(systemExceptionUserDaoLogin, null);
        abstractUserService.login(LOGIN, PASSWORD);

        EasyMock.verify(systemExceptionUserDaoLogin);
    }

    @Test(expected = SystemException.class)
    public void testLoginMobilePhoneSystemException() throws Exception {
        GenericUserDao okUserDao = createOkUserDaoLogin();
        GenericMobilePhoneDao systemExceptionMobilePhoneDao = createSystemExceptionMobilePhoneDaoAll();
        EasyMock.replay(okUserDao, systemExceptionMobilePhoneDao);

        abstractUserService = new UserService(okUserDao, systemExceptionMobilePhoneDao);
        abstractUserService.login(LOGIN, PASSWORD);

        EasyMock.verify(okUserDao, systemExceptionMobilePhoneDao);
    }

    @Test(expected = RequestException.class)
    public void testLoginInvalidLoginPassword() throws Exception {
        GenericUserDao requestExceptionUserDao = createRequestExceptionUserDaoLogin();
        GenericMobilePhoneDao okMobilePhoneDao = createOkMobilePhoneDaoAll();
        EasyMock.replay(requestExceptionUserDao, okMobilePhoneDao);

        abstractUserService = new UserService(requestExceptionUserDao, okMobilePhoneDao);
        abstractUserService.login(LOGIN, PASSWORD);

        EasyMock.verify(requestExceptionUserDao);
    }

    @Test
    public void testGetSimpleUserInfo() throws Exception {
        User expected = getActualUser();
        expected.setMobilePhoneList(getActualMobilePhones());

        GenericUserDao okUserDao = createOkUserDaoRead();
        GenericMobilePhoneDao okMobilePhoneDao = createOkMobilePhoneDaoAll();
        EasyMock.replay(okUserDao, okMobilePhoneDao);

        abstractUserService = new UserService(okUserDao, okMobilePhoneDao);
        Assert.assertEquals(expected, abstractUserService.getSimpleUserInfo(ID));

        EasyMock.verify(okUserDao, okMobilePhoneDao);
    }

    @Test(expected = RequestException.class)
    public void testGetSimpleUserInfoNoUserException() throws Exception {

        GenericUserDao requestExceptionUserDao = createRequestExceptionUserDaoRead();
        EasyMock.replay(requestExceptionUserDao);

        abstractUserService = new UserService(requestExceptionUserDao, null);
        abstractUserService.getSimpleUserInfo(ID);

        EasyMock.verify(requestExceptionUserDao);
    }

    @Test(expected = SystemException.class)
    public void testGetSimpleUserInfoUserDaoSystemException() throws Exception {

        GenericUserDao systemExceptionUserDao = createSystemExceptionUserDaoRead();
        EasyMock.replay(systemExceptionUserDao);

        abstractUserService = new UserService(systemExceptionUserDao, null);
        abstractUserService.getSimpleUserInfo(ID);

        EasyMock.verify(systemExceptionUserDao);
    }

    @Test(expected = SystemException.class)
    public void testGetSimpleUserInfoMobilePhonesSystemException() {

        GenericUserDao okUserDao = createOkUserDaoRead();
        GenericMobilePhoneDao systemExceptionMobilePhoneDao = createSystemExceptionMobilePhoneDaoAll();
        EasyMock.replay(okUserDao, systemExceptionMobilePhoneDao);

        abstractUserService = new UserService(okUserDao, systemExceptionMobilePhoneDao);
        abstractUserService.getSimpleUserInfo(ID);

        EasyMock.verify(okUserDao, systemExceptionMobilePhoneDao);
    }

    private static GenericUserDao createOkUserDaoLogin() {
        GenericUserDao okUserDaoLogin = EasyMock.createMock(GenericUserDao.class);
        EasyMock.expect(okUserDaoLogin.tryLogin(LOGIN, PASSWORD)).andReturn(getActualUser());
        return okUserDaoLogin;
    }

    private static GenericUserDao createRequestExceptionUserDaoLogin() {
        GenericUserDao requestExceptionUserDaoLogin = EasyMock.createMock(GenericUserDao.class);
        EasyMock.expect(requestExceptionUserDaoLogin.tryLogin(LOGIN, PASSWORD)).andReturn(noSuchUserStub);
        return requestExceptionUserDaoLogin;
    }

    private static GenericUserDao createSystemExceptionUserDaoLogin() {
        GenericUserDao systemExceptionUserDaoLogin = EasyMock.createMock(GenericUserDao.class);
        EasyMock.expect(systemExceptionUserDaoLogin.tryLogin(LOGIN, PASSWORD)).andReturn(null);
        return systemExceptionUserDaoLogin;
    }

    private static GenericUserDao createOkUserDaoRead() {
        GenericUserDao okUserDaoRead = EasyMock.createMock(GenericUserDao.class);
        EasyMock.expect(okUserDaoRead.read(ID)).andReturn(getActualUser());
        return okUserDaoRead;
    }

    private static GenericUserDao createRequestExceptionUserDaoRead() {
        GenericUserDao requestExceptionUserDaoRead = EasyMock.createMock(GenericUserDao.class);
        EasyMock.expect(requestExceptionUserDaoRead.read(ID)).andReturn(noSuchUserStub);
        return requestExceptionUserDaoRead;
    }

    private static GenericUserDao createSystemExceptionUserDaoRead() {
        GenericUserDao systemExceptionUserDaoRead = EasyMock.createMock(GenericUserDao.class);
        EasyMock.expect(systemExceptionUserDaoRead.read(ID)).andReturn(null);
        return systemExceptionUserDaoRead;
    }

    private static GenericMobilePhoneDao createOkMobilePhoneDaoAll() {
        GenericMobilePhoneDao okMobilePhoneDao = EasyMock.createMock(GenericMobilePhoneDao.class);
        EasyMock.expect(okMobilePhoneDao.getAll(ID)).andReturn(getActualMobilePhones());
        return okMobilePhoneDao;
    }

    private static GenericMobilePhoneDao createSystemExceptionMobilePhoneDaoAll() {
        GenericMobilePhoneDao systemExceptionMobilePhoneDao = EasyMock.createMock(GenericMobilePhoneDao.class);
        EasyMock.expect(systemExceptionMobilePhoneDao.getAll(ID)).andReturn(null);
        return systemExceptionMobilePhoneDao;
    }
}