package application.core.services;

import application.core.interfaces.IMatricula;
import global.DateHelper;
import global.Result;
import infrastructure.core.interfaces.IAlumnoRepository;
import infrastructure.core.interfaces.IMatriculaRepository;
import infrastructure.core.models.Matricula;

import java.util.Date;
import java.util.List;

public class MatriculaService implements IMatricula {
    private final IMatriculaRepository _matriculaRepository;
    private final IAlumnoRepository _alumnoRepository;

    public MatriculaService(IMatriculaRepository matriculaRepository, IAlumnoRepository alumnoRepository) {
        _matriculaRepository = matriculaRepository;
        _alumnoRepository = alumnoRepository;
    }

    public Result<List<Matricula>> getAllMatriculas() {
        return _matriculaRepository.getAllMatriculas();
    }

    public Result<Matricula> saveMatricula(int codAlumno, int codCurso) {
        Result<Boolean> alumnoMatriculado = _matriculaRepository.alumnoMatriculado(codAlumno);

        if (alumnoMatriculado.isError()) {
            return Result.error(alumnoMatriculado.getError());
        }

        if (alumnoMatriculado.getValue()) {
            return Result.error("No se puede matricular al alumno nuevamente");
        }

        Date fecha = new Date();

        Matricula matricula = new Matricula(codAlumno, codCurso, DateHelper.getFormattedDate(fecha), DateHelper.getFormattedTime(fecha));

        _alumnoRepository.updateToMatriculado(codAlumno);

        return _matriculaRepository.saveMatricula(matricula);
    }

    public Result<Matricula> editMatricula(int codMatricula, int codCurso) {
        Date fecha = new Date();

        Matricula matricula = new Matricula();
        matricula.setNumMatricula(codMatricula);
        matricula.setCodCurso(codCurso);
        matricula.setFecha(DateHelper.getFormattedDate(fecha));
        matricula.setHora(DateHelper.getFormattedTime(fecha));

        return _matriculaRepository.editMatricula(matricula);
    }

    /*public Result<Void> deleteMatricula(int numMatricula) {

    }*/
}
