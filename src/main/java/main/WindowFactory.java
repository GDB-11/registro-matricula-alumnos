package main;

import presentation.MainWindow;
import presentation.mantenimiento.AlumnoWindow;
import application.core.interfaces.IAlumno;

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
}