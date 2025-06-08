package main;

import com.formdev.flatlaf.FlatDarkLaf;
import application.core.interfaces.IAlumno;
import application.core.interfaces.ICurso;
import application.core.services.AlumnoService;
import application.core.services.CursoService;
import infrastructure.core.interfaces.IAlumnoRepository;
import infrastructure.core.interfaces.ICursoRepository;
import infrastructure.core.services.AlumnoRepository;
import infrastructure.core.services.CursoRepository;
import presentation.MainWindow;

/**
 * Orquestador de toda la aplicación
 */
public class Program {
    private static ServiceContainer container;
    private static WindowFactory windowFactory;

    public static void main(String[] args) {
        try {
            System.out.println("Iniciando aplicación...");
            
            // Inicializar el contenedor de inyección de dependencia (Dependency Injection Container)
            container = new ServiceContainer();
            configureServices(container);
            
            // Crear window factory
            windowFactory = new WindowFactory(container);
            
            // Correr aplicación
            runApplication();
        } catch (Exception e) {
            System.err.println("Error iniciando aplicación: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Configurar todos los servicios
     */
    private static void configureServices(ServiceContainer container) {
        try {
            System.out.println("Configurando servicios...");
            
            // Lógica de inicialización de la base de datos
            DatabaseManager databaseManager = new DatabaseManager();
            DatabaseInitializer databaseInitializer = new DatabaseInitializer(databaseManager);
            databaseInitializer.initializeTables();
            
            // Registrar instancias de base de datos
            container.addInstance(DatabaseManager.class, databaseManager);
            container.addInstance(DatabaseInitializer.class, databaseInitializer);

            // Registrar repositorios
            container.addSingleton(IAlumnoRepository.class, AlumnoRepository.class);
            container.addSingleton(ICursoRepository.class, CursoRepository.class);

            // Registrar servicios
            container.addSingleton(IAlumno.class, AlumnoService.class);
            container.addSingleton(ICurso.class, CursoService.class);

            // Debug: Imprimir servicios registrados
            container.printRegisteredServices();
            
            System.out.println("Servicios configurados correctamente");
        } catch (Exception e) {
            throw new RuntimeException("No se pudieron configurar los servicios", e);
        }
    }

    /**
     * Correr aplicación Java Swing
     */
    private static void runApplication() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Configurando la interfaz de usuario...");
                
                // Tema visual
                FlatDarkLaf.setup();
                
                MainWindow mainWindow = windowFactory.createMainWindow();
                mainWindow.setVisible(true);
                
                System.out.println("La aplicación se inició correctamente");
            } catch (Exception e) {
                System.err.println("Error al ejecutar la aplicación: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Obtener el contenedor de servicio
     */
    public static ServiceContainer getContainer() {
        return container;
    }

    /**
     * Obtener window factory
     */
    public static WindowFactory getWindowFactory() {
        return windowFactory;
    }
}