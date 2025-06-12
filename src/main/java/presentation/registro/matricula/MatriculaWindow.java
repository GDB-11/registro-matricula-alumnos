package presentation.registro.matricula;

import application.core.interfaces.IAlumno;
import application.core.interfaces.ICurso;
import application.core.interfaces.IMatricula;
import global.Result;
import infrastructure.core.models.Matricula;
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

public class MatriculaWindow extends JFrame  {
    @Serial
    private static final long serialVersionUID = 1L;

    private final IMatricula _matriculaService;
    private final IAlumno _alumnoService;
    private final ICurso _cursoService;

    private JTable matriculasTable;
    private MatriculaWindow.MatriculaTableModel tableModel;
    private JButton btnAdd;
    private JButton btnRefresh;

    public MatriculaWindow(IMatricula matriculaService, IAlumno alumnoService, ICurso cursoService) {
        _matriculaService = matriculaService;
        _alumnoService = alumnoService;
        _cursoService = cursoService;

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadMatriculas();
    }

    private void initializeComponents() {
        setResizable(false);
        setTitle("Gestión de Alumnos");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 1600, 750);
        WindowHelper.centerWindow(this);

        // Initialize table model and table
        tableModel = new MatriculaWindow.MatriculaTableModel();
        matriculasTable = new JTable(tableModel);
        setupTable();

        btnAdd = new JButton("+ Agregar Matrícula");
        btnRefresh = new JButton("🔄 Actualizar");

