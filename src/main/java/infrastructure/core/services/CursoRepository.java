package infrastructure.core.services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import global.Result;
import infrastructure.core.interfaces.ICursoRepository;
import infrastructure.core.models.Curso;
import main.DatabaseManager;

public class CursoRepository implements ICursoRepository {
    private final DatabaseManager _databaseManager;

    public CursoRepository(DatabaseManager databaseManager) {
        _databaseManager = databaseManager;
    }

    public Result<List<Curso>> getAllCursos() {
        String sql = "SELECT * FROM curso ORDER BY cod_curso";

        List<Curso> cursos = new ArrayList<>();

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

    public Result<Curso> saveCurso(Curso curso) {
        String sql = """
                INSERT INTO curso (cod_curso, asignatura, ciclo, creditos, horas)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql, Statement.NO_GENERATED_KEYS)) {
            stmt.setInt(1, curso.getCodCurso());
            stmt.setString(2, curso.getAsignatura());
            stmt.setInt(3, curso.getCiclo());
            stmt.setInt(4, curso.getCreditos());
            stmt.setInt(5, curso.getHoras());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                return Result.success(curso);
            }

            return Result.error("No se pudo insertar el curso");

        } catch (SQLException e) {
            return Result.error("Excepción al insertar el curso", e);
        }
    }

    public Result<Boolean> codigoExists(int codigo) {
		String sql = "SELECT COUNT(*) FROM curso WHERE cod_curso = ?";

		try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql)) {
			stmt.setInt(1, codigo);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt(1);
					return Result.success(count > 0);
				}
				return Result.success(false);
			}
		} catch (SQLException e) {
			return Result.error("Error verificando existencia del código de curso: " + codigo, e);
		}
	}

    public Result<Curso> editCurso(Curso curso) {
        String sql = """
                UPDATE curso
                SET asignatura = ?, ciclo = ?,  creditos = ?, horas = ?
                WHERE cod_curso = ?
                """;

        try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql, Statement.NO_GENERATED_KEYS)) {
            stmt.setString(1, curso.getAsignatura());
            stmt.setInt(2, curso.getCiclo());
            stmt.setInt(3, curso.getCreditos());
            stmt.setInt(4, curso.getHoras());
            stmt.setInt(5, curso.getCodCurso());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                return Result.success(curso);
            }

            return Result.error("No se pudo editar el curso");

        } catch (SQLException e) {
            return Result.error("Excepción al editar el curso", e);
        }
    }

    public Result<Void> deleteCurso(int codigo) {
		String sql = "DELETE FROM curso WHERE cod_curso = ?";

		try(PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql, Statement.NO_GENERATED_KEYS)) {
			stmt.setInt(1, codigo);

			int affectedRows = stmt.executeUpdate();

			if (affectedRows > 0) {
				return Result.success();
			}

			return Result.error("No se pudo eliminar el curso");
		} catch (SQLException e) {
			return Result.error("Excepción al eliminar el curso", e);
		}
	}

    public Result<Curso> getCursoFromMatricula(int numMatricula) {
        String sql = """
				SELECT
					c.*
				FROM
					curso c
					INNER JOIN matricula m ON c.cod_curso = m.cod_curso
				WHERE
					m.num_matricula = ?;
				""";

        try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, numMatricula);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int codigo = rs.getInt("cod_curso");
                    String asignatura = rs.getString("asignatura");
                    int ciclo = rs.getInt("ciclo");
                    int creditos = rs.getInt("creditos");
                    int horas = rs.getInt("horas");

                    return Result.success(new Curso(codigo, asignatura, ciclo, creditos, horas));
                } else {
                    return Result.error("No se encontró el curso en matrícula " + numMatricula);
                }
            }
        } catch (SQLException e) {
            return Result.error("Error obteniendo curso en matrícula " + numMatricula, e);
        }
    }
}
