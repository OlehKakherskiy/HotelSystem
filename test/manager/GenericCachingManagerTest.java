package manager;

import app.constants.CommandConstant;
import com.mysql.fabric.jdbc.FabricMySQLDataSource;
import controller.command.AbstractCommand;
import controller.command.commandImpl.FillNewReservationCommand;
import controller.command.commandImpl.GetReservationListCommand;
import controller.command.commandImpl.LogoutCommand;
import controller.command.commandImpl.RefuseHotelRoomOfferCommand;
import manager.managerImpl.CommandManagerImpl;
import manager.managerImpl.DataSourceDaoManagerImpl;
import manager.managerImpl.ServiceManagerImpl;
import model.dao.*;
import model.dao.daoImpl.*;
import model.exceptions.SystemException;
import model.service.*;
import model.service.serviceImpl.HotelRoomService;
import model.service.serviceImpl.ParameterValueServiceImpl;
import model.service.serviceImpl.ReservationService;
import model.service.serviceImpl.UserService;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class GenericCachingManagerTest { //TODO: протестить сервисы на наличие ошибки когда в конструкторе левый параметр передан, дао - когда нет датасорса, гет-сет методов или конструктор по умолчанию не доступен.

    private static List<GenericCachingManager> cachingManagers = new ArrayList<>();

    private static AbstractCommandManager commandManager;

    private static GenericServiceManager serviceManager;

    private static DaoManager daoManager;

    private static List<Map> initMapsForManagers = new ArrayList<>();

    @BeforeClass
    public static void init() {
        initCommandManager();
        initDaoManager();
        initServiceManager();
    }

    private static void initCommandManager() {
        Map<CommandConstant, Class<? extends AbstractCommand>> map = new HashMap<>();
        map.put(CommandConstant.LOGOUT_COMMAND, LogoutCommand.class);
        map.put(CommandConstant.REFUSE_HOTEL_ROOM_OFFER_COMMAND, RefuseHotelRoomOfferCommand.class);
        map.put(CommandConstant.GET_RESERVATION_LIST_COMMAND, GetReservationListCommand.class);
        map.put(CommandConstant.FILL_NEW_RESERVATION_COMMAND, FillNewReservationCommand.class);

        commandManager = new CommandManagerImpl(map);
        cachingManagers.add(commandManager);
        initMapsForManagers.add(map);


    }

    private static void initDaoManager() {
        Map<Class<? extends GenericDao>, Class<? extends GenericDao>> daoManagerConfigs = new HashMap<>();
        daoManagerConfigs.put(GenericHotelRoomDao.class, GenericHotelRoomDaoImpl.class);
        daoManagerConfigs.put(GenericMobilePhoneDao.class, GenericMobilePhoneDaoImpl.class);
        daoManagerConfigs.put(GenericParameterValueDao.class, GenericParameterValueDaoImpl.class);
        daoManagerConfigs.put(GenericReservationDao.class, GenericReservationDaoImpl.class);
        daoManagerConfigs.put(GenericUserDao.class, GenericUserDaoImpl.class);

        daoManager = new DataSourceDaoManagerImpl(daoManagerConfigs, new FabricMySQLDataSource());
        cachingManagers.add(daoManager);//stub datasource
        initMapsForManagers.add(daoManagerConfigs);
    }

    private static void initServiceManager() {
        Map<Class<? extends AbstractService>, Class<? extends AbstractService>> serviceMap = new HashMap<>();
        serviceMap.put(AbstractHotelRoomService.class, HotelRoomService.class);
        serviceMap.put(AbstractParameterValueService.class, ParameterValueServiceImpl.class);
        serviceMap.put(AbstractReservationService.class, ReservationService.class);
        serviceMap.put(AbstractUserService.class, UserService.class);

        serviceManager = new ServiceManagerImpl(serviceMap, (DaoManager) cachingManagers.get(1));
        cachingManagers.add(serviceManager);
        initMapsForManagers.add(serviceMap);
    }

    @Test
    public void testGetInstance() throws Exception {
        for (int i = 0; i < cachingManagers.size(); i++) {
            for (Object entry : initMapsForManagers.get(i).entrySet()) {
                System.out.println(((Map.Entry) entry).getKey());
                Assert.assertEquals(((Map.Entry) entry).getValue(), cachingManagers.get(i).getInstance(((Map.Entry) entry).getKey()).getClass());
            }
        }
    }

    @Test(expected = SystemException.class)
    public void testCommandManagerGetInstanceOfNull() throws Exception {
        commandManager.getInstance(null);
    }

    @Test(expected = SystemException.class)
    public void testDaoManagerGetInstanceOfNull() throws Exception {
        daoManager.getInstance(null);
    }

    @Test(expected = SystemException.class)
    public void testServiceManagerGetInstanceOfNull() throws Exception {
        serviceManager.getInstance(null);
    }

    @Test(expected = SystemException.class)
    public void testCommandManagerNoValue() throws Exception {
        commandManager.getInstance(CommandConstant.LOGIN_COMMAND);
    }

    @Test(expected = SystemException.class)
    public void testDaoManagerNoValue() throws Exception {
        class DaoStub extends TransparentGenericDao{
        }
        daoManager.getInstance(DaoStub.class);

    }

    @Test(expected = SystemException.class)
    public void testServiceManager() throws Exception{
        class ServiceStub implements AbstractService{
        }
        serviceManager.getInstance(ServiceStub.class);
    }
}