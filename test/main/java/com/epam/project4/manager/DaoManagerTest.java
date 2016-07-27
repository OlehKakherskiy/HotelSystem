package main.java.com.epam.project4.manager;

import com.mysql.fabric.jdbc.FabricMySQLDataSource;
import main.java.com.epam.project4.manager.managerImpl.DataSourceDaoManagerImpl;
import main.java.com.epam.project4.model.dao.GenericDao;
import main.java.com.epam.project4.model.dao.TransparentGenericDao;
import main.java.com.epam.project4.model.exception.SystemException;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class DaoManagerTest {


    private static DaoManager daoManager;

    private class DaoStubWithoutConstructor extends TransparentGenericDao {
        public DaoStubWithoutConstructor(String stub) {
        }
    }

    private class DaoStubWithoutDataSource extends TransparentGenericDao {

    }

    private class DaoStubWithoutSetDataSourceMethod extends TransparentGenericDao {
        private DataSource dataSource;

        public DataSource getDataSource() {
            return dataSource;
        }
    }

    private class DaoStubWithoutGetDataSourceMethod extends TransparentGenericDao {
        private DataSource dataSource;

        public void setDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
        }
    }

    private class DaoStubWithOpenDataSource extends TransparentGenericDao {
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
    public void testDaoStubWithoutSetDataSourceMethod() throws Exception{
        daoManager.getInstance(DaoStubWithoutSetDataSourceMethod.class);
    }

    @Test(expected = SystemException.class)
    public void testDaoStubWithoutGetDataSourceMethod() throws Exception{
        daoManager.getInstance(DaoStubWithoutGetDataSourceMethod.class);
    }

    @Test(expected = SystemException.class)
    public void testDaoStubWithOpenDataSource() throws Exception{
        daoManager.getInstance(DaoStubWithOpenDataSource.class);
    }
}