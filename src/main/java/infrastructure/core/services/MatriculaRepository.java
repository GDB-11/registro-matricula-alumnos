package infrastructure.core.services;

import global.Result;
import infrastructure.core.interfaces.IMatriculaRepository;
import infrastructure.core.models.Matricula;
import main.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MatriculaRepository implements IMatriculaRepository {
    private final DatabaseManager _databaseManager;
    String sql = """
            CREATE TABLE IF NOT EXISTS matricula (
                numMatricula INTEGER PRIMARY KEY AUTOINCREMENT,
                cod_alumno INTEGER NOT NULL,
                cod_curso INTEGER NOT NULL,
                fecha DATE NOT NULL,
                hora TIME NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (cod_alumno) REFERENCES alumno(cod_alumno),
                FOREIGN KEY (cod_curso) REFERENCES curso(cod_curso)
            )
            """;

    public MatriculaRepository(DatabaseManager databaseManager) {
        _databaseManager = databaseManager;
    }

    public Result<List<Matricula>> getAllCursos() {
        String sql = "SELECT * FROM curso ORDER BY cod_curso";

        List<Matricula> matriculas = new ArrayList<>();

        try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int codigo = rs.getInt("cod_curso");
                String asignatura = rs.getString("asignatura");
                int ciclo = rs.getInt("ciclo");
                int creditos = rs.getInt("creditos");
                int horas = rs.getInt("horas");

                cursos.add(new Curso(codigo, asignatura, ciclo, creditos, horas));
            }

            return Result.success(cursos);
        } catch (SQLException e) {
            return Result.error("Error obteniendo todos los cursos", e);
        }

    }
}
