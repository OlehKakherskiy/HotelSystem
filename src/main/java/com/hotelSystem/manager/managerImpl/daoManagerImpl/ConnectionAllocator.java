package main.java.com.hotelSystem.manager.managerImpl.daoManagerImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Oleh Kakherskyi (olehkakherskiy@gmail.com)
 */
public class ConnectionAllocator {

    private Map<String, Connection> threadConnectionMap;

    private DataSource dataSource;

    public ConnectionAllocator(DataSource dataSource) {
        threadConnectionMap = new ConcurrentHashMap<>();
        this.dataSource = dataSource;
    }

    public Connection allocateConnection() {
        String threadName = Thread.currentThread().getName();
        Connection result = threadConnectionMap.get(threadName);
        if (result == null) {
            try {
                result = dataSource.getConnection();
                threadConnectionMap.put(threadName, result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Connection getConnection() {
        return threadConnectionMap.get(Thread.currentThread().getName());
    }

    public void closeConnection() {
        String threadName = Thread.currentThread().getName();
        Connection target = threadConnectionMap.remove(threadName);
        try {
            if (target != null && !target.isClosed()) {
                target.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
