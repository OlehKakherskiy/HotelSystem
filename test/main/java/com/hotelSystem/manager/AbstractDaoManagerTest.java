package main.java.com.hotelSystem.manager;

import com.mysql.fabric.jdbc.FabricMySQLDataSource;
import main.java.com.hotelSystem.dao.GenericDao;
import main.java.com.hotelSystem.exception.SystemException;
import main.java.com.hotelSystem.manager.managerImpl.DataSourceDaoManagerImpl;
import main.java.com.hotelSystem.model.HotelRoom;
import main.java.com.hotelSystem.model.Reservation;
import main.java.com.hotelSystem.model.User;
import main.java.com.hotelSystem.model.roomParameter.ParameterValueTuple;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class AbstractDaoManagerTest {


    private static AbstractDaoManager daoManager;

    private class DaoStubWithoutConstructor implements GenericDao<User> {
        public DaoStubWithoutConstructor(String stub) {
        }
    }

    private class DaoStubWithoutDataSource implements GenericDao<ParameterValueTuple> {

    }

    private class DaoStubWithoutSetDataSourceMethod implements GenericDao<HotelRoom> {
        private DataSource dataSource;

        public DataSource getDataSource() {
            return dataSource;
        }
    }

    private class DaoStubWithoutGetDataSourceMethod implements GenericDao<Reservation> {
        private DataSource dataSource;

        public void setDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
        }
    }

    private class DaoStubWithOpenDataSource implements GenericDao<Reservation> {
        public DataSource dataSource;
    }

    @BeforeClass
    public static void init() throws Exception {
        Map<Class<? extends GenericDao>, Class<? extends GenericDao>> daoMap = new HashMap<>();

        daoMap.put(DaoStubWithOpenDataSource.class, DaoStubWithOpenDataSource.class);
        daoMap.put(DaoStubWithoutConstructor.class, DaoStubWithoutConstructor.class);
        daoMap.put(DaoStubWithoutDataSource.class, DaoStubWithoutDataSource.class);
        daoMap.put(DaoStubWithoutSetDataSourceMethod.class, DaoStubWithoutSetDataSourceMethod.class);
        daoMap.put(DaoStubWithoutGetDataSourceMethod.class, DaoStubWithoutGetDataSourceMethod.class);

        daoManager = new DataSourceDaoManagerImpl(daoMap, new FabricMySQLDataSource()/*stub*/);
    }

    @Test(expected = SystemException.class)
    public void testInstantiateWithoutConstructor() throws Exception {
        daoManager.getInstance(DaoStubWithoutConstructor.class);
    }

    @Test(expected = SystemException.class)
    public void testDaoStubWithoutDataSource() throws Exception {
        daoManager.getInstance(DaoStubWithoutDataSource.class);
    }

    @Test(expected = SystemException.class)
    public void testDaoStubWithoutSetDataSourceMethod() throws Exception {
        daoManager.getInstance(DaoStubWithoutSetDataSourceMethod.class);
    }

    @Test(expected = SystemException.class)
    public void testDaoStubWithoutGetDataSourceMethod() throws Exception {
        daoManager.getInstance(DaoStubWithoutGetDataSourceMethod.class);
    }

    @Test(expected = SystemException.class)
    public void testDaoStubWithOpenDataSource() throws Exception {
        daoManager.getInstance(DaoStubWithOpenDataSource.class);
    }
}