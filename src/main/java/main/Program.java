package main;

import com.formdev.flatlaf.FlatDarkLaf;
import application.core.interfaces.IAlumno;
import application.core.services.AlumnoService;
import infrastructure.core.interfaces.IAlumnoRepository;
import infrastructure.core.services.AlumnoRepository;
import presentation.MainWindow;

/**
 * Main entry point for the application
 * Similar to C#'s Program.cs with dependency injection setup
 */
public class Program {
    private static ServiceContainer container;
    private static WindowFactory windowFactory;

    public static void main(String[] args) {
        try {
            System.out.println("Starting application...");
            
            // Inicializar el contenedor de inyección de dependencia (Dependency Injection Container)
            container = new ServiceContainer();
            configureServices(container);
            
            // Create window factory
            windowFactory = new WindowFactory(container);
            
            // Run the application
            runApplication();
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Configure all services - similar to C#'s ConfigureServices method
     */
    private static void configureServices(ServiceContainer container) {
        try {
            System.out.println("Configuring services...");
            
            // Database initialization logic
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseInitializer databaseInitializer = new DatabaseInitializer(databaseManager);
            databaseInitializer.initializeTables();
            
            // Register database instances (already created objects)
            container.addInstance(DatabaseManager.class, databaseManager);
            container.addInstance(DatabaseInitializer.class, databaseInitializer);

            // Register repositories as singletons (interface -> implementation)
            container.addSingleton(IAlumnoRepository.class, AlumnoRepository.class);

            // Register services as singletons (interface -> implementation)
            container.addSingleton(IAlumno.class, AlumnoService.class);

            // Optional: Add more services here as you create them
            // container.addSingleton(ICursoRepository.class, CursoRepository.class);
            // container.addSingleton(ICurso.class, CursoService.class);

            // Debug: Print registered services
            container.printRegisteredServices();
            
            System.out.println("Services configured successfully");
        } catch (Exception e) {
            throw new RuntimeException("Failed to configure services", e);
        }
    }

    /**
     * Run the Swing application
     */
    private static void runApplication() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Setting up UI...");
                
                // Setup look and feel
                FlatDarkLaf.setup();
                
                // Create and show main window
                MainWindow mainWindow = windowFactory.createMainWindow();
                mainWindow.setVisible(true);
                
                System.out.println("Application started successfully");
            } catch (Exception e) {
                System.err.println("Error running application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Get the service container (for accessing from other parts of the application if needed)
     * Similar to accessing IServiceProvider in C#
     */
    public static ServiceContainer getContainer() {
        return container;
    }

    /**
     * Get the window factory (for accessing from other parts of the application if needed)
     */
    public static WindowFactory getWindowFactory() {
        return windowFactory;
    }
}