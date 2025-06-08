package application.core.interfaces;

import java.util.List;

import global.Result;
import infrastructure.core.models.Alumno;

public interface IAlumno {
	Result<List<Alumno>> getAllAlumnos();
	Result<Alumno> saveAlumno(String nombres, String apellidos, String dni, int edad, int celular);
	Result<Boolean> dniExists(String dni);
	Result<Alumno> editAlumno(int codigo, String nombres, String apellidos, int edad, int celular, int estado);
	Result<Void> deleteAlumno(int codigo);
}
