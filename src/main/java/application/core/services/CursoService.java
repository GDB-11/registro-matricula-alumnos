package application.core.services;

import java.util.List;

import application.core.interfaces.ICurso;
import global.Result;
import infrastructure.core.interfaces.ICursoRepository;
import infrastructure.core.models.Curso;

public class CursoService implements ICurso {
    private final ICursoRepository _cursoRepository;

    public CursoService(ICursoRepository cursoRepository) {
        _cursoRepository = cursoRepository;
    }

    public Result<List<Curso>> getAllCursos() {
        return _cursoRepository.getAllCursos();
    }

    public Result<Void> saveCurso(int codigo, String asignatura, int ciclo, int creditos, int horas) {
        Curso curso = new Curso(codigo, asignatura, ciclo, creditos, horas);

        return _cursoRepository.saveCurso(curso);
    }

    public Result<Void> editCurso(int codigo, String asignatura, int ciclo, int creditos, int horas) {
        Curso curso = new Curso(codigo, asignatura, ciclo, creditos, horas);

        return _cursoRepository.editCurso(curso);
    }
}
