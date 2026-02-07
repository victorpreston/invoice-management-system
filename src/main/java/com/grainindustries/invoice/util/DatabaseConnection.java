package com.grainindustries.invoice.util;

import java.io.IOException;
import java.io. InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static Properties properties = new Properties();
    private static String url;
    private static String username;
    private static String password;

    static {
        try {
            // Try to read from environment variables first (for Docker)
            String dbServer = System.getenv("DB_SERVER");
            String dbPort = System.getenv("DB_PORT");
            String dbName = System.getenv("DB_NAME");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");
            
            if (dbServer != null && dbName != null && dbUser != null && dbPassword != null) {
                // Docker environment - use environment variables
                if (dbPort == null) {
                    dbPort = "1433"; // default SQL Server port
                }
                url = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=false;trustServerCertificate=true",
                        dbServer, dbPort, dbName);
                username = dbUser;
                password = dbPassword;
            } else {
                // Local development - use database.properties
                try (InputStream input = DatabaseConnection.class.getClassLoader()
                        .getResourceAsStream("database.properties")) {
                    if (input == null) {
                        throw new IOException("Unable to find database.properties");
                    }
                    properties.load(input);
                    url = properties.getProperty("db.url");
                    username = properties.getProperty("db.username");
                    password = properties.getProperty("db.password");
                }
            }
            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
