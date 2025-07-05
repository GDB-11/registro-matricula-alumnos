package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import application.core.interfaces.IWindow;
import main.WindowFactory;
import presentation.ayuda.AyudaWindow;
import presentation.consultas.MatriculasRetirosWindow;
import presentation.helper.WindowHelper;
import presentation.mantenimiento.AlumnoWindow;
import presentation.mantenimiento.CursoWindow;
import presentation.registro.matricula.MatriculaWindow;
import presentation.registro.retiro.RetiroWindow;
import presentation.reporte.ReportesWindow;

import java.awt.*;
import java.io.Serial;

public class MainWindow extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    private final WindowFactory windowFactory;
    private final IWindow windowService;

    public MainWindow(WindowFactory windowFactory, IWindow windowService) {
        this.windowFactory = windowFactory;
        this.windowService = windowService;
        initializeComponents();
    }

    private void initializeComponents() {
        setResizable(false);
        setTitle("Nuestra empresa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1500, 750);
        WindowHelper.centerWindow(this);

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

        JMenuItem mntm_reportes = new JMenuItem("Reportes");
        mn_reporte.add(mntm_reportes);

        // Reporte menu
        JMenu mn_ayuda = new JMenu("Ayuda");
        menuBar_mainWindow.add(mn_ayuda);

        JMenuItem mntm_acercaDe = new JMenuItem("Acerda de");
        mn_ayuda.add(mntm_acercaDe);

        // Content panel
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
      
        // Imagen de fondo
        try {
            ImageIcon backgroundIcon = new ImageIcon("red-dev-logo.png");
            JLabel backgroundLabel = new JLabel(backgroundIcon);
            backgroundLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            backgroundLabel.setLayout(null);
            setContentPane(backgroundLabel);

            System.out.println("Imagen de fondo cargada");
        } catch (Exception e) {
            System.err.println("Error cargando imagen de fondo: " + e.getMessage());
            JPanel contentPaneExc = new JPanel();
            contentPaneExc.setBorder(new EmptyBorder(5, 5, 5, 5));
            contentPaneExc.setLayout(null);
            contentPaneExc.setBackground(new Color(27, 8, 53));
            setContentPane(contentPaneExc);
        }
      
        // Agregar eventos click para abrir ventanas secundarias
        mntm_alumno.addActionListener(e -> openAlumnoWindow());
        mntm_curso.addActionListener(e -> openCursoWindow());
        mntm_matricula.addActionListener(e -> openMatriculaWindow());
        mntm_retiro.addActionListener(e -> openRetiroWindow());
        mntm_matriculasRetiros.addActionListener(e -> openMatriculasRetirosWindow());
        mntm_alumnosCursos.addActionListener(e -> openAlumnosCursosWindow());
        mntm_reportes.addActionListener(e -> openReportesWindow());
        mntm_acercaDe.addActionListener(e -> openAyudaWindow());
    }

    private void openAlumnoWindow() {
        try {
            AlumnoWindow alumnoWindow = windowFactory.createAlumnoWindow();
            alumnoWindow.setVisible(true);
            alumnoWindow.setIconImage(windowService.getWindowIcon());

            System.out.println("AlumnoWindow abierto");
        } catch (Exception e) {
            handleWindowError("AlumnoWindow", e);
        }
    }

    private void openCursoWindow() {
        try {
            CursoWindow cursoWindow = windowFactory.createCursoWindow();
            cursoWindow.setVisible(true);
            cursoWindow.setIconImage(windowService.getWindowIcon());

            System.out.println("CursoWindow abierto");
        } catch (Exception e) {
            handleWindowError("CursoWindow", e);
        }
    }

    private void openMatriculaWindow() {
        try {
            MatriculaWindow matriculaWindow = windowFactory.createMatriculaWindow();
            matriculaWindow.setVisible(true);
            matriculaWindow.setIconImage(windowService.getWindowIcon());

            System.out.println("MatriculaWindow abierto");
        } catch (Exception e) {
            handleWindowError("MatriculaWindow", e);
        }
    }

    private void openRetiroWindow() {
        try {
            RetiroWindow retiroWindow = windowFactory.createRetiroWindow();
            retiroWindow.setVisible(true);
            retiroWindow.setIconImage(windowService.getWindowIcon());

            System.out.println("RetiroWindow abierto");
        } catch (Exception e) {
            handleWindowError("RetiroWindow", e);
        }
    }

    private void handleWindowError(String windowName, Exception e) {
        System.err.println("Error abriendo " + windowName + e.getMessage());
        e.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Error al abrir la ventana de " + windowName + ":\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void openAlumnosCursosWindow() {
        try {
            var alumnosCursosWindow = windowFactory.createAlumnosCursosWindow();
            alumnosCursosWindow.setVisible(true);
            alumnosCursosWindow.setIconImage(windowService.getWindowIcon());

            System.out.println("AlumnosCursosWindow abierto");
        } catch (Exception e) {
            handleWindowError("AlumnoCursosWindow", e);
        }
    }

    private void openMatriculasRetirosWindow() {
        try {
            MatriculasRetirosWindow window = windowFactory.createMatriculasRetirosWindow();
            window.setVisible(true);
            window.setIconImage(windowService.getWindowIcon());

            System.out.println("MAtriculasRetirosWindow Abierto");
        } catch (Exception e) {
            handleWindowError("MatriculasRetirosWindow", e);
        }
    }

    private void openReportesWindow() {
        try {
            ReportesWindow window = windowFactory.createReportesWindow();
            window.setVisible(true);
            window.setIconImage(windowService.getWindowIcon());

            System.out.println("ReportesWindow Abierto");
        } catch (Exception e) {
            handleWindowError("ReportesWindow", e);
        }
    }

    private void openAyudaWindow() {
        try {
            AyudaWindow window = windowFactory.createAyudaWindow();
            window.setVisible(true);
            window.setIconImage(windowService.getWindowIcon());

            System.out.println("AyudaWindow Abierto");
        } catch (Exception e) {
            handleWindowError("AyudaWindow", e);
        }
    }
}