        ButtonHelper.styleButton(btnAdd, new Color(52, 152, 219));
        ButtonHelper.styleButton(btnRefresh, new Color(46, 204, 113));
    }

    private void setupTable() {
        // Sección estética
        matriculasTable.setRowHeight(40);
        matriculasTable.setFont(new Font("Arial", Font.PLAIN, 12));
        matriculasTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        matriculasTable.getTableHeader().setBackground(new Color(52, 73, 94));
        matriculasTable.getTableHeader().setForeground(Color.WHITE);
        matriculasTable.setSelectionBackground(new Color(155, 193, 239));
        matriculasTable.setGridColor(new Color(189, 195, 199));

        matriculasTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Númer Matrícula
        matriculasTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Cod Alumno
        matriculasTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Cod Curso
        matriculasTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Fecha
        matriculasTable.getColumnModel().getColumn(4).setPreferredWidth(60);  // Hora
        matriculasTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Editar
        matriculasTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Eliminar

        // Centrar
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        matriculasTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        matriculasTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        matriculasTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        // Columnas con botones
        matriculasTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonHelper.ButtonRenderer("Editar", new Color(241, 196, 15)));
        matriculasTable.getColumnModel().getColumn(5).setCellEditor(new ButtonHelper.ButtonEditor("Editar", this::onEditMatricula));

        matriculasTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonHelper.ButtonRenderer("Eliminar", new Color(231, 76, 60)));
        matriculasTable.getColumnModel().getColumn(6).setCellEditor(new ButtonHelper.ButtonEditor("Eliminar", this::onDeleteMatricula));

        // Desactivar la selección en la tabla
        matriculasTable.setSelectionModel(TableHelper.getNoSelectionModel());
    }

    private void setupLayout() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(btnAdd);
        topPanel.add(btnRefresh);

        JScrollPane scrollPane = new JScrollPane(matriculasTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Alumnos"));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("Listo");
        statusPanel.add(statusLabel);

        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(statusPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        btnAdd.addActionListener(e -> onAddMatricula());
        btnRefresh.addActionListener(e -> loadMatriculas());
    }

    private void loadMatriculas() {
        SwingUtilities.invokeLater(() -> {
            try {
                Result<List<Matricula>> result = _matriculaService.getAllMatriculas();

                if (result.isSuccess()) {
                    List<Matricula> matriculas = result.getValue();
                    tableModel.setMatriculas(matriculas);
                    updateTitle(matriculas.size());
                } else {
                    showErrorMessage("Error al cargar matrículas", result.getError());
                    tableModel.clearData();
                }
            } catch (Exception ex) {
                showErrorMessage("Error inesperado", "Ocurrió un error al cargar las matrículas: " + ex.getMessage());
                tableModel.clearData();
            }
        });
    }

    private void updateTitle(int count) {
        setTitle("Gestión de Matrículas - " + count + " registro" + (count == 1 ? "" : "s"));
    }

    private void onAddMatricula() {
        AddMatriculaModal modal = new AddMatriculaModal(this, _matriculaService, _alumnoService, _cursoService);
        modal.setVisible(true);

        if (modal.getDialogResult()) {
            Matricula newMatricula = modal.getCreatedMatricula();
            if (newMatricula != null) {
                loadMatriculas();

                JOptionPane.showMessageDialog(this,
                        "Matrícula agregada exitosamente:\n" +
                                newMatricula.getNumMatricula(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void onEditMatricula(int rowIndex) {
        Matricula selectedMatricula = tableModel.getMatriculaAt(rowIndex);

        EditMatriculaModal modal = new EditMatriculaModal(this, _matriculaService, _cursoService, selectedMatricula);
        modal.setVisible(true);

        if (modal.getDialogResult()) {
            Matricula editedMatricula = modal.getEditedMatricula();
            loadMatriculas();

            JOptionPane.showMessageDialog(this,
                    "Matrícula " + editedMatricula.getNumMatricula() + " editada exitosamente:\n",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onDeleteMatricula(int rowIndex) {
        Matricula selectedMatricula = tableModel.getMatriculaAt(rowIndex);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cancelar la matrícula:\n" +
                        selectedMatricula.getNumMatricula() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            Result<Void> eliminarMatricula = _matriculaService.deleteMatricula(selectedMatricula.getNumMatricula());

            if (eliminarMatricula.isError()) {
                JOptionPane.showMessageDialog(this,
                        eliminarMatricula.getError(),
                        "Error grave",
                        JOptionPane.ERROR_MESSAGE);

                return;
            }

            tableModel.removeMatricula(rowIndex);
            JOptionPane.showMessageDialog(this,
                    "matrícula " + selectedMatricula.getNumMatricula() + " cancelada correctamente",
                    "Eliminación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    // Modelo de tabla personalizada para Matricula con columnas de acción
    private static class MatriculaTableModel extends AbstractTableModel {
        private static final String[] COLUMN_NAMES = {
                "Matrícula", "Alumno", "Curso", "Fecha", "Hora", "", ""
        };

        private List<Matricula> matriculas;

        public MatriculaTableModel() {
            this.matriculas = List.of();
        }

        public void setMatriculas(List<Matricula> matriculas) {
            this.matriculas = matriculas != null ? matriculas : List.of();
            fireTableDataChanged();
        }

        public void clearData() {
            this.matriculas = List.of();
            fireTableDataChanged();
        }

        public Matricula getMatriculaAt(int rowIndex) {
            return matriculas.get(rowIndex);
        }

        public void removeMatricula(int rowIndex) {
            if (rowIndex >= 0 && rowIndex < matriculas.size()) {
                matriculas.remove(rowIndex);
                fireTableRowsDeleted(rowIndex, rowIndex);
            }
        }

        @Override
        public int getRowCount() {
            return matriculas.size();
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
            if (columnIndex >= 5) return ""; // Columnas de botones

            Matricula matricula = matriculas.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> matricula.getNumMatricula();
                case 1 -> matricula.getCodAlumno();
                case 2 -> matricula.getCodCurso();
                case 3 -> matricula.getFecha();
                case 4 -> matricula.getHora();
                default -> "";
            };
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0, 1, 2 -> Integer.class;
                case 5, 6 -> JButton.class;
                default -> String.class;
            };
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex >= 5;
        }
    }
}
