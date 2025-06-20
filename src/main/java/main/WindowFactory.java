package main;

import application.core.interfaces.IMatricula;
import application.core.interfaces.IRetiro;
import presentation.MainWindow;
import presentation.mantenimiento.AlumnoWindow;
import presentation.mantenimiento.CursoWindow;
import application.core.interfaces.IAlumno;
import application.core.interfaces.ICurso;
import presentation.registro.matricula.MatriculaWindow;
import presentation.registro.retiro.RetiroWindow;

/**
 * Factory for creating windows with dependency injection
 */
public class WindowFactory {
    private final ServiceContainer serviceContainer;

    public WindowFactory(ServiceContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
    }

    /**
     * Obtener objeto ServiceContainer
     */
    public ServiceContainer getServiceContainer() {
        return serviceContainer;
    }

    /**
     * Crea la ventana principal
     */
    public MainWindow createMainWindow() {
        return new MainWindow(this);
    }

    /**
     * Crea la ventana AlumnoWindow e inyecta dependencias
     */
    public AlumnoWindow createAlumnoWindow() {
        IAlumno alumnoService = serviceContainer.getService(IAlumno.class);
        return new AlumnoWindow(alumnoService);
    }

    /**
     * Crea la ventana CursoWindow e inyecta dependencias
     */
    public CursoWindow createCursoWindow() {
        ICurso cursoService = serviceContainer.getService(ICurso.class);
        return new CursoWindow(cursoService);
    }

    /**
     * Crea la ventana MatriculaWindow e inyecta dependencias
     */
    public MatriculaWindow createMatriculaWindow() {
        IMatricula matriculaService = serviceContainer.getService(IMatricula.class);
        IAlumno alumnoService = serviceContainer.getService(IAlumno.class);
        ICurso cursoService = serviceContainer.getService(ICurso.class);

        return new MatriculaWindow(matriculaService, alumnoService, cursoService);
    }

    /**
     * Crea la ventana RetiroWindow e inyecta dependencias
     */
    public RetiroWindow createRetiroWindow() {
        IRetiro retiroService = serviceContainer.getService(IRetiro.class);
        IMatricula matriculaService = serviceContainer.getService(IMatricula.class);
        ICurso cursoService = serviceContainer.getService(ICurso.class);

        return new RetiroWindow(retiroService, cursoService, matriculaService);
    }
}