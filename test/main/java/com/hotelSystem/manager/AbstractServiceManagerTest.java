package main.java.com.hotelSystem.manager;

import main.java.com.hotelSystem.dao.AbstractMobilePhoneDao;
import main.java.com.hotelSystem.exception.SystemException;
import main.java.com.hotelSystem.manager.managerImpl.ServiceManagerImpl;
import main.java.com.hotelSystem.manager.managerImpl.daoManagerImpl.DataSourceDaoManagerImpl;
import main.java.com.hotelSystem.service.IHotelRoomService;
import main.java.com.hotelSystem.service.IService;
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

    private class ExtraConstructorParamServiceStub implements IService {
        public ExtraConstructorParamServiceStub(String string, AbstractMobilePhoneDao abstractMobilePhoneDao, IHotelRoomService hotelRoomService) {
        }
    }

    @BeforeClass
    public static void init() {
        Map<Class<? extends IService>, Class<? extends IService>> serviceMap = new HashMap<>();
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
        IService stub = serviceManager.getInstance(DefaultConstructorServiceStub.class);
        Assert.assertNotNull(stub);
    }
}