package application.core.services;

import application.core.interfaces.IRetiro;
import global.ConstantsHelper;
import global.DateHelper;
import global.Result;
import infrastructure.core.interfaces.IAlumnoRepository;
import infrastructure.core.interfaces.IRetiroRepository;
import infrastructure.core.models.Alumno;
import infrastructure.core.models.Retiro;

import java.util.Date;
import java.util.List;

public class RetiroService implements IRetiro {
    private final IRetiroRepository _retiroRepository;
    private final IAlumnoRepository _alumnoRepository;

    public RetiroService(IRetiroRepository retiroRepository, IAlumnoRepository alumnoRepository) {
        _retiroRepository = retiroRepository;
        _alumnoRepository = alumnoRepository;
    }

    public Result<List<Retiro>> getAllRetiros(){
        return _retiroRepository.getAllRetiros();
    }

    public Result<Retiro> saveRetiro(int numMatricula) {
        Result<Alumno> alumnoMatriculado = _alumnoRepository.getAlumnoInMatricula(numMatricula);

        if (alumnoMatriculado.isError()) {
            return Result.error(alumnoMatriculado.getError());
        }

        Result<Void> alumnoRetirado = _alumnoRepository.updateToRetirado(alumnoMatriculado.getValue().getCodAlumno());

        if (alumnoRetirado.isError()) {
            return Result.error(alumnoRetirado.getError());
        }

        Date fecha = new Date();

        Retiro retiro = new Retiro(numMatricula, DateHelper.getFormattedDate(fecha), DateHelper.getFormattedTime(fecha));

        return _retiroRepository.saveRetiro(retiro);
    }

    public Result<Void> deleteRetiro(int numRetiro) {
        Result<Retiro> retiro = _retiroRepository.getRetiroByCodigo(numRetiro);

        if (retiro.isError()) {
            return Result.error(retiro.getError());
        }

        Result<Alumno> alumno = _alumnoRepository.getAlumnoInMatricula(retiro.getValue().getNumMatricula());

        if (alumno.isError()) {
            return Result.error(alumno.getError());
        }

        if (alumno.getValue().getEstado() != ConstantsHelper.AlumnoConstants.getEstadoRetirado()) {
            return Result.error("No se puede cancelar el retiro de un alumno si su estado no es 'retirado");
        }

        Result<Void> alumnoMatriculado = _alumnoRepository.updateToMatriculado(alumno.getValue().getCodAlumno());

        if (alumnoMatriculado.isError()) {
            return Result.error(alumnoMatriculado.getError());
        }

        return _retiroRepository.deleteRetiro(numRetiro);
    }
}
