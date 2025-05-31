package presentation;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;

    /**
	 * Create the frame.
	 */
	public MainWindow() {
    	setResizable(false);
		setTitle("Nuestra empresa");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 375);
        
        JMenuBar menuBar_mainWindow = new JMenuBar();
        setJMenuBar(menuBar_mainWindow);
        
        JMenu mn_mantenimiento = new JMenu("Mantenimiento");
        menuBar_mainWindow.add(mn_mantenimiento);
        
        JMenuItem mntm_alumno = new JMenuItem("Alumno");
        mn_mantenimiento.add(mntm_alumno);
        
        JMenuItem mntm_curso = new JMenuItem("Curso");
        mn_mantenimiento.add(mntm_curso);
        
        JMenu mn_registro = new JMenu("Registro");
        menuBar_mainWindow.add(mn_registro);
        
        JMenuItem mntm_matricula = new JMenuItem("Matrícula");
        mn_registro.add(mntm_matricula);
        
        JMenuItem mntm_retiro = new JMenuItem("Retiro");
        mn_registro.add(mntm_retiro);
        
        JMenu mn_consulta = new JMenu("Consulta");
        menuBar_mainWindow.add(mn_consulta);
        
        JMenuItem mntm_alumnosCursos = new JMenuItem("Alumnos y cursos");
        mn_consulta.add(mntm_alumnosCursos);
        
        JMenuItem mntm_matriculasRetiros = new JMenuItem("Matrículas y retiros");
        mn_consulta.add(mntm_matriculasRetiros);
        
        JMenu mn_reporte = new JMenu("Reporte");
        menuBar_mainWindow.add(mn_reporte);
        
        JMenuItem mntm_alumnosMatriculaPendiente = new JMenuItem("Alumnos con matrícula pendiente");
        mn_reporte.add(mntm_alumnosMatriculaPendiente);
        
        JMenuItem mntm_alumnosMatriculaVigente = new JMenuItem("Alumnos con matrícula vigente");
        mn_reporte.add(mntm_alumnosMatriculaVigente);
        
        JMenuItem mntm_alumnosMatriculadosPorCurso = new JMenuItem("Alumnos matriculados por curso");
        mn_reporte.add(mntm_alumnosMatriculadosPorCurso);
        JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
	}
}
