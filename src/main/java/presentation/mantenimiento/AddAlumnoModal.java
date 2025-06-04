package presentation.mantenimiento;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

		gbc.gridx = 0; gbc.gridy = 1;
		mainPanel.add(createLabel("Nombres:", true), gbc);
		gbc.gridx = 1;
		mainPanel.add(txtNombres, gbc);

		gbc.gridx = 0; gbc.gridy = 2;
		mainPanel.add(createLabel("Apellidos:", true), gbc);
		gbc.gridx = 1;
		mainPanel.add(txtApellidos, gbc);

		gbc.gridx = 0; gbc.gridy = 3;
		mainPanel.add(createLabel("DNI:", true), gbc);
		gbc.gridx = 1;
		JPanel dniPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		dniPanel.add(txtDni);
		dniPanel.add(createHintLabel(" (8 dígitos)"));
		mainPanel.add(dniPanel, gbc);

		gbc.gridx = 0; gbc.gridy = 4;
		mainPanel.add(createLabel("Edad:", true), gbc);
		gbc.gridx = 1;
		JPanel edadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		edadPanel.add(txtEdad);
		edadPanel.add(createHintLabel(" (años)"));
		mainPanel.add(edadPanel, gbc);

		gbc.gridx = 0; gbc.gridy = 5;
		mainPanel.add(createLabel("Celular:", true), gbc);
		gbc.gridx = 1;
		JPanel celularPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		celularPanel.add(txtCelular);
		celularPanel.add(createHintLabel(" (9 dígitos)"));
		mainPanel.add(celularPanel, gbc);

		// Create a separate error message container with fixed height
		JPanel errorContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
		errorContainer.setPreferredSize(new Dimension(0, 30)); // Fixed height of 30 pixels
		errorContainer.setBorder(new EmptyBorder(5, 10, 5, 10));
		errorContainer.add(lblErrorMessage);

		// Create a wrapper panel to hold both main panel and error container
		JPanel contentWrapper = new JPanel(new BorderLayout());
		contentWrapper.add(mainPanel, BorderLayout.CENTER);
		contentWrapper.add(errorContainer, BorderLayout.SOUTH);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
		buttonPanel.add(btnSave);
		buttonPanel.add(btnCancel);

		add(contentWrapper, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private JLabel createLabel(String text, boolean required) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.BOLD, 12));
		if (required) {
			label.setText(text + " *");
			//label.setForeground(new Color(52, 73, 94));
		}
		return label;
	}

	private JLabel createHintLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.ITALIC, 10));
		label.setForeground(Color.GRAY);
		return label;
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
				clearErrorMessage();
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
				if (isAsciiOnly(string)) {
					super.insertString(fb, offset, string, attr);
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
				if (isAsciiOnly(text)) {
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

	private boolean isAsciiOnly(String text) {
		return text.chars().allMatch(c -> c < 128 && (Character.isLetter(c) || Character.isWhitespace(c)));
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
					showErrorMessage("Error al guardar: " + result.getError());
				}

			} catch (Exception ex) {
				showErrorMessage("Error inesperado: " + ex.getMessage());
			}
		}
	}

	private void onCancel() {
		dialogResult = false;
		dispose();
	}

	private boolean validateForm() {
		clearErrorMessage();

		// Nombres
		if (txtNombres.getText().trim().isEmpty()) {
			showErrorMessage("El campo Nombres es obligatorio");
			txtNombres.requestFocus();
			return false;
		}

		if (txtNombres.getText().trim().length() < 2) {
			showErrorMessage("Los nombres deben tener al menos 2 caracteres");
			txtNombres.requestFocus();
			return false;
		}

		// Apellidos
		if (txtApellidos.getText().trim().isEmpty()) {
			showErrorMessage("El campo Apellidos es obligatorio");
			txtApellidos.requestFocus();
			return false;
		}

		if (txtApellidos.getText().trim().length() < 2) {
			showErrorMessage("Los apellidos deben tener al menos 2 caracteres");
			txtApellidos.requestFocus();
			return false;
		}

		// DNI
		String dni = txtDni.getText().trim();
		if (dni.length() != 8) {
			showErrorMessage("El DNI debe tener exactamente 8 dígitos");
			txtDni.requestFocus();
			return false;
		}

		Result<Boolean> dniExists = _alumnoService.dniExists(dni);
		if (dniExists.isError()) {
			showErrorMessage(dniExists.getError());
			txtDni.requestFocus();
			return false;
		}
		if (dniExists.getValue()) {
			showErrorMessage("El DNI ya ha sido registrado");
			txtDni.requestFocus();
			return false;
		}

		// Edad
		String edadText = txtEdad.getText().trim();
		if (edadText.isEmpty()) {
			showErrorMessage("El campo Edad es obligatorio");
			txtEdad.requestFocus();
			return false;
		}

		try {
			int edad = Integer.parseInt(edadText);
			if (edad < 3 || edad > 110) {
				showErrorMessage("La edad debe estar entre 3 y 110 años");
				txtEdad.requestFocus();
				return false;
			}
		} catch (NumberFormatException e) {
			showErrorMessage("La edad debe ser un número válido");
			txtEdad.requestFocus();
			return false;
		}

		// Celular
		String celular = txtCelular.getText().trim();
		if (celular.length() != 9) {
			showErrorMessage("El celular debe tener exactamente 9 dígitos");
			txtCelular.requestFocus();
			return false;
		}

		return true;
	}

	private void showErrorMessage(String message) {
		lblErrorMessage.setText(message);
	}

	private void clearErrorMessage() {
		lblErrorMessage.setText("");
	}

	public boolean getDialogResult() {
		return dialogResult;
	}

	public Alumno getCreatedAlumno() {
		return createdAlumno;
	}
}