package presentation.registro.retiro;

import application.core.interfaces.ICurso;
import application.core.interfaces.IMatricula;
import application.core.interfaces.IRetiro;
import global.Result;
import infrastructure.core.models.Retiro;
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

public class RetiroWindow extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;

    private final IRetiro _retiroService;
    private final IMatricula _matriculaService;
    private final ICurso _cursoService;

    private JTable retirosTable;
    private RetiroWindow.RetiroTableModel tableModel;
    private JButton btnAdd;
    private JButton btnRefresh;

    public RetiroWindow(IRetiro retiroService, ICurso cursoService, IMatricula matriculaService) {
        _retiroService = retiroService;
        _matriculaService = matriculaService;
        _cursoService = cursoService;

        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadRetiros();
    }

    private void initializeComponents() {
        setResizable(false);
        setTitle("Gestión de Alumnos");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(100, 100, 1600, 750);
        WindowHelper.centerWindow(this);

        // Inicializar tabla y su modelo
        tableModel = new RetiroWindow.RetiroTableModel();
        retirosTable = new JTable(tableModel);
        setupTable();

        btnAdd = new JButton("+ Agregar Retiro");
        btnRefresh = new JButton("🔄 Actualizar");

        ButtonHelper.styleButton(btnAdd, new Color(52, 152, 219));
        ButtonHelper.styleButton(btnRefresh, new Color(46, 204, 113));
    }

    private void setupTable() {
        // Sección estética
        retirosTable.setRowHeight(40);
        retirosTable.setFont(new Font("Arial", Font.PLAIN, 12));
        retirosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        retirosTable.getTableHeader().setBackground(new Color(52, 73, 94));
        retirosTable.getTableHeader().setForeground(Color.WHITE);
        retirosTable.setSelectionBackground(new Color(155, 193, 239));
        retirosTable.setGridColor(new Color(189, 195, 199));

        retirosTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Número Retiro
        retirosTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Cod Matrícula
        retirosTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Fecha
        retirosTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Hora
        retirosTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Editar
        retirosTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Eliminar

        // Centrar
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        retirosTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        retirosTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        retirosTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        retirosTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        // Columnas con botones
        retirosTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonHelper.ButtonRenderer("Editar", new Color(241, 196, 15)));
        retirosTable.getColumnModel().getColumn(4).setCellEditor(new ButtonHelper.ButtonEditor("Editar", this::onEditRetiro));

        retirosTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonHelper.ButtonRenderer("Eliminar", new Color(231, 76, 60)));
        retirosTable.getColumnModel().getColumn(5).setCellEditor(new ButtonHelper.ButtonEditor("Eliminar", this::onDeleteRetiro));

        // Desactivar la selección en la tabla
        retirosTable.setSelectionModel(TableHelper.getNoSelectionModel());
    }

    private void setupLayout() {
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(btnAdd);
        topPanel.add(btnRefresh);

        JScrollPane scrollPane = new JScrollPane(retirosTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Alumnos"));

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusLabel = new JLabel("Listo");
        statusPanel.add(statusLabel);

        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(statusPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        btnAdd.addActionListener(e -> onAddRetiro());
        btnRefresh.addActionListener(e -> loadRetiros());
    }

    private void loadRetiros() {
        SwingUtilities.invokeLater(() -> {
            try {
                Result<List<Retiro>> result = _retiroService.getAllRetiros();

                if (result.isSuccess()) {
                    List<Retiro> retiros = result.getValue();
                    tableModel.setRetiros(retiros);
                    updateTitle(retiros.size());
                } else {
                    showErrorMessage("Error al cargar retiros", result.getError());
                    tableModel.clearData();
                }
            } catch (Exception ex) {
                showErrorMessage("Error inesperado", "Ocurrió un error al cargar los retiros: " + ex.getMessage());
                tableModel.clearData();
            }
        });
    }

    private void updateTitle(int count) {
        setTitle("Gestión de Retiros - " + count + " registro" + (count == 1 ? "" : "s"));
    }

    private void onAddRetiro() {
        AddRetiroModal modal = new AddRetiroModal(this, _retiroService, _matriculaService);
        modal.setVisible(true);

        if (modal.getDialogResult()) {
            Retiro newRetiro = modal.getCreatedRetiro();
            if (newRetiro != null) {
                loadRetiros();

                JOptionPane.showMessageDialog(this,
                        "Retiro agregado exitosamente:\n" +
                                newRetiro.getNumRetiro(),
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void onEditRetiro(int rowIndex) {
        Retiro selectedRetiro = tableModel.getRetiroAt(rowIndex);

        EditRetiroModal modal = new EditRetiroModal(this, _retiroService, _cursoService, _matriculaService, selectedRetiro);
        modal.setVisible(true);

        if (modal.getDialogResult()) {
            Retiro editedRetiro = modal.getEditedRetiro();
            loadRetiros();

            JOptionPane.showMessageDialog(this,
                    "Retiro " + editedRetiro.getNumMatricula() + " editada exitosamente\n",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void onDeleteRetiro(int rowIndex) {
        Retiro selectedRetiro = tableModel.getRetiroAt(rowIndex);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cancelar el retiro:\n" +
                        selectedRetiro.getNumRetiro() + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            Result<Void> eliminarRetiro = _retiroService.deleteRetiro(selectedRetiro.getNumRetiro());

            if (eliminarRetiro.isError()) {
                JOptionPane.showMessageDialog(this,
                        eliminarRetiro.getError(),
                        "Error grave",
                        JOptionPane.ERROR_MESSAGE);

                return;
            }

            tableModel.removeRetiro(rowIndex);
            JOptionPane.showMessageDialog(this,
                    "Retiro " + selectedRetiro.getNumRetiro() + " cancelado correctamente",
                    "Eliminación exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showErrorMessage(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    // Modelo de tabla personalizada para Retiro con columnas de acción
    private static class RetiroTableModel extends AbstractTableModel {
        private static final String[] COLUMN_NAMES = {
                "Retiro", "Matrícula", "Fecha", "Hora", "", ""
        };

        private List<Retiro> retiros;

        public RetiroTableModel() {
            this.retiros = List.of();
        }

        public void setRetiros(List<Retiro> retiros) {
            this.retiros = retiros != null ? retiros : List.of();
            fireTableDataChanged();
        }

        public void clearData() {
            this.retiros = List.of();
            fireTableDataChanged();
        }

        public Retiro getRetiroAt(int rowIndex) {
            return retiros.get(rowIndex);
        }

        public void removeRetiro(int rowIndex) {
            if (rowIndex >= 0 && rowIndex < retiros.size()) {
                retiros.remove(rowIndex);
                fireTableRowsDeleted(rowIndex, rowIndex);
            }
        }

        @Override
        public int getRowCount() {
            return retiros.size();
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
            if (columnIndex >= 4) return ""; // Columnas de botones

            Retiro retiro = retiros.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> retiro.getNumRetiro();
                case 1 -> retiro.getNumMatricula();
                case 2 -> retiro.getFecha();
                case 3 -> retiro.getHora();
                default -> "";
            };
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return switch (columnIndex) {
                case 0, 1 -> Integer.class;
                case 4, 5 -> JButton.class;
                default -> String.class;
            };
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex >= 4;
        }
    }
}
