package manager;

import manager.managerImpl.DataSourceDaoManagerImpl;
import manager.managerImpl.ServiceManagerImpl;
import model.dao.GenericMobilePhoneDao;
import model.exceptions.SystemException;
import model.service.AbstractHotelRoomService;
import model.service.AbstractService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class GenericServiceManagerTest {

    private static GenericServiceManager serviceManager;

    private class ExtraConstructorParamServiceStub implements AbstractService {
        public ExtraConstructorParamServiceStub(String string, GenericMobilePhoneDao genericMobilePhoneDao, AbstractHotelRoomService hotelRoomService) {
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