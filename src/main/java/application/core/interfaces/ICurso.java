package application.core.interfaces;

import java.util.List;

import global.Result;
import infrastructure.core.models.Curso;

public interface ICurso {
    Result<List<Curso>> getAllCursos();
    Result<Curso> saveCurso(int codigo, String asignatura, int ciclo, int creditos, int horas);
    Result<Curso> editCurso(int codigo, String asignatura, int ciclo, int creditos, int horas);
    Result<Void> deleteCurso(int codigo);
    Result<Boolean> codigoExists(int codigo);
    Result<Curso> getCursoFromMatricula(int numMatricula);
}
