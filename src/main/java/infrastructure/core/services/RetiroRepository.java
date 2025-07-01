package infrastructure.core.services;

import global.Result;
import infrastructure.core.interfaces.IRetiroRepository;
import infrastructure.core.models.Retiro;
import main.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RetiroRepository implements IRetiroRepository {
    private final DatabaseManager _databaseManager;

    public RetiroRepository(DatabaseManager databaseManager) {
        _databaseManager = databaseManager;
    }

    public Result<List<Retiro>> getAllRetiros() {
        String sql = "SELECT * FROM retiro ORDER BY num_retiro";

        List<Retiro> retiros = new ArrayList<>();

        try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int numRetiro = rs.getInt("num_retiro");
                int numMatricula = rs.getInt("num_matricula");
                String fecha = rs.getString("fecha");
                String hora = rs.getString("hora");

                retiros.add(new Retiro(numRetiro, numMatricula, fecha, hora));
            }

            return Result.success(retiros);
        } catch (SQLException e) {
            return Result.error("Error obteniendo todas los retiros", e);
        } finally {
            _databaseManager.closeConnection();
        }
    }

    public Result<Retiro> saveRetiro(Retiro retiro) {
        String sql = """
                INSERT INTO retiro (num_matricula, fecha, hora)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, retiro.getNumMatricula());
            stmt.setString(2, retiro.getFecha());
            stmt.setString(3, retiro.getHora());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        retiro.setNumRetiro(generatedKeys.getInt(1));
                        return Result.success(retiro);
                    }
                }
            }

            return Result.error("No se pudo insertar el retiro");
        } catch (SQLException e) {
            return Result.error("Excepción al insertar el retiro", e);
        } finally {
            _databaseManager.closeConnection();
        }
    }

    public Result<Retiro> getRetiroByCodigo(int numRetiro) {
        String sql = "SELECT * FROM retiro WHERE num_retiro = ?";

        try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, numRetiro);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int codigoRetiro = rs.getInt("num_retiro");
                    int numMatricula = rs.getInt("num_matricula");
                    String fecha = rs.getString("fecha");
                    String hora = rs.getString("hora");

                    return Result.success(new Retiro(codigoRetiro, numMatricula, fecha, hora));
                } else {
                    return Result.error("No se encontró el retiro con código " + numRetiro);
                }
            }
        } catch (SQLException e) {
            return Result.error("Error obteniendo retiro con código " + numRetiro, e);
        } finally {
            _databaseManager.closeConnection();
        }
    }

    public Result<Void> deleteRetiro(int numRetiro) {
        String sql = "DELETE FROM retiro WHERE num_retiro = ?";

        try(PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql, Statement.NO_GENERATED_KEYS)) {
            stmt.setInt(1, numRetiro);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                return Result.success();
            }

            return Result.error("No se pudo cancelar el retiro");
        } catch (SQLException e) {
            return Result.error("Excepción al cancelar el retiro", e);
        } finally {
            _databaseManager.closeConnection();
        }
    }
}
