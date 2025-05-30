package infrastructure.core.interfaces;

import java.util.List;

import global.Result;
import infrastructure.core.models.Alumno;

public interface IAlumnoRepository {
	Result<List<Alumno>> getAllAlumnos();
	Result<Alumno> saveAlumno(Alumno alumno);
}
