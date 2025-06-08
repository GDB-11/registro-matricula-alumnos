package presentation.mantenimiento;

import java.io.Serial;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import application.core.interfaces.ICurso;
import global.Result;
import infrastructure.core.models.Curso;
import presentation.helper.ButtonHelper;
import presentation.helper.TableHelper;
import presentation.helper.WindowHelper;

public class CursoWindow extends JFrame {
    @Serial
	private static final long serialVersionUID = 1L;

	private final ICurso _cursoService;
	private JTable cursosTable;
	private CursoTableModel tableModel;
	private JButton btnAdd;
	private JButton btnRefresh;

    public CursoWindow(ICurso cursoService) {
        _cursoService = cursoService;

        initializeComponents();
		setupLayout();
		setupEventHandlers();
		loadCursos();
    }

    private void initializeComponents() {
		setResizable(false);
		setTitle("Gestión de Cursos");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 1600, 750);
		WindowHelper.centerWindow(this);

		// Initialize table model and table
		tableModel = new CursoTableModel();
		cursosTable = new JTable(tableModel);
		setupTable();

		btnAdd = new JButton("+ Agregar Curso");
		btnRefresh = new JButton("🔄 Actualizar");

		ButtonHelper.styleButton(btnAdd, new Color(52, 152, 219));
		ButtonHelper.styleButton(btnRefresh, new Color(46, 204, 113));
	}

    private void setupTable() {
		// Sección estética
		cursosTable.setRowHeight(40);
		cursosTable.setFont(new Font("Arial", Font.PLAIN, 12));
		cursosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
		cursosTable.getTableHeader().setBackground(new Color(52, 73, 94));
		cursosTable.getTableHeader().setForeground(Color.WHITE);
		cursosTable.setSelectionBackground(new Color(155, 193, 239));
		cursosTable.setGridColor(new Color(189, 195, 199));

		cursosTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Código
		cursosTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Asignatura
		cursosTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Ciclo
		cursosTable.getColumnModel().getColumn(3).setPreferredWidth(60);  // Creditos
		cursosTable.getColumnModel().getColumn(4).setPreferredWidth(60);  // Horas
		cursosTable.getColumnModel().getColumn(5).setPreferredWidth(70);  // Editar
		cursosTable.getColumnModel().getColumn(6).setPreferredWidth(70);  // Eliminar

		// Centrar
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		cursosTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		cursosTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		cursosTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

		// Columnas con botones
		cursosTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonHelper.ButtonRenderer("Editar", new Color(241, 196, 15)));
		cursosTable.getColumnModel().getColumn(5).setCellEditor(new ButtonHelper.ButtonEditor("Editar", this::onEditCurso));

		cursosTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonHelper.ButtonRenderer("Eliminar", new Color(231, 76, 60)));
		cursosTable.getColumnModel().getColumn(6).setCellEditor(new ButtonHelper.ButtonEditor("Eliminar", this::onDeleteCurso));

		// Desactivar la selección en la tabla
		cursosTable.setSelectionModel(TableHelper.getNoSelectionModel());
	}

    private void setupLayout() {
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		topPanel.add(btnAdd);
		topPanel.add(btnRefresh);

		JScrollPane scrollPane = new JScrollPane(cursosTable);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Cursos"));

		JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel statusLabel = new JLabel("Listo");
		statusPanel.add(statusLabel);

		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(statusPanel, BorderLayout.SOUTH);
	}

    private void setupEventHandlers() {
		btnAdd.addActionListener(e -> onAddCurso());
		btnRefresh.addActionListener(e -> loadCursos());
	}

    private void loadCursos() {
		SwingUtilities.invokeLater(() -> {
			try {
				Result<List<Curso>> result = _cursoService.getAllCursos();

				if (result.isSuccess()) {
					List<Curso> cursos = result.getValue();
					tableModel.setCursos(cursos);
					updateTitle(cursos.size());
				} else {
					showErrorMessage("Error al cargar cursos", result.getError());
					tableModel.clearData();
				}
			} catch (Exception ex) {
				showErrorMessage("Error inesperado", "Ocurrió un error al cargar los cursos: " + ex.getMessage());
				tableModel.clearData();
			}
		});
	}

    private void updateTitle(int count) {
		setTitle("Gestión de Cursos - " + count + " registro" + (count == 1 ? "" : "s"));
	}

    private void onAddCurso() {
		AddCursoModal modal = new AddCursoModal(this, _cursoService);
		modal.setVisible(true);

		if (modal.getDialogResult()) {
			Curso newCurso = modal.getCreatedCurso();
			if (newCurso != null) {
				loadCursos();

				JOptionPane.showMessageDialog(this,
						"Curso agregado exitosamente:\n" +
								newCurso.getCodCurso() + " - " + newCurso.getAsignatura(),
						"Éxito",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

    private void onEditCurso(int rowIndex) {
		Curso selectedCurso = tableModel.getCursoAt(rowIndex);
		
		EditCursoModal modal = new EditCursoModal(this, _cursoService, selectedCurso);
		modal.setVisible(true);

		if (modal.getDialogResult()) {
			Curso editedCurso = modal.getEditedCurso();
			loadCursos();

            JOptionPane.showMessageDialog(this,
                    "Curso " + editedCurso.getCodCurso() + " - " + editedCurso.getAsignatura() + " editado exitosamente:\n",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void onDeleteCurso(int rowIndex) {
		Curso selectedCurso = tableModel.getCursoAt(rowIndex);
		int confirm = JOptionPane.showConfirmDialog(this,
				"¿Está seguro que desea eliminar el curso:\n" +
						selectedCurso.getCodCurso() + " - " + selectedCurso.getAsignatura() + "?",
				"Confirmar eliminación",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			Result<Void> eliminarCurso = _cursoService.deleteCurso(selectedCurso.getCodCurso());

			if (eliminarCurso.isError()) {
				JOptionPane.showMessageDialog(this,
					"Ocurrió un error al eliminar el curso",
					"Eror grave",
					JOptionPane.ERROR_MESSAGE);

				return;
			}

			tableModel.removeCurso(rowIndex);
			JOptionPane.showMessageDialog(this,
					"Curso eliminado correctamente",
					"Eliminación exitosa",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void showErrorMessage(String title, String message) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}

    // Modelo de tabla personalizada para Curso con columnas de acción
	private static class CursoTableModel extends AbstractTableModel {
		private static final String[] COLUMN_NAMES = {
				"Código", "Asignatura", "Ciclo", "Créditos", "Horas", "", ""
		};

		private List<Curso> cursos;

		public CursoTableModel() {
			this.cursos = List.of();
		}

		public void setCursos(List<Curso> cursos) {
			this.cursos = cursos != null ? cursos : List.of();
			fireTableDataChanged();
		}

		public void clearData() {
			this.cursos = List.of();
			fireTableDataChanged();
		}

		public Curso getCursoAt(int rowIndex) {
			return cursos.get(rowIndex);
		}

		public void removeCurso(int rowIndex) {
			if (rowIndex >= 0 && rowIndex < cursos.size()) {
				cursos.remove(rowIndex);
				fireTableRowsDeleted(rowIndex, rowIndex);
			}
		}

		@Override
		public int getRowCount() {
			return cursos.size();
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

			Curso curso = cursos.get(rowIndex);
			return switch (columnIndex) {
				case 0 -> curso.getCodCurso();
				case 1 -> curso.getAsignatura();
				case 2 -> getCicloText(curso.getCiclo());
				case 3 -> curso.getCreditos();
				case 4 -> curso.getHoras();
				default -> "";
			};
		}

		private String getCicloText(int ciclo) {
            return switch (ciclo) {
                case 0 -> "Primero";
                case 1 -> "Segundo";
                case 2 -> "Tercero";
                case 3 -> "Cuarto";
                case 4 -> "Quinto";
                case 5 -> "Sexto";
                default -> "";
            };
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex >= 5;
		}
	}
}
