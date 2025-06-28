package infrastructure.core.interfaces;

import java.util.List;

import global.Result;
import infrastructure.core.models.Curso;

public interface ICursoRepository {
    Result<List<Curso>> getAllCursos();
    Result<Curso> saveCurso(Curso curso);
    Result<Boolean> codigoExists(int codigo);
    Result<Curso> editCurso(Curso curso);
    Result<Void> deleteCurso(int codigo);
    Result<Curso> getCursoFromMatricula(int numMatricula);
    Result<Curso> getCursoByCodigo(int codCurso);
}
