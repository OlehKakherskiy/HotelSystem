package main.java.com.epam.project4.manager;

import main.java.com.epam.project4.exception.SystemException;
import main.java.com.epam.project4.manager.managerImpl.DataSourceDaoManagerImpl;
import main.java.com.epam.project4.manager.managerImpl.ServiceManagerImpl;
import main.java.com.epam.project4.model.dao.AbstractMobilePhoneDao;
import main.java.com.epam.project4.model.service.AbstractHotelRoomService;
import main.java.com.epam.project4.model.service.AbstractService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class AbstractServiceManagerTest {

    private static AbstractServiceManager serviceManager;

    private class ExtraConstructorParamServiceStub implements AbstractService {
        public ExtraConstructorParamServiceStub(String string, AbstractMobilePhoneDao abstractMobilePhoneDao, AbstractHotelRoomService hotelRoomService) {
        }
    }

    @BeforeClass
    public static void init() {
        Map<Class<? extends AbstractService>, Class<? extends AbstractService>> serviceMap = new HashMap<>();
        serviceMap.put(ExtraConstructorParamServiceStub.class, ExtraConstructorParamServiceStub.class);
        serviceMap.put(DefaultConstructorServiceStub.class, DefaultConstructorServiceStub.class);
        serviceManager = new ServiceManagerImpl(serviceMap, new DataSourceDaoManagerImpl(null, null));

    }

    @Test(expected = SystemException.class)
    public void getServiceWithExtraParamsConstructor() throws Exception {
        serviceManager.getInstance(ExtraConstructorParamServiceStub.class);
    }

    @Test
    public void getServiceWithDefaultConstructor() throws Exception {
        DefaultConstructorServiceStub stub = serviceManager.getInstance(DefaultConstructorServiceStub.class);
        Assert.assertNotNull(stub);
    }
}