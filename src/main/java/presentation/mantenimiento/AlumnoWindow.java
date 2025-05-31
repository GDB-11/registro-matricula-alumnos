package presentation.mantenimiento;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import application.core.interfaces.IAlumno;
import global.Result;
import infrastructure.core.models.Alumno;

public class AlumnoWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final IAlumno _alumnoService;

	/**
	 * Create the frame.
	 */
	public AlumnoWindow(IAlumno alumnoService) {
		_alumnoService = alumnoService;
		
		setResizable(false);
		setTitle("Alumno");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 375);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Result<List<Alumno>> result = _alumnoService.getAllAlumnos();
		int x = 0;
	}

}
