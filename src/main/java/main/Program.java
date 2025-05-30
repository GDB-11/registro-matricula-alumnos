package main;

import com.formdev.flatlaf.FlatDarkLaf;
import infrastructure.core.interfaces.IAlumnoRepository;
import infrastructure.core.services.AlumnoRepository;
import presentation.MainWindow;

public class Program {
    private static ServiceContainer container;
    private static WindowFactory windowFactory;

    public static void main(String[] args) {
        container = new ServiceContainer();
        configureServices(container);
        windowFactory = new WindowFactory(container);
        runApplication();
    }

    private static void configureServices(ServiceContainer container) {
        //Lógica de inicialización de base de datos
        DatabaseManager databaseManager = new DatabaseManager();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(databaseManager);

        databaseInitializer.initializeTables();
        container.addInstance(DatabaseInitializer.class, databaseInitializer);

        container.addInstance(DatabaseManager.class, databaseManager);

        //Repositorios
        container.addSingleton(IAlumnoRepository.class, AlumnoRepository.class);

        //Servicios

        //Ventanas
        container.addInstance(WindowFactory.class, new WindowFactory(container));
    }

    private static void runApplication() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            FlatDarkLaf.setup();
            MainWindow mainWindow = windowFactory.createMainWindow();
            mainWindow.setVisible(true);
        });
    }

    public static <T> T getService(Class<T> serviceType) {
        return container.getService(serviceType);
    }

    public static WindowFactory getWindowFactory() {
        return windowFactory;
    }
}
