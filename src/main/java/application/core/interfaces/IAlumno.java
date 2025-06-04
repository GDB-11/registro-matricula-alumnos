package application.core.interfaces;

import java.util.List;

import global.Result;
import infrastructure.core.models.Alumno;

public interface IAlumno {
	Result<List<Alumno>> getAllAlumnos();
	Result<Alumno> saveAlumno(String nombres, String apellidos, String dni, int edad, int celular);
	Result<Boolean> dniExists(String dni);
}
