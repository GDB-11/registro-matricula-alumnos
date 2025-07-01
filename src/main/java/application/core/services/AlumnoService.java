package application.core.services;

import java.util.ArrayList;
import java.util.List;

import application.core.interfaces.IAlumno;
import global.ConstantsHelper;
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
		Alumno alumno = new Alumno(nombres, apellidos, dni, edad, celular, ConstantsHelper.AlumnoConstants.getEstadoRegistrado());

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
		Result<Alumno> alumno = _alumnoRepository.getAlumnoByCodigo(codigo);

		if (alumno.getValue().getEstado() != ConstantsHelper.AlumnoConstants.getEstadoRegistrado()) {
			return Result.error("No es posible eliminar alumnos con estado 'matriculado' o 'retirado'");
		}

		return _alumnoRepository.deleteAlumno(codigo);
	}

	public Result<List<Alumno>> getAlumnosForMatricula() {
		return _alumnoRepository.getAlumnosForMatricula();
	}

	public Result<List<Alumno>> getAllAlumnosEstadoRegistrado() {
		Result<List<Alumno>> alumnos = _alumnoRepository.getAllAlumnos();
		List<Alumno> alumnosEstadoRegistrado = new ArrayList<>();

		if (alumnos.isError()) {
			return Result.error(alumnos.getError());
		}

		for (int i = 0; i < alumnos.getValue().size(); i++) {
			if (alumnos.getValue().get(i).getEstado() == ConstantsHelper.AlumnoConstants.getEstadoRegistrado()) {
				alumnosEstadoRegistrado.add(alumnos.getValue().get(i));
			}
		}

		return Result.success(alumnosEstadoRegistrado);
	}

	public Result<List<Alumno>> getAllAlumnosEstadoMatriculado() {
		Result<List<Alumno>> alumnos = _alumnoRepository.getAllAlumnos();
		List<Alumno> alumnosEstadoMatriculado = new ArrayList<>();

		if (alumnos.isError()) {
			return Result.error(alumnos.getError());
		}

		for (int i = 0; i < alumnos.getValue().size(); i++) {
			if (alumnos.getValue().get(i).getEstado() == ConstantsHelper.AlumnoConstants.getEstadoMatriculado()) {
				alumnosEstadoMatriculado.add(alumnos.getValue().get(i));
			}
		}

		return Result.success(alumnosEstadoMatriculado);
	}
}
