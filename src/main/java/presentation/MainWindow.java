package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.WindowFactory;
import presentation.mantenimiento.AlumnoWindow;

import java.io.Serial;

public class MainWindow extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    private final WindowFactory windowFactory;

    public MainWindow(WindowFactory windowFactory) {
        this.windowFactory = windowFactory;
        initializeComponents();
    }

    private void initializeComponents() {
        setResizable(false);
        setTitle("Nuestra empresa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 750, 375);
        
        // Crear menu bar
        JMenuBar menuBar_mainWindow = new JMenuBar();
        setJMenuBar(menuBar_mainWindow);
        
        // Mantenimiento menu
        JMenu mn_mantenimiento = new JMenu("Mantenimiento");
        menuBar_mainWindow.add(mn_mantenimiento);
        
        JMenuItem mntm_alumno = new JMenuItem("Alumno");
        mn_mantenimiento.add(mntm_alumno);
        
        JMenuItem mntm_curso = new JMenuItem("Curso");
        mn_mantenimiento.add(mntm_curso);
        
        // Registro menu
        JMenu mn_registro = new JMenu("Registro");
        menuBar_mainWindow.add(mn_registro);
        
        JMenuItem mntm_matricula = new JMenuItem("Matrícula");
        mn_registro.add(mntm_matricula);
        
        JMenuItem mntm_retiro = new JMenuItem("Retiro");
        mn_registro.add(mntm_retiro);
        
        // Consulta menu
        JMenu mn_consulta = new JMenu("Consulta");
        menuBar_mainWindow.add(mn_consulta);
        
        JMenuItem mntm_alumnosCursos = new JMenuItem("Alumnos y cursos");
        mn_consulta.add(mntm_alumnosCursos);
        
        JMenuItem mntm_matriculasRetiros = new JMenuItem("Matrículas y retiros");
        mn_consulta.add(mntm_matriculasRetiros);
        
        // Reporte menu
        JMenu mn_reporte = new JMenu("Reporte");
        menuBar_mainWindow.add(mn_reporte);
        
        JMenuItem mntm_alumnosMatriculaPendiente = new JMenuItem("Alumnos con matrícula pendiente");
        mn_reporte.add(mntm_alumnosMatriculaPendiente);
        
        JMenuItem mntm_alumnosMatriculaVigente = new JMenuItem("Alumnos con matrícula vigente");
        mn_reporte.add(mntm_alumnosMatriculaVigente);
        
        JMenuItem mntm_alumnosMatriculadosPorCurso = new JMenuItem("Alumnos matriculados por curso");
        mn_reporte.add(mntm_alumnosMatriculadosPorCurso);
        
        // Content panel
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        
        // Agregar eventos click para abrir ventanas secundarias
        mntm_alumno.addActionListener(e -> openAlumnoWindow());
    }

    private void openAlumnoWindow() {
        try {
            AlumnoWindow alumnoWindow = windowFactory.createAlumnoWindow();
            alumnoWindow.setVisible(true);
            
            System.out.println("AlumnoWindow abierto");
        } catch (Exception e) {
            handleWindowError("AlumnoWindow", e);
        }
    }

    private void handleWindowError(String windowName, Exception e) {
        System.err.println("Error abriendo " + windowName + e.getMessage());
        e.printStackTrace();
        
        // Show user-friendly error dialog
        JOptionPane.showMessageDialog(
            this, 
            "Error al abrir la ventana de " + windowName + ":\n" + e.getMessage(),
            "Error", 
            JOptionPane.ERROR_MESSAGE
        );
    }
}