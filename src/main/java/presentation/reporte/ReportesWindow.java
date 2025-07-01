package presentation.reporte;

import application.core.interfaces.IAlumno;
import application.core.interfaces.ICurso;
import application.core.interfaces.IMatricula;
import global.Result;
import infrastructure.core.models.Alumno;
import infrastructure.core.models.Curso;
import presentation.helper.ButtonHelper;
import presentation.helper.TableHelper;
import presentation.helper.WindowHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.Serial;
import java.util.List;

public class ReportesWindow extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    private final IMatricula _matriculaService;
    private final IAlumno _alumnoService;
    private final ICurso _cursoService;

    // Componentes del primer tab (Alumnos Registrados)
    private JTable tablaAlumnosRegistrados;
    private AlumnoTableModel modeloAlumnosRegistrados;
    private JButton btnReporteRegistrados;

    // Componentes del segundo tab (Alumnos Matriculados)
    private JTable tablaAlumnosMatriculados;
    private AlumnoTableModel modeloAlumnosMatriculados;
    private JButton btnReporteMatriculados;

    // Componentes del tercer tab (Alumnos por Curso)
    private JTable tablaAlumnosPorCurso;
    private AlumnoTableModel modeloAlumnosPorCurso;
    private JComboBox<Curso> comboCursos;

    public ReportesWindow(IMatricula matriculaService, IAlumno alumnoService, ICurso cursoService) {
        _matriculaService = matriculaService;
        _alumnoService = alumnoService;
        _cursoService = cursoService;

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadCursos();
    }

    private void initializeComponents() {
        setResizable(false);
        setTitle("Reportes del Sistema");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 1200, 700);
        WindowHelper.centerWindow(this);

        // Inicializar componentes del primer tab
        modeloAlumnosRegistrados = new AlumnoTableModel();
        tablaAlumnosRegistrados = new JTable(modeloAlumnosRegistrados);
        setupTable(tablaAlumnosRegistrados);
        btnReporteRegistrados = new JButton("Generar Reporte");
        ButtonHelper.styleButton(btnReporteRegistrados, new Color(52, 152, 219));

        // Inicializar componentes del segundo tab
        modeloAlumnosMatriculados = new AlumnoTableModel();
        tablaAlumnosMatriculados = new JTable(modeloAlumnosMatriculados);
        setupTable(tablaAlumnosMatriculados);
        btnReporteMatriculados = new JButton("Generar Reporte");
        ButtonHelper.styleButton(btnReporteMatriculados, new Color(52, 152, 219));

        // Inicializar componentes del tercer tab
        modeloAlumnosPorCurso = new AlumnoTableModel();
        tablaAlumnosPorCurso = new JTable(modeloAlumnosPorCurso);
        setupTable(tablaAlumnosPorCurso);
        comboCursos = new JComboBox<>();
        comboCursos.setFont(new Font("Arial", Font.PLAIN, 12));
        comboCursos.setPreferredSize(new Dimension(300, 30));
    }

    private void setupTable(JTable table) {
        // Configuración estética
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(52, 73, 94));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(155, 193, 239));
        table.setGridColor(new Color(189, 195, 199));

        // Configurar anchos de columna
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Código
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Nombres
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Apellidos
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // DNI
        table.getColumnModel().getColumn(4).setPreferredWidth(60);  // Edad
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Celular
        table.getColumnModel().getColumn(6).setPreferredWidth(80);  // Estado

        // Centrar contenido
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        // Desactivar selección
        table.setSelectionModel(TableHelper.getNoSelectionModel());
    }

    private void setupLayout() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));

        // Tab 1: Alumnos Registrados
        JPanel panelRegistrados = createReportPanel(
                "Alumnos con Matrícula Pendiente",
                "Lista de alumnos que están registrados pero no matriculados",
                tablaAlumnosRegistrados,
                btnReporteRegistrados
        );
        tabbedPane.addTab("Registrados", panelRegistrados);

        // Tab 2: Alumnos Matriculados
        JPanel panelMatriculados = createReportPanel(
                "Alumnos con Matrícula Vigente",
                "Lista de alumnos que están matriculados en el sistema",
                tablaAlumnosMatriculados,
                btnReporteMatriculados
        );
        tabbedPane.addTab("Matriculados", panelMatriculados);

        // Tab 3: Alumnos por Curso
        JPanel panelPorCurso = createCourseReportPanel();
        tabbedPane.addTab("Por Curso", panelPorCurso);

        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // Panel de estado
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("Seleccione un reporte para generar");
        statusPanel.add(statusLabel);
        contentPane.add(statusPanel, BorderLayout.SOUTH);
    }

    private JPanel createReportPanel(String title, String description, JTable table, JButton reportButton) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel superior con título y botón
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        descLabel.setForeground(Color.GRAY);

        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(descLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(reportButton);

        topPanel.add(titlePanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        // Scroll pane con la tabla
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCourseReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel superior
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Alumnos Matriculados por Curso");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel descLabel = new JLabel("Seleccione un curso para ver los alumnos matriculados");
        descLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        descLabel.setForeground(Color.GRAY);

        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(descLabel, BorderLayout.CENTER);

        // Panel de selección de curso
        JPanel coursePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        coursePanel.add(new JLabel("Curso:"));
        coursePanel.add(comboCursos);

        topPanel.add(titlePanel, BorderLayout.CENTER);
        topPanel.add(coursePanel, BorderLayout.SOUTH);

        // Scroll pane con la tabla
        JScrollPane scrollPane = new JScrollPane(tablaAlumnosPorCurso);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Alumnos del Curso"));

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void setupEventHandlers() {
        btnReporteRegistrados.addActionListener(e -> loadAlumnosRegistrados());
        btnReporteMatriculados.addActionListener(e -> loadAlumnosMatriculados());
        comboCursos.addActionListener(e -> {
            Curso cursoSeleccionado = (Curso) comboCursos.getSelectedItem();
            if (cursoSeleccionado != null) {
                loadAlumnosPorCurso(cursoSeleccionado.getCodCurso());
            }
        });
    }

    private void loadCursos() {
        SwingUtilities.invokeLater(() -> {
            try {
                Result<List<Curso>> result = _cursoService.getAllCursos();

                if (result.isSuccess()) {
                    List<Curso> cursos = result.getValue();
                    comboCursos.removeAllItems();

                    // Agregar opción por defecto
                    comboCursos.addItem(null);
                    comboCursos.setRenderer(new DefaultListCellRenderer() {
                        @Override
                        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                            if (value == null) {
                                setText("Seleccione un curso...");
                                setForeground(Color.GRAY);
                            } else {
                                Curso curso = (Curso) value;
                                setText(curso.getAsignatura() + " (Ciclo " + curso.getCiclo() + ")");
                                setForeground(list.getForeground());
                            }
                            return this;
                        }
                    });

                    for (Curso curso : cursos) {
                        comboCursos.addItem(curso);
                    }
                } else {
                    showErrorMessage("Error al cargar cursos", result.getError());
                }
            } catch (Exception ex) {
                showErrorMessage("Error inesperado", "Ocurrió un error al cargar los cursos: " + ex.getMessage());
            }
        });
    }

    private void loadAlumnosRegistrados() {
        SwingUtilities.invokeLater(() -> {
            try {
                Result<List<Alumno>> result = _alumnoService.getAllAlumnosEstadoRegistrado();

                if (result.isSuccess()) {
                    List<Alumno> alumnos = result.getValue();
                    modeloAlumnosRegistrados.setAlumnos(alumnos);

                    JOptionPane.showMessageDialog(this,
                            "Reporte generado exitosamente.\nAlumnos encontrados: " + alumnos.size(),
                            "Reporte Completado",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showErrorMessage("Error al generar reporte", result.getError());
                    modeloAlumnosRegistrados.clearData();
                }
            } catch (Exception ex) {
                showErrorMessage("Error inesperado", "Ocurrió un error al generar el reporte: " + ex.getMessage());
                modeloAlumnosRegistrados.clearData();
            }
        });
    }

    private void loadAlumnosMatriculados() {
        SwingUtilities.invokeLater(() -> {
            try {
                Result<List<Alumno>> result = _alumnoService.getAllAlumnosEstadoMatriculado();

                if (result.isSuccess()) {
                    List<Alumno> alumnos = result.getValue();
                    modeloAlumnosMatriculados.setAlumnos(alumnos);

                    JOptionPane.showMessageDialog(this,
                            "Reporte generado exitosamente.\nAlumnos encontrados: " + alumnos.size(),
                            "Reporte Completado",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showErrorMessage("Error al generar reporte", result.getError());
                    modeloAlumnosMatriculados.clearData();
                }
            } catch (Exception ex) {
                showErrorMessage("Error inesperado", "Ocurrió un error al generar el reporte: " + ex.getMessage());
                modeloAlumnosMatriculados.clearData();
            }
        });
    }

    private void loadAlumnosPorCurso(int codCurso) {
        SwingUtilities.invokeLater(() -> {
            try {
                Result<List<Alumno>> result = _matriculaService.getAllAlumnosEnCurso(codCurso);

                if (result.isSuccess()) {
                    List<Alumno> alumnos = result.getValue();
                    modeloAlumnosPorCurso.setAlumnos(alumnos);
                } else {
                    showErrorMessage("Error al cargar alumnos del curso", result.getError());
                    modeloAlumnosPorCurso.clearData();
                }
            } catch (Exception ex) {
                showErrorMessage("Error inesperado", "Ocurrió un error al cargar los alumnos del curso: " + ex.getMessage());
                modeloAlumnosPorCurso.clearData();
            }
        });
    }

    private void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private static class AlumnoTableModel extends AbstractTableModel {
        private static final String[] COLUMN_NAMES = {
                "Código", "Nombres", "Apellidos", "DNI", "Edad", "Celular", "Estado"
        };

        private List<Alumno> alumnos;

        public AlumnoTableModel() {
            this.alumnos = List.of();
        }

        public void setAlumnos(List<Alumno> alumnos) {
            this.alumnos = alumnos != null ? alumnos : List.of();
            fireTableDataChanged();
        }

        public void clearData() {
            this.alumnos = List.of();
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return alumnos.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override
        public String getColumnName(int column) {
            return COLUMN_NAMES[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Alumno alumno = alumnos.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> alumno.getCodAlumno();
                case 1 -> alumno.getNombres();
                case 2 -> alumno.getApellidos();
                case 3 -> alumno.getDni();
                case 4 -> alumno.getEdad();
                case 5 -> alumno.getCelular();
                case 6 -> getEstadoText(alumno.getEstado());
                default -> "";
            };
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0, 4, 5, 6 -> Integer.class;
                default -> String.class;
            };
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false; // Tabla de solo lectura
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
}