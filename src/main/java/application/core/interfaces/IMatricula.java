package application.core.interfaces;

import global.Result;
import infrastructure.core.models.Alumno;
import infrastructure.core.models.Matricula;

import java.util.List;

public interface IMatricula {
    Result<List<Matricula>> getAllMatriculas();
    Result<Matricula> saveMatricula(int codAlumno, int codCurso);
    Result<Matricula> editMatricula(int codMatricula, int codCurso);
    Result<Void> deleteMatricula(int numMatricula);
    Result<List<Alumno>> getAllAlumnosEnCurso(int codCurso);
}
