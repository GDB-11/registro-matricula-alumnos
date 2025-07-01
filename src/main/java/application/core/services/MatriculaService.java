package application.core.services;

import application.core.interfaces.IMatricula;
import global.ConstantsHelper;
import global.DateHelper;
import global.Result;
import infrastructure.core.interfaces.IAlumnoRepository;
import infrastructure.core.interfaces.IMatriculaRepository;
import infrastructure.core.models.Alumno;
import infrastructure.core.models.Matricula;

import java.util.ArrayList;
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

    public Result<Void> deleteMatricula(int numMatricula) {
        Result<Matricula> matricula = _matriculaRepository.getMatriculaByCodigo(numMatricula);

        if (matricula.isError()) {
            return Result.error(matricula.getError());
        }

        Result<Alumno> alumno = _alumnoRepository.getAlumnoByCodigo(matricula.getValue().getCodAlumno());

        if (alumno.isError()) {
            return Result.error(alumno.getError());
        }

        if (alumno.getValue().getEstado() == ConstantsHelper.AlumnoConstants.getEstadoRetirado()) {
            return Result.error("No se puede cancelar la matrícula de un alumno con estado 'retirado'");
        }

        return _matriculaRepository.deleteMatricula(numMatricula);
    }

    public Result<List<Alumno>> getAllAlumnosEnCurso(int codCurso) {
        List<Alumno> alumnosEnCurso = new ArrayList<>();
        Result<List<Matricula>> matriculas = _matriculaRepository.getAllMatriculas();

        if (matriculas.isError()) {
            return Result.error(matriculas.getError());
        }

        for (int i = 0; i < matriculas.getValue().size(); i++) {
            if (matriculas.getValue().get(i).getCodCurso() == codCurso) {
                Result<Alumno> alumno = _alumnoRepository.getAlumnoByCodigo(matriculas.getValue().get(i).getCodAlumno());

                if (alumno.isError()) {
                    return Result.error(alumno.getError());
                }

                alumnosEnCurso.add(alumno.getValue());
            }
        }

        return Result.success(alumnosEnCurso);
    }
}
