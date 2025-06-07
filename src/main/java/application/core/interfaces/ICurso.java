package application.core.interfaces;

import java.util.List;

import global.Result;
import infrastructure.core.models.Curso;

public interface ICurso {
    Result<List<Curso>> getAllCursos();
    Result<Void> saveCurso(int codigo, String asignatura, int ciclo, int creditos, int horas);
    Result<Void> editCurso(int codigo, String asignatura, int ciclo, int creditos, int horas);
}
