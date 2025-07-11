package main;

import application.core.interfaces.*;
import presentation.MainWindow;
import presentation.ayuda.AyudaWindow;
import presentation.consultas.AlumnosCursosWindow;
import presentation.consultas.MatriculasRetirosWindow;
import presentation.mantenimiento.AlumnoWindow;
import presentation.mantenimiento.CursoWindow;
import presentation.registro.matricula.MatriculaWindow;
import presentation.registro.retiro.RetiroWindow;
import presentation.reporte.ReportesWindow;

/**
 * Factory for creating windows with dependency injection
 */
public class WindowFactory {
    private final ServiceContainer serviceContainer;

    public WindowFactory(ServiceContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
    }

    /**
     * Crea la ventana principal
     */
    public MainWindow createMainWindow() {
        IWindow windowService = serviceContainer.getService(IWindow.class);

        return new MainWindow(this, windowService);
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

    public AlumnosCursosWindow createAlumnosCursosWindow() {
        IConsulta consultaService = serviceContainer.getService(IConsulta.class);
        return new AlumnosCursosWindow(consultaService);
    }

    /**
     * Crea la ventana MatriculaRetirosWindow
     */
    public MatriculasRetirosWindow createMatriculasRetirosWindow() {
        IConsulta consultaService = serviceContainer.getService(IConsulta.class);
        return new MatriculasRetirosWindow(consultaService);
    }

    /**
     * Crea la ventana ReportesWindow e inyecta dependencias
     */
    public ReportesWindow createReportesWindow() {
        IMatricula matriculaService = serviceContainer.getService(IMatricula.class);
        IAlumno alumnoService = serviceContainer.getService(IAlumno.class);
        ICurso cursoService = serviceContainer.getService(ICurso.class);

        return new ReportesWindow(matriculaService, alumnoService, cursoService);
    }

    public AyudaWindow createAyudaWindow() {
        return new AyudaWindow();
    }
}