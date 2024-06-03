package org.example.util;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ConnectionManager {
    private static final String USERNAME_KEY = "db.password";
    private static final String PASSWORD_KEY = "db.username";
    private static final String URL_KEY = "db.url";
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static final String ORG_POSTGRESQL_DRIVER = "org.postgresql.Driver";
    private static final Integer DEFAULT_POOL_SIZE = 10;
    private static final Queue<Connection> pool = new ConcurrentLinkedQueue<>();
    private static final List<Connection> sourceConnections = new CopyOnWriteArrayList<>();

    private ConnectionManager() {
    }

    static {
        loadDriver();
        initConnectionPool();
    }

    private static void initConnectionPool() {
        int poolSize = Integer.parseInt(PropertiesUtil.get(POOL_SIZE_KEY));
        if (poolSize == 0) {
            poolSize = DEFAULT_POOL_SIZE;
        }
        for (int i = 0; i < poolSize; i++) {
            Connection connection = open();
            Connection proxyConnection = (Connection) Proxy.newProxyInstance(
                    ConnectionManager.class.getClassLoader(),
                    new Class[]{Connection.class},
                    (proxy, method, args) -> {
                        if (method.getName().equals("close")) {
                            pool.add((Connection) proxy);
                            return null;
                        } else {
                            return method.invoke(connection, args);
                        }
                    });
            pool.add(proxyConnection);
            sourceConnections.add(connection);
        }
    }

    public static Connection get() {
        Connection connection;
        while ((connection = pool.poll()) == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public static Connection open() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadDriver() {
        try {
            Class.forName(ORG_POSTGRESQL_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closePool() {
        for (Connection sourceConnection : sourceConnections) {
            try {
                sourceConnection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
