package application.core.interfaces;

import global.Result;
import infrastructure.core.models.Alumno;
import infrastructure.core.models.Curso;
import infrastructure.core.models.Matricula;
import infrastructure.core.models.Retiro;

public interface IConsulta {
  Result<Alumno> consultarAlumnoPorCodigo(int codAlumno);
  Result<Curso> consultarCursoDeAlumno(int codAlumno);
  Result<String> consultarDatosDeMatricula(int numMatricula);
  Result<String> consultarDatosDeRetiro(int numRetiro);
  Result<Curso> consultarCursoPorCodigo(int codCurso);
  Result<Matricula> getMatriculaByCodigo(int numMatricula);
  Result<Retiro> getRetiroByCodigo(int numRetiro);
}