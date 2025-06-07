package main;

import presentation.MainWindow;
import presentation.mantenimiento.AlumnoWindow;
import presentation.mantenimiento.CursoWindow;
import application.core.interfaces.IAlumno;
import application.core.interfaces.ICurso;

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
}