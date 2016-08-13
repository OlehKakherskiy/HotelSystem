package main.java.com.hotelSystem.manager;

import main.java.com.hotelSystem.app.constants.CommandConstant;
import main.java.com.hotelSystem.controller.command.ICommand;
import main.java.com.hotelSystem.controller.command.commandImpl.FillNewReservationCommand;
import main.java.com.hotelSystem.controller.command.commandImpl.GetReservationListCommand;
import main.java.com.hotelSystem.controller.command.commandImpl.LogoutCommand;
import main.java.com.hotelSystem.controller.command.commandImpl.RefuseHotelRoomOfferCommand;
import main.java.com.hotelSystem.dao.*;
import main.java.com.hotelSystem.dao.daoImpl.*;
import main.java.com.hotelSystem.exception.SystemException;
import main.java.com.hotelSystem.manager.managerImpl.CommandManagerImpl;
import main.java.com.hotelSystem.manager.managerImpl.DataSourceDaoManagerImpl;
import main.java.com.hotelSystem.manager.managerImpl.ServiceManagerImpl;
import main.java.com.hotelSystem.model.Reservation;
import main.java.com.hotelSystem.service.*;
import main.java.com.hotelSystem.service.serviceImpl.HotelRoomService;
import main.java.com.hotelSystem.service.serviceImpl.ParameterValueServiceImpl;
import main.java.com.hotelSystem.service.serviceImpl.ReservationService;
import main.java.com.hotelSystem.service.serviceImpl.UserService;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class GenericCachingManagerTest {

    private static List<GenericCachingManager> cachingManagers = new ArrayList<>();

    private static AbstractCommandManager commandManager;

    private static AbstractServiceManager serviceManager;

    private static AbstractDaoManager daoManager;

    private static List<Map> initMapsForManagers = new ArrayList<>();

    @BeforeClass
    public static void init() {
        initCommandManager();
        initDaoManager();
        initServiceManager();

        LogManager.getRootLogger().setLevel(Level.OFF);

    }

    private static void initCommandManager() {
        Map<CommandConstant, Class<? extends ICommand>> map = new HashMap<>();
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
        daoManagerConfigs.put(AbstractHotelRoomDao.class, AbstractHotelRoomDaoImpl.class);
        daoManagerConfigs.put(AbstractMobilePhoneDao.class, AbstractMobilePhoneDaoImpl.class);
        daoManagerConfigs.put(AbstractParameterValueDao.class, AbstractParameterValueDaoImpl.class);
        daoManagerConfigs.put(AbstractReservationDao.class, AbstractReservationDaoImpl.class);
        daoManagerConfigs.put(AbstractUserDao.class, AbstractUserDaoImpl.class);

        DataSource dataSource = EasyMock.createMock(DataSource.class);
        Connection connection = EasyMock.createMock(Connection.class);
        try {
            EasyMock.expect(dataSource.getConnection()).andReturn(connection).anyTimes();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        EasyMock.replay(dataSource);
        daoManager = new DataSourceDaoManagerImpl(daoManagerConfigs, dataSource);

        cachingManagers.add(daoManager);//stub datasource
        initMapsForManagers.add(daoManagerConfigs);
    }

    private static void initServiceManager() {
        Map<Class<? extends IService>, Class<? extends IService>> serviceMap = new HashMap<>();
        serviceMap.put(IHotelRoomService.class, HotelRoomService.class);
        serviceMap.put(IParameterValueService.class, ParameterValueServiceImpl.class);
        serviceMap.put(IReservationService.class, ReservationService.class);
        serviceMap.put(IUserService.class, UserService.class);

        serviceManager = new ServiceManagerImpl(serviceMap, (AbstractDaoManager) cachingManagers.get(1));
        cachingManagers.add(serviceManager);
        initMapsForManagers.add(serviceMap);
    }

    @Test
    public void testGetInstance() throws Exception {
        for (int i = 0; i < cachingManagers.size(); i++) {
            for (Object entry : initMapsForManagers.get(i).entrySet()) {
                Assert.assertEquals(((Map.Entry) entry).getValue(),
                        cachingManagers.get(i).getInstance(((Map.Entry) entry).getKey()).getClass());
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
        class DaoStub implements GenericDao<Reservation> {
        }
        daoManager.getInstance(DaoStub.class);

    }

    @Test(expected = SystemException.class)
    public void testServiceManager() throws Exception {
        class ServiceStub implements IService {
        }
        serviceManager.getInstance(ServiceStub.class);
    }
}