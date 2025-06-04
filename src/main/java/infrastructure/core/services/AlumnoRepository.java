package infrastructure.core.services;

import global.Result;
import infrastructure.core.interfaces.IAlumnoRepository;
import infrastructure.core.models.Alumno;
import main.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/*
 * Implementación de IAlumnoRepository, encargado de guardar/buscar en la base de datos
 * */
public class AlumnoRepository implements IAlumnoRepository {
	private final DatabaseManager _databaseManager;
	
	public AlumnoRepository(DatabaseManager databaseManager) {
		_databaseManager = databaseManager;
	}
	
    public Result<List<Alumno>> getAllAlumnos() {
    	String sql = "SELECT * FROM alumno ORDER BY cod_alumno";
    	
    	List<Alumno> alumnos = new ArrayList<>();
    	
    	try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
    		while (rs.next()) {
    			int codigo = rs.getInt("cod_alumno");
    			String nombres = rs.getString("nombres");
    			String apellidos = rs.getString("apellidos");
    			String dni = rs.getString("dni");
    			int edad = rs.getInt("edad");
    			int celular = rs.getInt("celular");
    			int estado = rs.getInt("estado");
    			
    			Alumno alumno = new Alumno(codigo, nombres, apellidos, dni, edad, celular, estado);    	        
                alumnos.add(alumno);
            }
    		
    		return Result.success(alumnos);
    	} catch (SQLException e) {
    		return Result.error("Error obteniendo a todos los alumnos", e);
    	}
    	
    }

    public Result<Alumno> saveAlumno(Alumno alumno) { 
    	String sql = """
                INSERT INTO alumno (nombres, apellidos, dni, edad, celular, estado) 
                VALUES (?, ?, ?, ?, ?, 0)
                """;
    	
    	try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
    		stmt.setString(1, alumno.getNombres());
            stmt.setString(2, alumno.getApellidos());
            stmt.setString(3, alumno.getDni());
            stmt.setInt(4, alumno.getEdad());
            stmt.setString(5, String.valueOf(alumno.getCelular()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
            	try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        alumno.setCodAlumno(generatedKeys.getInt(1));
                        return Result.success(alumno);
                    }
                }
            }
            
            return Result.error("No se pudo insertar el alumno");
    		
    	} catch (SQLException e) {
    		return Result.error("Excepción al insertar el alumno", e);
    	}
    }

	public Result<Boolean> dniExists(String dni) {
		String sql = "SELECT COUNT(*) FROM alumno WHERE dni = ?";

		try (PreparedStatement stmt = _databaseManager.getConnection().prepareStatement(sql)) {
			stmt.setString(1, dni);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt(1);
					return Result.success(count > 0);
				}
				return Result.success(false);
			}
		} catch (SQLException e) {
			return Result.error("Error verificando existencia de DNI: " + dni, e);
		}
	}
}
