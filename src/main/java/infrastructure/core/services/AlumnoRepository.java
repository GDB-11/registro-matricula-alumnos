package infrastructure.core.services;

import global.Result;
import infrastructure.core.interfaces.IAlumnoRepository;

import java.util.List;

public class AlumnoRepository implements IAlumnoRepository {
    /*public Result<List<Alumno>> getAllAlumnos() {

    }*/

    public Result<Void> saveAlumno() {
        return Result.success();
    }
}
