package main.java.com.hotelSystem.manager.managerImpl.daoManagerImpl;

import main.java.com.hotelSystem.app.constants.MessageCode;
import main.java.com.hotelSystem.exception.SystemException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class represents connection manager. It's purpose is to allocate one
 * {@link Connection} per one execution thread, so that all {@link main.java.com.hotelSystem.dao.GenericDao DAOs}
 * will use just one connection for implementing ORM. Thread-to-connection mapping stores in
 * {@link #threadConnectionMap}. For better performance this map's real type is {@link ConcurrentHashMap}.
 * <p>
 * The way it should be used:
 * <ul>
 * <li>{@link #allocateConnection()} before DAOs operations will be executed;</li>
 * <li>{@link #getConnection()} during the operation's performing for getting allocated connection;
 * </li>
 * <li>{@link #closeConnection()} after DAOs operation will be executed;</li>
 * </ul>
 * </p>
 * <p>
 * Class is being configured via constructor. It needs {@link DataSource} for providing operations.
 * </p>
 *
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class ConnectionAllocator {

    /**
     * map, that contains threadName-connection mapping
     */
    private Map<String, Connection> threadConnectionMap;

    /**
     * datasource, that will be used for getting {@link Connection} from pool
     */
    private DataSource dataSource;

    /**
     * Inits class' fields
     *
     * @param dataSource datasource, that will be used for getting {@link Connection} from pool
     */
    public ConnectionAllocator(DataSource dataSource) {
        threadConnectionMap = new ConcurrentHashMap<>();
        this.dataSource = dataSource;
    }

    /**
     * Allocate new connection for current thread and adds it to {@link #threadConnectionMap}
     * (key - current thread's name, value - connection).
     *
     * @return allocated connection
     * @throws SystemException if exception was thrown during the process of getting connection
     *                         from datasource
     */
    public Connection allocateConnection() {
        String threadName = Thread.currentThread().getName();
        Connection result = threadConnectionMap.get(threadName);
        if (result == null) {
            try {
                result = dataSource.getConnection();
                threadConnectionMap.put(threadName, result);
            } catch (SQLException e) {
                throw new SystemException(MessageCode.GENERAL_SYSTEM_EXCEPTION, e);
            }
        }
        return result;
    }

    /**
     * Returns previously allocated by {@link #allocateConnection()} connection or null
     * if {@link #allocateConnection()} wasn't executed. It uses current thread's name
     * as a key in map.
     *
     * @return connection, assosiated with current execution thread.
     */
    public Connection getConnection() {
        return threadConnectionMap.get(Thread.currentThread().getName());
    }

    /**
     * Releases connection from current execution thread. If there's no connection,
     * allocated to current thread, do nothing.
     *
     * @throws SystemException if exception was thrown during the process of closing connection
     */
    public void closeConnection() {
        String threadName = Thread.currentThread().getName();
        Connection target = threadConnectionMap.remove(threadName);
        try {
            if (target != null && !target.isClosed()) {
                target.close();
            }
        } catch (SQLException e) {
            throw new SystemException(MessageCode.GENERAL_SYSTEM_EXCEPTION, e);
        }
    }
}
