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

            System.out.println("Base de datos SQLite database conectada");
            System.out.println("Ubicación: " + System.getProperty("user.dir") + "/" + DB_NAME);

        } catch (SQLException e) {
            System.err.println("Error conectando a base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo incializar la base de datos", e);
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("Conexión a base de datos abierta.");
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("Error obteniendo conexión a base de datos: " + e.getMessage());
            throw new RuntimeException("Error obteniendo conexión a base de datos", e);
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión a base de datos cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error cerrando conexión a base de datos: " + e.getMessage());
        }
    }

    public boolean databaseExists() {
        return Files.exists(Paths.get(DB_NAME));
    }
}
