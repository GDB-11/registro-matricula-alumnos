package presentation.mantenimiento;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serial;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import application.core.interfaces.IAlumno;
import global.Result;
import infrastructure.core.models.Alumno;
import presentation.helper.ButtonHelper;
import presentation.helper.ErrorHelper;
import presentation.helper.GridBagHelper;
import presentation.helper.TextFieldHelper;
import presentation.helper.ValidationHelper;

public class AddAlumnoModal extends JDialog {
	@Serial
	private static final long serialVersionUID = 1L;

	private final IAlumno _alumnoService;
	private JTextField txtNombres;
	private JTextField txtApellidos;
	private JTextField txtDni;
	private JTextField txtEdad;
	private JTextField txtCelular;
	private JButton btnSave;
	private JButton btnCancel;
	private JLabel lblErrorMessage;

	private boolean dialogResult = false;
	private Alumno createdAlumno = null;

	public AddAlumnoModal(Frame parent, IAlumno alumnoService) {
		super(parent, "Agregar Nuevo Alumno", true);
		this._alumnoService = alumnoService;

		initializeComponents();
		setupLayout();
		setupEventHandlers();
		setupValidation();

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		pack();
		setLocationRelativeTo(parent);
	}

	private void initializeComponents() {
		txtNombres = new JTextField(20);
		txtApellidos = new JTextField(20);
		txtDni = new JTextField(8);
		txtEdad = new JTextField(3);
		txtCelular = new JTextField(9);

		TextFieldHelper.styleTextField(txtNombres);
		TextFieldHelper.styleTextField(txtApellidos);
		TextFieldHelper.styleTextField(txtDni);
		TextFieldHelper.styleTextField(txtEdad);
		TextFieldHelper.styleTextField(txtCelular);

		btnSave = new JButton("Guardar");
		btnCancel = new JButton("Cancelar");

		ButtonHelper.styleButton(btnSave, new Color(52, 152, 219));
		ButtonHelper.styleButton(btnCancel, new Color(149, 165, 166));

		lblErrorMessage = new JLabel();
		lblErrorMessage.setForeground(Color.RED);
		lblErrorMessage.setFont(new Font("Arial", Font.BOLD, 11));
		lblErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);
	}

	private void setupLayout() {
		setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.anchor = GridBagConstraints.WEST;

		JLabel titleLabel = new JLabel("Datos del Alumno");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
		//titleLabel.setForeground(new Color(52, 73, 94));
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(titleLabel, gbc);

		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;

		GridBagHelper.addLabelAndComponent(mainPanel, gbc, 1, "Nombres:", txtNombres);
		GridBagHelper.addLabelAndComponent(mainPanel, gbc, 2, "Apellidos:", txtApellidos);
		GridBagHelper.addLabelAndComponentWithHint(mainPanel, gbc, 3, "DNI:", txtDni, " (8 dígitos)");
		GridBagHelper.addLabelAndComponentWithHint(mainPanel, gbc, 4, "Edad:", txtEdad, " (años)");
		GridBagHelper.addLabelAndComponentWithHint(mainPanel, gbc, 5, "Celular:", txtCelular, " (9 dígitos)");
		
		// Contenedor de errores
		JPanel errorContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
		errorContainer.setPreferredSize(new Dimension(0, 30));
		errorContainer.setBorder(new EmptyBorder(5, 10, 5, 10));
		errorContainer.add(lblErrorMessage);

		// Contenedor externo para formulario y contenedor de errores
		JPanel contentWrapper = new JPanel(new BorderLayout());
		contentWrapper.add(mainPanel, BorderLayout.CENTER);
		contentWrapper.add(errorContainer, BorderLayout.SOUTH);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
		buttonPanel.add(btnSave);
		buttonPanel.add(btnCancel);

		add(contentWrapper, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void setupEventHandlers() {
		btnSave.addActionListener(e -> onSave());

		btnCancel.addActionListener(e -> onCancel());

		KeyAdapter enterKeyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					onSave();
				}
			}
		};

		txtNombres.addKeyListener(enterKeyListener);
		txtApellidos.addKeyListener(enterKeyListener);
		txtDni.addKeyListener(enterKeyListener);
		txtEdad.addKeyListener(enterKeyListener);
		txtCelular.addKeyListener(enterKeyListener);

		KeyAdapter clearErrorListener = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				ErrorHelper.clearErrorMessage(lblErrorMessage);
			}
		};

		txtNombres.addKeyListener(clearErrorListener);
		txtApellidos.addKeyListener(clearErrorListener);
		txtDni.addKeyListener(clearErrorListener);
		txtEdad.addKeyListener(clearErrorListener);
		txtCelular.addKeyListener(clearErrorListener);
	}

	private void setupValidation() {
		DocumentFilter asciiFilter = new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
				if (ValidationHelper.isAsciiAndSpanishOnly(string)) {
					super.insertString(fb, offset, string, attr);
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
				if (ValidationHelper.isAsciiAndSpanishOnly(text)) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		};

		((AbstractDocument) txtNombres.getDocument()).setDocumentFilter(asciiFilter);
		((AbstractDocument) txtApellidos.getDocument()).setDocumentFilter(asciiFilter);

		DocumentFilter dniFilter = new ValidationHelper.DigitsOnlyFilter(8);
		((AbstractDocument) txtDni.getDocument()).setDocumentFilter(dniFilter);

		DocumentFilter celularFilter = new ValidationHelper.DigitsOnlyFilter(9);
		((AbstractDocument) txtCelular.getDocument()).setDocumentFilter(celularFilter);

		DocumentFilter edadFilter = new ValidationHelper.DigitsOnlyFilter(3);
		((AbstractDocument) txtEdad.getDocument()).setDocumentFilter(edadFilter);
	}

	private void onSave() {
		if (validateForm()) {
			try {
				String nombres = txtNombres.getText().trim();
				String apellidos = txtApellidos.getText().trim();
				String dni = txtDni.getText().trim();
				int edad = Integer.parseInt(txtEdad.getText().trim());
				int celular = Integer.parseInt(txtCelular.getText().trim());

				Result<Alumno> result = _alumnoService.saveAlumno(nombres, apellidos, dni, edad, celular);

				if (result.isSuccess()) {
					createdAlumno = result.getValue();
					dialogResult = true;
					dispose();
				} else {
					ErrorHelper.showErrorMessage(lblErrorMessage, "Error al guardar: " + result.getError());
				}

			} catch (Exception ex) {
				ErrorHelper.showErrorMessage(lblErrorMessage, "Error inesperado: " + ex.getMessage());
			}
		}
	}

	private void onCancel() {
		dialogResult = false;
		dispose();
	}

	private boolean validateForm() {
		ErrorHelper.clearErrorMessage(lblErrorMessage);

		// Nombres
		if (txtNombres.getText().trim().isEmpty()) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "El campo Nombres es obligatorio");
			txtNombres.requestFocus();
			return false;
		}

		if (txtNombres.getText().trim().length() < 2) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "Los nombres deben tener al menos 2 caracteres");
			txtNombres.requestFocus();
			return false;
		}

		// Apellidos
		if (txtApellidos.getText().trim().isEmpty()) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "El campo Apellidos es obligatorio");
			txtApellidos.requestFocus();
			return false;
		}

		if (txtApellidos.getText().trim().length() < 2) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "Los apellidos deben tener al menos 2 caracteres");
			txtApellidos.requestFocus();
			return false;
		}

		// DNI
		String dni = txtDni.getText().trim();
		if (dni.length() != 8) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "El DNI debe tener exactamente 8 dígitos");
			txtDni.requestFocus();
			return false;
		}

		Result<Boolean> dniExists = _alumnoService.dniExists(dni);
		if (dniExists.isError()) {
			ErrorHelper.showErrorMessage(lblErrorMessage, dniExists.getError());
			txtDni.requestFocus();
			return false;
		}
		if (dniExists.getValue()) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "El DNI ya ha sido registrado");
			txtDni.requestFocus();
			return false;
		}

		// Edad
		String edadText = txtEdad.getText().trim();
		if (edadText.isEmpty()) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "El campo Edad es obligatorio");
			txtEdad.requestFocus();
			return false;
		}

		try {
			int edad = Integer.parseInt(edadText);
			if (edad < 3 || edad > 110) {
				ErrorHelper.showErrorMessage(lblErrorMessage, "La edad debe estar entre 3 y 110 años");
				txtEdad.requestFocus();
				return false;
			}
		} catch (NumberFormatException e) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "La edad debe ser un número válido");
			txtEdad.requestFocus();
			return false;
		}

		// Celular
		String celular = txtCelular.getText().trim();
		if (celular.length() != 9) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "El celular debe tener exactamente 9 dígitos");
			txtCelular.requestFocus();
			return false;
		}

		return true;
	}

	public boolean getDialogResult() {
		return dialogResult;
	}

	public Alumno getCreatedAlumno() {
		return createdAlumno;
	}
}