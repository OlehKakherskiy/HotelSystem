package model.service;

import model.dao.GenericMobilePhoneDao;
import model.dao.GenericUserDao;
import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Oleh Kakherskyi, IP-31, FICT, NTUU "KPI", olehkakherskiy@gmail.com
 */
public class AbstractUserServiceTest {

    private static AbstractUserService abstractUserService;


    @BeforeClass
    public static void init() {
        GenericUserDao userDao = EasyMock.createMock(GenericUserDao.class);
        GenericMobilePhoneDao mobilePhoneDao = EasyMock.createMock(GenericMobilePhoneDao.class);
    }

    @Test
    public void testLogin() throws Exception {

    }

    @Test
    public void testGetSimpleUserInfo() throws Exception {

    }
}