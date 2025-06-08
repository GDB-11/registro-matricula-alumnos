package application.core.services;

import java.util.List;

import application.core.interfaces.IAlumno;
import global.Result;
import infrastructure.core.interfaces.IAlumnoRepository;
import infrastructure.core.models.Alumno;

public class AlumnoService implements IAlumno {
	private final IAlumnoRepository _alumnoRepository;
	
	public AlumnoService(IAlumnoRepository alumnoRepository) {
		_alumnoRepository = alumnoRepository;
	}
	
	public Result<List<Alumno>> getAllAlumnos() {
		return _alumnoRepository.getAllAlumnos();
	}

	public Result<Alumno> saveAlumno(String nombres, String apellidos, String dni, int edad, int celular) {
		Alumno alumno = new Alumno(nombres, apellidos, dni, edad, celular, 0);

		return _alumnoRepository.saveAlumno(alumno);
	}

	public Result<Boolean> dniExists(String dni) {
		return _alumnoRepository.dniExists(dni);
	}

	public Result<Alumno> editAlumno(int codigo, String nombres, String apellidos, int edad, int celular, int estado) {
		Alumno alumno = new Alumno(codigo, nombres, apellidos, edad, celular, estado);

		return _alumnoRepository.editAlumno(alumno);
	}

	public Result<Void> deleteAlumno(int codigo) {
		return _alumnoRepository.deleteAlumno(codigo);
	}
}
