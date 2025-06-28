package application.core.services;

import application.core.interfaces.IConsulta;
import global.Result;
import infrastructure.core.interfaces.IAlumnoRepository;
import infrastructure.core.interfaces.ICursoRepository;
import infrastructure.core.interfaces.IMatriculaRepository;
import infrastructure.core.interfaces.IRetiroRepository;
import infrastructure.core.models.Alumno;
import infrastructure.core.models.Curso;
import infrastructure.core.models.Matricula;
import infrastructure.core.models.Retiro;

public class ConsultaService implements IConsulta {
  private final IAlumnoRepository alumnoRepository;
  private final IMatriculaRepository matriculaRepository;
  private final ICursoRepository cursoRepository;
  private final IRetiroRepository retiroRepository;

  public ConsultaService(
      IAlumnoRepository alumnoRepository,
      IMatriculaRepository matriculaRepository,
      ICursoRepository cursoRepository,
      IRetiroRepository retiroRepository) {
    this.alumnoRepository = alumnoRepository;
    this.matriculaRepository = matriculaRepository;
    this.cursoRepository = cursoRepository;
    this.retiroRepository = retiroRepository;
  }

  @Override
  public Result<Alumno> consultarAlumnoPorCodigo(int codAlumno) {
    Result<Alumno> resultado = alumnoRepository.getAlumnoByCodigo(codAlumno);
    if (!resultado.isSuccess()) {
      return Result.error("No se encontro el alumno con codigo: " + codAlumno);
    }
    return resultado;
  }

  @Override
  public Result<Curso> consultarCursoDeAlumno(int codAlumno) {
    // Paso 1: Buscar al alumno
    Result<Alumno> resultadoAlumno = alumnoRepository.getAlumnoByCodigo(codAlumno);

    if (!resultadoAlumno.isSuccess()) {
      return Result.error("No se encontró el curso con codigo: " + codAlumno);
    }

    Alumno alumno = resultadoAlumno.getValue();

    // Paso 2: Verificar que el alumno esté matriculado (estado == 1)
    if (alumno.getEstado() != 1) {
      return Result.error("El alumno no está matriculado actualmente.");
    }

    // Paso 3: Buscar la matrícula usando el código del alumno
    Result<Matricula> resultadoMatricula = matriculaRepository.getMatriculaByCodAlumno(codAlumno);

    if (!resultadoMatricula.isSuccess()) {
      return Result.error("No se encontró matrícula para el alumno.");
    }

    Matricula matricula = resultadoMatricula.getValue();

    // Paso 4: Obtener el curso vinculado a esa matrícula
    Result<Curso> resultadoCurso = cursoRepository.getCursoFromMatricula(matricula.getNumMatricula());

    if (!resultadoCurso.isSuccess()) {
      return Result.error("No se encontró el curso asociado a la matrícula.");
    }

    return resultadoCurso;
  }

  @Override
  public Result<String> consultarDatosDeMatricula(int numMatricula) {
    Result<Matricula> resultMatricula = matriculaRepository.getMatriculaByCodigo(numMatricula);
    if (!resultMatricula.isSuccess()) {
      return Result.error("Matricula no encontrada: " + numMatricula);
    }

    Matricula matricula = resultMatricula.getValue();

    int codAlumno = matricula.getCodAlumno();
    int codCurso = matricula.getCodCurso();

    Result<Alumno> resultAlumno = alumnoRepository.getAlumnoByCodigo(codAlumno);
    if (!resultAlumno.isSuccess()) {
      return Result.error("No se encontró el alumno con código: " + codAlumno);
    }

    Result<Curso> resultCurso = cursoRepository.getCursoFromMatricula(matricula.getNumMatricula());
    if (!resultCurso.isSuccess()) {
      return Result.error("No se encontró el curso con el código: " + codCurso);
    }
    Alumno alumno = resultAlumno.getValue();
    Curso curso = resultCurso.getValue();

    String datos = "Información de matrícula\n"
        + "---------------------------\n"
        + "Alumno: " + alumno.getNombres() + " " + alumno.getApellidos() + "\n"
        + "Curso: " + curso.getAsignatura() + " (Codigo: " + curso.getCodCurso() + ")\n"
        + "Fecha: " + matricula.getFecha() + " - " + matricula.getHora();
    return Result.success(datos);
  }

  @Override
  public Result<String> consultarDatosDeRetiro(int numRetiro) {
    Result<Retiro> resultRetiro = retiroRepository.getRetiroByCodigo(numRetiro);
    if (!resultRetiro.isSuccess()) {
      return Result.error("Retirno no encontrado con el número: " + numRetiro);
    }

    Retiro retiro = resultRetiro.getValue();
    int numMatricula = retiro.getNumMatricula();

    Result<Matricula> resultMatricula = matriculaRepository.getMatriculaByCodigo(numMatricula);
    if (!resultMatricula.isSuccess()) {
      return Result.error("Matricula no encontrada con el número: " + numMatricula);
    }

    Matricula matricula = resultMatricula.getValue();

    Result<Alumno> resultAlumno = alumnoRepository.getAlumnoByCodigo(matricula.getCodAlumno());
    if(!resultAlumno.isSuccess()) {
      return Result.error("Alumno no encontrado con el código: " + matricula.getCodAlumno());
    }

    Result<Curso> resultCurso = cursoRepository.getCursoFromMatricula(matricula.getNumMatricula());
    if(!resultCurso.isSuccess()) {
      return Result.error("Curso no encontrado asociado a la matrícula: " + matricula.getNumMatricula());
    }

    Alumno alumno = resultAlumno.getValue();
    Curso curso = resultCurso.getValue();

    String datos = "Información del Retiro\n"
                 + "----------------------------------\n"
                 + "Alumno: " + alumno.getNombres() + " " + alumno.getApellidos() + "\n"
                 + "Curso: " + curso.getAsignatura() + " (Código: " + curso.getCodCurso() + ")\n"
                 + "Fecha de retiro: " + retiro.getFecha() + " - " + retiro.getHora();

    return Result.success(datos);

  }

  @Override
  public Result<Curso> consultarCursoPorCodigo(int codCurso) {
    return cursoRepository.getCursoByCodigo(codCurso);
  }
}
