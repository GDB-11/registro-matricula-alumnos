package presentation.mantenimiento;

import java.awt.*;
import java.io.Serial;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import application.core.interfaces.IAlumno;
import global.Result;
import infrastructure.core.models.Alumno;
import presentation.helper.ButtonHelper;
import presentation.helper.TableHelper;
import presentation.helper.WindowHelper;

public class AlumnoWindow extends JFrame {
	@Serial
	private static final long serialVersionUID = 1L;

	private final IAlumno _alumnoService;
	private JTable alumnosTable;
	private AlumnoTableModel tableModel;
	private JButton btnAdd;
	private JButton btnRefresh;

	public AlumnoWindow(IAlumno alumnoService) {
		_alumnoService = alumnoService;

		initializeComponents();
		setupLayout();
		setupEventHandlers();
		loadAlumnos();
	}

	private void initializeComponents() {
		setResizable(false);
		setTitle("Gestión de Alumnos");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 1600, 750);
		WindowHelper.centerWindow(this);

		// Initialize table model and table
		tableModel = new AlumnoTableModel();
		alumnosTable = new JTable(tableModel);
		setupTable();

		btnAdd = new JButton("+ Agregar Alumno");
		btnRefresh = new JButton("🔄 Actualizar");

		ButtonHelper.styleButton(btnAdd, new Color(52, 152, 219));
		ButtonHelper.styleButton(btnRefresh, new Color(46, 204, 113));
	}

	private void setupTable() {
		// Sección estética
		alumnosTable.setRowHeight(40);
		alumnosTable.setFont(new Font("Arial", Font.PLAIN, 12));
		alumnosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
		alumnosTable.getTableHeader().setBackground(new Color(52, 73, 94));
		alumnosTable.getTableHeader().setForeground(Color.WHITE);
		alumnosTable.setSelectionBackground(new Color(155, 193, 239));
		alumnosTable.setGridColor(new Color(189, 195, 199));

		alumnosTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Código
		alumnosTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Nombres
		alumnosTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Apellidos
		alumnosTable.getColumnModel().getColumn(3).setPreferredWidth(100); // DNI
		alumnosTable.getColumnModel().getColumn(4).setPreferredWidth(60);  // Edad
		alumnosTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Celular
		alumnosTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Estado
		alumnosTable.getColumnModel().getColumn(7).setPreferredWidth(70);  // Editar
		alumnosTable.getColumnModel().getColumn(8).setPreferredWidth(70);  // Eliminar

		// Centrar
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		alumnosTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		alumnosTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		alumnosTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		alumnosTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

		// Columnas con botones
		alumnosTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonHelper.ButtonRenderer("Editar", new Color(241, 196, 15)));
		alumnosTable.getColumnModel().getColumn(7).setCellEditor(new ButtonHelper.ButtonEditor("Editar", this::onEditAlumno));

		alumnosTable.getColumnModel().getColumn(8).setCellRenderer(new ButtonHelper.ButtonRenderer("Eliminar", new Color(231, 76, 60)));
		alumnosTable.getColumnModel().getColumn(8).setCellEditor(new ButtonHelper.ButtonEditor("Eliminar", this::onDeleteAlumno));

		// Desactivar la selección en la tabla
		alumnosTable.setSelectionModel(TableHelper.getNoSelectionModel());
	}

	private void setupLayout() {
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		topPanel.add(btnAdd);
		topPanel.add(btnRefresh);

		JScrollPane scrollPane = new JScrollPane(alumnosTable);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Alumnos"));

		JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel statusLabel = new JLabel("Listo");
		statusPanel.add(statusLabel);

		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(statusPanel, BorderLayout.SOUTH);
	}

	private void setupEventHandlers() {
		btnAdd.addActionListener(e -> onAddAlumno());
		btnRefresh.addActionListener(e -> loadAlumnos());
	}

	private void loadAlumnos() {
		SwingUtilities.invokeLater(() -> {
			try {
				Result<List<Alumno>> result = _alumnoService.getAllAlumnos();

				if (result.isSuccess()) {
					List<Alumno> alumnos = result.getValue();
					tableModel.setAlumnos(alumnos);
					updateTitle(alumnos.size());
				} else {
					showErrorMessage("Error al cargar alumnos", result.getError());
					tableModel.clearData();
				}
			} catch (Exception ex) {
				showErrorMessage("Error inesperado", "Ocurrió un error al cargar los alumnos: " + ex.getMessage());
				tableModel.clearData();
			}
		});
	}

	private void updateTitle(int count) {
		setTitle("Gestión de Alumnos - " + count + " registro" + (count == 1 ? "" : "s"));
	}

	private void onAddAlumno() {
		AddAlumnoModal modal = new AddAlumnoModal(this, _alumnoService);
		modal.setVisible(true);

		if (modal.getDialogResult()) {
			Alumno newAlumno = modal.getCreatedAlumno();
			if (newAlumno != null) {
				loadAlumnos();

				JOptionPane.showMessageDialog(this,
						"Alumno agregado exitosamente:\n" +
								newAlumno.getNombres() + " " + newAlumno.getApellidos(),
						"Éxito",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void onEditAlumno(int rowIndex) {
		Alumno selectedAlumno = tableModel.getAlumnoAt(rowIndex);
		
		EditAlumnoModal modal = new EditAlumnoModal(this, _alumnoService, selectedAlumno);
		modal.setVisible(true);

		if (modal.getDialogResult()) {
			Alumno editedAlumno = modal.getEditedAlumno();
			loadAlumnos();

			JOptionPane.showMessageDialog(this,
					"Alumno " + editedAlumno.getNombres() + " " + editedAlumno.getApellidos() + " editado exitosamente:\n",
					"Éxito",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void onDeleteAlumno(int rowIndex) {
		Alumno selectedAlumno = tableModel.getAlumnoAt(rowIndex);
		int confirm = JOptionPane.showConfirmDialog(this,
				"¿Está seguro que desea eliminar al alumno:\n" +
						selectedAlumno.getNombres() + " " + selectedAlumno.getApellidos() +
						"\nDNI: " + selectedAlumno.getDni() + "?",
				"Confirmar eliminación",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE);

		if (confirm == JOptionPane.YES_OPTION) {
			Result<Void> eliminarAlumno = _alumnoService.deleteAlumno(selectedAlumno.getCodAlumno());

			if (eliminarAlumno.isError()) {
				JOptionPane.showMessageDialog(this,
					"Ocurrió un error al eliminar al alumno",
					"Eror grave",
					JOptionPane.ERROR_MESSAGE);

				return;
			}

			tableModel.removeAlumno(rowIndex);
			JOptionPane.showMessageDialog(this,
					"Alumno eliminado correctamente",
					"Eliminación exitosa",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void showErrorMessage(String title, String message) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}

	// Modelo de tabla personalizada para Alumno con columnas de acción
	private static class AlumnoTableModel extends AbstractTableModel {
		private static final String[] COLUMN_NAMES = {
				"Código", "Nombres", "Apellidos", "DNI", "Edad", "Celular", "Estado", "", ""
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

		public Alumno getAlumnoAt(int rowIndex) {
			return alumnos.get(rowIndex);
		}

		public void removeAlumno(int rowIndex) {
			if (rowIndex >= 0 && rowIndex < alumnos.size()) {
				alumnos.remove(rowIndex);
				fireTableRowsDeleted(rowIndex, rowIndex);
			}
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
			if (columnIndex >= 7) return ""; // Columnas de botones

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
				case 0, 4, 5 -> Integer.class;
				case 7, 8 -> JButton.class;
				default -> String.class;
			};
		}

		private String getEstadoText(int estado) {
            return switch (estado) {
                case 0 -> "Registrado";
                case 1 -> "Matriculado";
                case 2 -> "Retirado";
                default -> "";
            };
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return columnIndex >= 7;
		}
	}
}