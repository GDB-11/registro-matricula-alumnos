package application.core.services;

import java.util.List;

import application.core.interfaces.ICurso;
import global.Result;
import infrastructure.core.interfaces.IAlumnoRepository;
import infrastructure.core.interfaces.ICursoRepository;
import infrastructure.core.interfaces.IMatriculaRepository;
import infrastructure.core.models.Alumno;
import infrastructure.core.models.Curso;

public class CursoService implements ICurso {
    private final ICursoRepository _cursoRepository;
    private final IAlumnoRepository _alumnoRepository;

    public CursoService(ICursoRepository cursoRepository, IAlumnoRepository alumnoRepository) {
        _cursoRepository = cursoRepository;
        _alumnoRepository = alumnoRepository;
    }

    public Result<List<Curso>> getAllCursos() {
        return _cursoRepository.getAllCursos();
    }

    public Result<Curso> saveCurso(int codigo, String asignatura, int ciclo, int creditos, int horas) {
        Curso curso = new Curso(codigo, asignatura, ciclo, creditos, horas);

        return _cursoRepository.saveCurso(curso);
    }

    public Result<Curso> editCurso(int codigo, String asignatura, int ciclo, int creditos, int horas) {
        Curso curso = new Curso(codigo, asignatura, ciclo, creditos, horas);

        return _cursoRepository.editCurso(curso);
    }

    public Result<Void> deleteCurso(int codigo) {
        Result<List<Alumno>> alumnosMatriculados = _alumnoRepository.getAlumnosMatriculadosEnCurso(codigo);

        if (alumnosMatriculados.isError()) {
            return Result.error(alumnosMatriculados.getError());
        }

        if (!alumnosMatriculados.getValue().isEmpty()) {
            return Result.error("No se puede eliminar el curso ya que hay alumnos matriculados en él");
        }

        return _cursoRepository.deleteCurso(codigo);
    }

    public Result<Boolean> codigoExists(int codigo) {
        return _cursoRepository.codigoExists(codigo);
    }

    public Result<Curso> getCursoFromMatricula(int numMatricula) {
        return _cursoRepository.getCursoFromMatricula(numMatricula);
    }
}
