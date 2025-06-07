package infrastructure.core.interfaces;

import java.util.List;

import global.Result;
import infrastructure.core.models.Curso;

public interface ICursoRepository {
    Result<List<Curso>> getAllCursos();
    Result<Void> saveCurso(Curso curso);
    Result<Boolean> codigoExists(int codigo);
    Result<Void> editCurso(Curso curso);
    Result<Void> deleteCurso(int codigo);
}
