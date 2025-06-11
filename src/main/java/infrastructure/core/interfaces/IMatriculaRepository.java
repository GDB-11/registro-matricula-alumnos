package infrastructure.core.interfaces;

import global.Result;
import infrastructure.core.models.Matricula;

import java.util.List;

public interface IMatriculaRepository {
    Result<List<Matricula>> getAllMatriculas();
    Result<Matricula> saveMatricula(Matricula matricula);
    Result<Matricula> editMatricula(Matricula matricula);
    Result<Void> deleteMatricula(int numMatricula);
    Result<Boolean> alumnoMatriculado(int codAlumno);
}
