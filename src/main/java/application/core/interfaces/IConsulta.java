package application.core.interfaces;

import global.Result;
import infrastructure.core.models.Alumno;
import infrastructure.core.models.Curso;

public interface IConsulta {
  Result<Alumno> consultarAlumnoPorCodigo(int codAlumno);
  Result<Curso> consultarCursoDeAlumno(int codAlumno);
}