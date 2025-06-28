package presentation.consultas;

import application.core.interfaces.IConsulta;
import global.Result;
import infrastructure.core.models.Alumno;
import infrastructure.core.models.Curso;
import presentation.helper.WindowHelper;

import javax.swing.*;
import java.awt.*;

public class AlumnosCursosWindow extends JFrame {
    private final IConsulta consultaService;

    // Alumno
    private JTextField txtCodigoAlumno;
    private JButton btnBuscarAlumno;
    private JTextArea txtResultadoAlumno;

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
        setResizable(false);
        setTitle("Consultas de Alumnos y Cursos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 450);
        WindowHelper.centerWindow(this);

        // Alumno
        txtCodigoAlumno = new JTextField(10);
        btnBuscarAlumno = new JButton("🔍 Buscar Alumno");
        txtResultadoAlumno = new JTextArea(7, 40);
        txtResultadoAlumno.setEditable(false);
        txtResultadoAlumno.setLineWrap(true);
        txtResultadoAlumno.setWrapStyleWord(true);

        // Curso
        txtCodigoCurso = new JTextField(10);
        btnBuscarCurso = new JButton("🔍 Buscar Curso");
        txtResultadoCurso = new JTextArea(7, 40);
        txtResultadoCurso.setEditable(false);
        txtResultadoCurso.setLineWrap(true);
        txtResultadoCurso.setWrapStyleWord(true);
    }

    private void setupLayout() {
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Alumno", createAlumnoPanel());
        tabbedPane.addTab("Curso", createCursoPanel());

        setContentPane(tabbedPane);
    }

    private JPanel createAlumnoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Buscar Alumno por Código"));
        searchPanel.add(new JLabel("Código de Alumno:"));
        searchPanel.add(txtCodigoAlumno);
        searchPanel.add(btnBuscarAlumno);

        JScrollPane resultScroll = new JScrollPane(txtResultadoAlumno);
        resultScroll.setBorder(BorderFactory.createTitledBorder("Resultado de la Búsqueda"));

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(resultScroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCursoPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Buscar Curso por Código"));
        searchPanel.add(new JLabel("Código de Curso:"));
        searchPanel.add(txtCodigoCurso);
        searchPanel.add(btnBuscarCurso);

        JScrollPane resultScroll = new JScrollPane(txtResultadoCurso);
        resultScroll.setBorder(BorderFactory.createTitledBorder("Resultado de la Búsqueda"));

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(resultScroll, BorderLayout.CENTER);

        return panel;
    }

    private void setupEvents() {
        btnBuscarAlumno.addActionListener(e -> buscarAlumno());
        btnBuscarCurso.addActionListener(e -> buscarCurso());
    }

    private void buscarAlumno() {
        try {
            int codAlumno = Integer.parseInt(txtCodigoAlumno.getText().trim());

            Result<Alumno> result = consultaService.consultarAlumnoPorCodigo(codAlumno);
            if (!result.isSuccess()) {
                txtResultadoAlumno.setText("⚠ " + result.getError());
                return;
            }

            Alumno alumno = result.getValue();
            StringBuilder info = new StringBuilder();

            info.append("Nombre: ").append(alumno.getNombres()).append(" ").append(alumno.getApellidos())
                    .append("\nDNI: ").append(alumno.getDni())
                    .append("\nEdad: ").append(alumno.getEdad())
                    .append("\nCelular: ").append(alumno.getCelular())
                    .append("\nEstado: ").append(getEstadoText(alumno.getEstado()));

            if (alumno.getEstado() == 1) {
                Result<Curso> cursoResult = consultaService.consultarCursoDeAlumno(codAlumno);
                if (cursoResult.isSuccess()) {
                    Curso curso = cursoResult.getValue();
                    info.append("\n\n➡ Curso matriculado:")
                            .append("\nCódigo: ").append(curso.getCodCurso())
                            .append("\nAsignatura: ").append(curso.getAsignatura())
                            .append("\nCiclo: ").append(curso.getCiclo())
                            .append("\nCréditos: ").append(curso.getCreditos())
                            .append("\nHoras: ").append(curso.getHoras());
                } else {
                    info.append("\n\n⚠ Matriculado, pero no se pudo obtener información del curso.")
                            .append("\nMotivo: ").append(cursoResult.getError());
                }
            }

            txtResultadoAlumno.setText(info.toString());

        } catch (NumberFormatException ex) {
            txtResultadoAlumno.setText("⚠ El código debe ser un número entero.");
        }
    }

    private void buscarCurso() {
        try {
            int codCurso = Integer.parseInt(txtCodigoCurso.getText().trim());

            Result<Curso> result = consultaService.consultarCursoDeAlumno(codCurso);
            if (result.isSuccess()) {
                Curso curso = result.getValue();
                String info = "Código: " + curso.getCodCurso() +
                        "\nAsignatura: " + curso.getAsignatura() +
                        "\nCiclo: " + curso.getCiclo() +
                        "\nCréditos: " + curso.getCreditos() +
                        "\nHoras: " + curso.getHoras();

                txtResultadoCurso.setText(info);
            } else {
                txtResultadoCurso.setText("⚠ " + result.getError());
            }
        } catch (NumberFormatException ex) {
            txtResultadoCurso.setText("⚠ El código debe ser un número entero.");
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
