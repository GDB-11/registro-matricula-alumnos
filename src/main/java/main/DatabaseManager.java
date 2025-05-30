package main;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_NAME = "registro_alumnos.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_NAME;

    private Connection connection;

    public DatabaseManager() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(DB_URL);

            System.out.println("SQLite database connected successfully!");
            System.out.println("Database location: " + System.getProperty("user.dir") + "/" + DB_NAME);

        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("Error getting database connection: " + e.getMessage());
            throw new RuntimeException("Failed to get database connection", e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    public boolean databaseExists() {
        return Files.exists(Paths.get(DB_NAME));
    }
}
