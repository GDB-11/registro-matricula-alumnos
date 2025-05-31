package application.core.interfaces;

import java.util.List;

import global.Result;
import infrastructure.core.models.Alumno;

public interface IAlumno {
	Result<List<Alumno>> getAllAlumnos();
}
