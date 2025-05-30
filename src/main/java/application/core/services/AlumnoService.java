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
}
