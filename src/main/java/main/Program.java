package main;

import application.core.interfaces.IMatricula;
import application.core.interfaces.IRetiro;
import application.core.services.MatriculaService;
import application.core.services.RetiroService;
import com.formdev.flatlaf.FlatDarkLaf;
import application.core.interfaces.IAlumno;
import application.core.interfaces.IConsulta;
import application.core.interfaces.ICurso;
import application.core.services.AlumnoService;
import application.core.services.ConsultaService;
import application.core.services.CursoService;
import infrastructure.core.interfaces.IAlumnoRepository;
import infrastructure.core.interfaces.ICursoRepository;
import infrastructure.core.interfaces.IMatriculaRepository;
import infrastructure.core.interfaces.IRetiroRepository;
import infrastructure.core.services.AlumnoRepository;
import infrastructure.core.services.CursoRepository;
import infrastructure.core.services.MatriculaRepository;
import infrastructure.core.services.RetiroRepository;
import presentation.MainWindow;

import javax.swing.*;

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
            //container.addInstance(DatabaseInitializer.class, databaseInitializer);

            // Registrar repositorios
            container.addSingleton(IAlumnoRepository.class, AlumnoRepository.class);
            container.addSingleton(ICursoRepository.class, CursoRepository.class);
            container.addSingleton(IMatriculaRepository.class, MatriculaRepository.class);
            container.addSingleton(IRetiroRepository.class, RetiroRepository.class);
            container.addSingleton(IConsulta.class, ConsultaService.class);


            // Registrar servicios
            container.addSingleton(IAlumno.class, AlumnoService.class);
            container.addSingleton(ICurso.class, CursoService.class);
            container.addSingleton(IMatricula.class, MatriculaService.class);
            container.addSingleton(IRetiro.class, RetiroService.class);
            container.addSingleton(application.core.interfaces.IConsulta.class, application.core.services.ConsultaService.class);

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

                ImageIcon icon = new ImageIcon("red-dev-simplified-logo.png");

                MainWindow mainWindow = windowFactory.createMainWindow();
                mainWindow.setVisible(true);
                mainWindow.setIconImage(icon.getImage());
                
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