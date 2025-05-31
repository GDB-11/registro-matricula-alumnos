package main;

import presentation.MainWindow;
import presentation.mantenimiento.AlumnoWindow;
import application.core.interfaces.IAlumno;

/**
 * Factory for creating windows with dependency injection
 * Similar to how you might create scoped services in C#
 */
public class WindowFactory {
    private final ServiceContainer serviceContainer;

    public WindowFactory(ServiceContainer serviceContainer) {
        this.serviceContainer = serviceContainer;
    }

    /**
     * Create the main window
     */
    public MainWindow createMainWindow() {
        return new MainWindow(this);
    }

    /**
     * Create the Alumno window with injected dependencies
     */
    public AlumnoWindow createAlumnoWindow() {
        IAlumno alumnoService = serviceContainer.getService(IAlumno.class);
        return new AlumnoWindow(alumnoService);
    }

    /**
     * Generic method to create any window with dependency injection
     * This allows you to create any window type dynamically
     */
    @SuppressWarnings("unchecked")
    public <T> T createWindow(Class<T> windowType) {
        try {
            java.lang.reflect.Constructor<?>[] constructors = windowType.getConstructors();
            
            if (constructors.length == 0) {
                throw new IllegalArgumentException("No public constructors found for: " + windowType.getName());
            }

            // Use the first constructor
            java.lang.reflect.Constructor<?> constructor = constructors[0];
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];

            // Resolve constructor dependencies
            for (int i = 0; i < parameterTypes.length; i++) {
                if (parameterTypes[i] == WindowFactory.class) {
                    // Inject the WindowFactory itself if needed
                    parameters[i] = this;
                } else if (serviceContainer.isRegistered(parameterTypes[i])) {
                    // Inject registered services
                    parameters[i] = serviceContainer.getService(parameterTypes[i]);
                } else {
                    throw new IllegalArgumentException(
                        "Cannot resolve dependency: " + parameterTypes[i].getName() + 
                        " for window: " + windowType.getName()
                    );
                }
            }

            T instance = (T) constructor.newInstance(parameters);
            return instance;
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to create window: " + windowType.getName(), e);
        }
    }

    /**
     * Get the service container (in case windows need direct access)
     */
    public ServiceContainer getServiceContainer() {
        return serviceContainer;
    }

    // You can add specific factory methods for other windows as needed
    // Example for future windows:
    
    /*
    public Curso createCursoWindow() {
        ICurso cursoService = serviceContainer.getService(ICurso.class);
        return new Curso(cursoService);
    }

    public Matricula createMatriculaWindow() {
        IMatricula matriculaService = serviceContainer.getService(IMatricula.class);
        IAlumno alumnoService = serviceContainer.getService(IAlumno.class);
        ICurso cursoService = serviceContainer.getService(ICurso.class);
        return new Matricula(matriculaService, alumnoService, cursoService);
    }
    */
}