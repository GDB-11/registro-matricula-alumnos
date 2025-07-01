package presentation.consultas;

import application.core.interfaces.IConsulta;
import global.Result;
import infrastructure.core.models.Alumno;
import infrastructure.core.models.Curso;
import presentation.helper.ButtonHelper;
import presentation.helper.ErrorHelper;
import presentation.helper.GridBagHelper;
import presentation.helper.TextFieldHelper;
import presentation.helper.WindowHelper;

import javax.swing.*;
import java.awt.*;

public class AlumnosCursosWindow extends JFrame {
    private final IConsulta consultaService;

    // Alumno
    private JTextField txtCodigoAlumno;
    private JButton btnBuscarAlumno;
    private JLabel lblErrorMessage;

    // Curso
    private JTextField txtCodigoCurso;
    private JButton btnBuscarCurso;
    private JTextArea txtResultadoCurso;

    public AlumnosCursosWindow(IConsulta consultaService) {
        this.consultaService = consultaService;

        initializeComponents();
        setupLayout();
        setupEvents();
    }

    private void initializeComponents() {
        txtCodigoAlumno = new JTextField(10);
        TextFieldHelper.styleTextField(txtCodigoAlumno);
        btnBuscarAlumno = new JButton("Buscar");
        ButtonHelper.styleButton(btnBuscarAlumno, new Color(52, 152, 219));

        txtCodigoCurso = new JTextField(10);
        TextFieldHelper.styleTextField(txtCodigoCurso);
        btnBuscarCurso = new JButton("Buscar");
        ButtonHelper.styleButton(btnBuscarCurso, new Color(52, 152, 219));

        lblErrorMessage = new JLabel();
        lblErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);
        lblErrorMessage.setForeground(Color.RED);

        setTitle("Consulta de Alumnos y Cursos");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(700, 400);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Panel para busqueda de alumno
        JPanel alumnoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcAlumno = new GridBagConstraints();
        gbcAlumno.insets = new Insets(8, 8, 8, 8);
        gbcAlumno.anchor = GridBagConstraints.WEST;

        gbcAlumno.fill = GridBagConstraints.HORIZONTAL;
        gbcAlumno.weightx = 1.0;

        GridBagHelper.addLabelAndComponent(alumnoPanel, gbcAlumno, 0, "Código de alumno:", txtCodigoAlumno);

        gbcAlumno.gridx = 2;
        gbcAlumno.gridy = 0;
        gbcAlumno.gridwidth = 1;
        alumnoPanel.add(btnBuscarAlumno, gbcAlumno);

        // Panel para busqueda de curso

        JPanel cursoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcCurso = new GridBagConstraints();
        gbcCurso.insets = new Insets(8, 8, 8, 8);
        gbcCurso.anchor = GridBagConstraints.WEST;

        gbcCurso.fill = GridBagConstraints.HORIZONTAL;
        gbcCurso.weightx = 1.0; // Esto permite que el campo se expanda con el panel

        GridBagHelper.addLabelAndComponent(cursoPanel, gbcCurso, 0, "Código de Curso:", txtCodigoCurso);

        gbcCurso.gridx = 2;
        gbcCurso.gridy = 0;
        gbcAlumno.gridwidth = 1;
        cursoPanel.add(btnBuscarCurso, gbcCurso);

        // Panel de errores
        JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        errorPanel.add(lblErrorMessage);

        // Contenedor principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.add(alumnoPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(cursoPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(errorPanel);

        setContentPane(mainPanel);
    }

    private void setupEvents() {
        btnBuscarAlumno.addActionListener(e -> onBuscarAlumno());
        btnBuscarCurso.addActionListener(e -> onBuscarCurso());
    }

    private void onBuscarAlumno() {
        ErrorHelper.clearErrorMessage(lblErrorMessage);
        String textoCodigo = txtCodigoAlumno.getText().trim();

        if (textoCodigo.isEmpty()) {
            ErrorHelper.showErrorMessage(lblErrorMessage, "Debe ingresar un codigo de alumno");
            return;
        }

        try {
            int codAlumno = Integer.parseInt(textoCodigo);
            Result<Alumno> resultAlumno = consultaService.consultarAlumnoPorCodigo(codAlumno);

            if (resultAlumno.isError()) {
                ErrorHelper.showErrorMessage(lblErrorMessage, resultAlumno.getError());
                return;
            }

            Alumno alumno = resultAlumno.getValue();
            // StringBuilder info = new StringBuilder();
            Curso curso = null;

            if (alumno.getEstado() == 1) {
                Result<Curso> cursoResult = consultaService.consultarCursoDeAlumno(codAlumno);
                if (cursoResult.isSuccess()) {
                    curso = cursoResult.getValue();
                }
            }

            // txtResultadoAlumno.setText(info.toString());
            AlumnoInfoModal modal = new AlumnoInfoModal(this, alumno, curso);
            modal.setVisible(true);

        } catch (NumberFormatException e) {
            // txtResultadoAlumno.setText("⚠ El código debe ser un número entero.");
            ErrorHelper.showErrorMessage(lblErrorMessage, "El codigo debe ser un número valido");
        }
    }

    private void onBuscarCurso() {
        ErrorHelper.clearErrorMessage(lblErrorMessage);
        String textoCodigo = txtCodigoCurso.getText().trim();

        if (textoCodigo.isEmpty()) {
            ErrorHelper.showErrorMessage(lblErrorMessage, "Debe ingresar un codigo de curso");
            return;
        }

        try {
            int codCurso = Integer.parseInt(textoCodigo);
            Result<Curso> resultCurso = consultaService.consultarCursoPorCodigo(codCurso);

            if (resultCurso.isSuccess()) {
                Curso curso = resultCurso.getValue();
                CursoInfoModal modal = new CursoInfoModal(this, curso);
                modal.setVisible(true);
            } else {
                ErrorHelper.showErrorMessage(lblErrorMessage, resultCurso.getError());
            }
        } catch (NumberFormatException ex) {

            ErrorHelper.showErrorMessage(lblErrorMessage, "El codigo debe ser un número válido");
        }

    }

    private String getEstadoText(int estado) {
        return switch (estado) {
            case 0 -> "Registrado";
            case 1 -> "Matriculado";
            case 2 -> "Retirado";
            default -> "Desconocido";
        };
    }
}
