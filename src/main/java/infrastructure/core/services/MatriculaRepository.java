package infrastructure.core.services;

import global.Result;
import infrastructure.core.interfaces.IMatriculaRepository;
import infrastructure.core.models.Matricula;
import main.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MatriculaRepository implements IMatriculaRepository {
    private final DatabaseManager _databaseManager;

    public MatriculaRepository(DatabaseManager databaseManager) {
        _databaseManager = databaseManager;
    }

    public Result<List<Matricula>> getAllMatriculas() {
        String sql = "SELECT * FROM matricula ORDER BY num_matricula";

        List<Matricula> matriculas = new ArrayList<>();

        try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int numMatricula = rs.getInt("num_matricula");
                int codAlumno = rs.getInt("cod_alumno");
                int codCurso = rs.getInt("cod_curso");
                String fecha = rs.getString("fecha");
                String hora = rs.getString("hora");

                matriculas.add(new Matricula(numMatricula, codAlumno, codCurso, fecha, hora));
            }

            return Result.success(matriculas);
        } catch (SQLException e) {
            return Result.error("Error obteniendo todas los matrículas", e);
        }
    }

    public Result<Matricula> saveMatricula(Matricula matricula) {
        String sql = """
                INSERT INTO matricula (cod_alumno, cod_curso, fecha, hora)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, matricula.getCodAlumno());
            stmt.setInt(2, matricula.getCodCurso());
            stmt.setString(3, matricula.getFecha());
            stmt.setString(4, matricula.getHora());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        matricula.setCodAlumno(generatedKeys.getInt(1));
                        return Result.success(matricula);
                    }
                }
            }

            return Result.error("No se pudo insertar la matrícula");
        } catch (SQLException e) {
            return Result.error("Excepción al insertar la matrícula", e);
        }
    }

    public Result<Matricula> editMatricula(Matricula matricula) {
        String sql = """
                UPDATE matricula
                SET cod_curso = ?
                WHERE num_matricula = ?
                """;

        try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql,
                Statement.NO_GENERATED_KEYS)) {
            stmt.setInt(1, matricula.getCodCurso());
            stmt.setInt(2, matricula.getNumMatricula());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                return Result.success(matricula);
            }

            return Result.error("No se pudo editar la matrícula");
        } catch (SQLException e) {
            return Result.error("Excepción al editar la matrícula", e);
        }
    }

    public Result<Void> deleteMatricula(int numMatricula) {
        String sql = "DELETE FROM matricula WHERE num_matricula = ?";

        try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql,
                Statement.NO_GENERATED_KEYS)) {
            stmt.setInt(1, numMatricula);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                return Result.success();
            }

            return Result.error("No se pudo eliminar la matrícula");
        } catch (SQLException e) {
            return Result.error("Excepción al eliminar la matrícula", e);
        }
    }

    public Result<Boolean> alumnoMatriculado(int codAlumno) {
        String sql = "SELECT COUNT(*) FROM matricula WHERE cod_alumno = ?";

        try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codAlumno);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return Result.success(count > 0);
                }
                return Result.success(false);
            }
        } catch (SQLException e) {
            return Result.error("Error verificando existencia de alumno: " + codAlumno + " en matrícula", e);
        }
    }

    public Result<Matricula> getMatriculaByCodigo(int numMatricula) {
        String sql = "SELECT * FROM matricula WHERE num_matricula = ?";

        try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, numMatricula);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int codigo = rs.getInt("num_matricula");
                    int codAlumno = rs.getInt("cod_alumno");
                    int codCurso = rs.getInt("cod_curso");
                    String fecha = rs.getString("fecha");
                    String hora = rs.getString("hora");

                    return Result.success(new Matricula(codigo, codAlumno, codCurso, fecha, hora));
                } else {
                    return Result.error("No se encontró la matrícula con código " + numMatricula);
                }
            }
        } catch (SQLException e) {
            return Result.error("Error obteniendo la matrícula con código " + numMatricula, e);
        }
    }

    public Result<Matricula> getMatriculaByCodalumno(int codAlumno) {
        String sql = "SELECT * FROM matricula WHERE cod_alumno = ? LIMIT 1";
        try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, codAlumno);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int numMatricula = rs.getInt("num_matricula");
                    int codCurso = rs.getInt("cod_curso");
                    String fecha = rs.getString("fecha");
                    String hora = rs.getString("hora");

                    return Result.success(new Matricula(numMatricula, codAlumno, codCurso, fecha, hora));
                } else {
                    return Result.error("No se encontró matrícula para el alumno");
                }
            }
        } catch (SQLException e) {
            return Result.error("Error buscando matrícula del alumno", e);
        }
    }
}
