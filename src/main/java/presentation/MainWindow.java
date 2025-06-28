package presentation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import infrastructure.core.interfaces.IAlumnoRepository;
import main.WindowFactory;
import presentation.consultas.MatriculasRetirosWindow;
import presentation.helper.WindowHelper;
import presentation.mantenimiento.AlumnoWindow;
import presentation.mantenimiento.CursoWindow;
import presentation.registro.matricula.MatriculaWindow;
import presentation.registro.retiro.RetiroWindow;

import java.awt.*;
import java.io.Serial;

public class MainWindow extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    private final WindowFactory windowFactory;

    public MainWindow(WindowFactory windowFactory) {
        this.windowFactory = windowFactory;
        initializeComponents();
        //solo para pruebas
        imprimirAlumnosDesdeRepositorio();
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
      
        // Imagen de fondo
        try {
            ImageIcon backgroundIcon = new ImageIcon("red-dev-logo.png");
            JLabel backgroundLabel = new JLabel(backgroundIcon);
            backgroundLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            backgroundLabel.setLayout(null);
            setContentPane(backgroundLabel);

            System.out.println("Background image loaded successfully");
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
            JPanel contentPaneExc = new JPanel();
            contentPaneExc.setBorder(new EmptyBorder(5, 5, 5, 5));
            contentPaneExc.setLayout(null);
            contentPaneExc.setBackground(new Color(240, 240, 240));
            setContentPane(contentPaneExc);
        }
      
        // Agregar eventos click para abrir ventanas secundarias
        mntm_alumno.addActionListener(e -> openAlumnoWindow());
        mntm_curso.addActionListener(e -> openCursoWindow());
        mntm_matricula.addActionListener(e -> openMatriculaWindow());
        mntm_retiro.addActionListener(e -> openRetiroWindow());
        mntm_matriculasRetiros.addActionListener(e -> openMatriculasRetirosWindow());
        mntm_alumnosCursos.addActionListener(e -> openAlumnosCursosWindow());
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

    private void openCursoWindow() {
        try {
            CursoWindow cursoWindow = windowFactory.createCursoWindow();

            cursoWindow.setVisible(true);
            System.out.println("CursoWindow abierto");
        } catch (Exception e) {
            handleWindowError("CursoWindow", e);
        }
    }

    private void openMatriculaWindow() {
        try {
            MatriculaWindow matriculaWindow = windowFactory.createMatriculaWindow();

            matriculaWindow.setVisible(true);
            System.out.println("MatriculaWindow abierto");
        } catch (Exception e) {
            handleWindowError("MatriculaWindow", e);
        }
    }

    private void openRetiroWindow() {
        try {
            RetiroWindow retiroWindow = windowFactory.createRetiroWindow();

            retiroWindow.setVisible(true);
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
            System.out.println("AlumnosCursosWindow abierto");
        } catch (Exception e) {
            handleWindowError("AlumnoCursosWindow", e);
        }
    }

    private void openMatriculasRetirosWindow() {
        try {
            MatriculasRetirosWindow window = windowFactory.createMatriculasRetirosWindow();
            window.setVisible(true);
            System.out.println("MAtriculasRetirosWindow Abierto");
        } catch (Exception e) {
            handleWindowError("MatriculasRetirosWindow", e);
        }
    }
    //Metodo de prueba para debugg
    private void imprimirAlumnosDesdeRepositorio() {
        IAlumnoRepository alumnoRepo = windowFactory
                .getServiceContainer()
                .getService(IAlumnoRepository.class);

        var result = alumnoRepo.getAllAlumnos();
        if (result.isSuccess()) {
            System.out.println("📋 Lista de alumnos encontrados en BD:");
            for (var a : result.getValue()) {
                System.out.printf("ID: %d | Nombre: %s %s | Estado: %d%n",
                        a.getCodAlumno(), a.getNombres(), a.getApellidos(), a.getEstado());
            }
        } else {
            System.out.println("❌ Error al obtener alumnos: " + result.getError());
        }
    }

}