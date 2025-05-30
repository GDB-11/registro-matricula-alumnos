package main;

import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private final DatabaseManager databaseManager;

    public DatabaseInitializer(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void initializeTables() {
        System.out.println("Initializing database tables...");

        createAlumnoTable();
        createCursoTable();
        createMatriculaTable();
        createRetiroTable();

        System.out.println("Database initialization completed!");
    }

    private void createAlumnoTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS alumno (
                cod_alumno INTEGER PRIMARY KEY AUTOINCREMENT,
                nombres TEXT NOT NULL,
                apellidos TEXT NOT NULL,
                dni TEXT NOT NULL UNIQUE,
                edad INTEGER NOT NULL,
                celular TEXT NOT NULL,
                estado INTEGER NOT NULL DEFAULT 1,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            )
            """;

        executeCreateTable(sql, "alumno");
    }

    private void createCursoTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS curso (
                cod_curso INTEGER PRIMARY KEY AUTOINCREMENT,
                asignatura TEXT NOT NULL,
                ciclo INTEGER NOT NULL,
                creditos INTEGER NOT NULL,
                horas INTEGER NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP
            )
            """;

        executeCreateTable(sql, "curso");
    }

    private void createMatriculaTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS matricula (
                correlativo INTEGER PRIMARY KEY AUTOINCREMENT,
                cod_alumno INTEGER NOT NULL,
                cod_curso INTEGER NOT NULL,
                fecha DATE NOT NULL,
                hora TIME NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (cod_alumno) REFERENCES alumno(cod_alumno),
                FOREIGN KEY (cod_curso) REFERENCES curso(cod_curso)
            )
            """;

        executeCreateTable(sql, "matricula");
    }

    private void createRetiroTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS retiro (
                num_retiro INTEGER PRIMARY KEY AUTOINCREMENT,
                num_matricula INTEGER NOT NULL,
                fecha DATE NOT NULL,
                hora TIME NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (num_matricula) REFERENCES matricula(correlativo)
            )
            """;

        executeCreateTable(sql, "retiro");
    }

    private void executeCreateTable(String sql, String tableName) {
        try (Statement stmt = databaseManager.getConnection().createStatement()) {
            stmt.execute(sql);
            System.out.println("✓ Table '" + tableName + "' created/verified successfully");
        } catch (SQLException e) {
            System.err.println("Error creating table " + tableName + ": " + e.getMessage());
            throw new RuntimeException("Failed to create table " + tableName, e);
        }
    }
}
