package presentation.mantenimiento;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serial;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
import presentation.helper.LabelHelper;
import presentation.helper.TextFieldHelper;
import presentation.helper.ValidationHelper;

public class EditAlumnoModal extends JDialog {
    @Serial
	private static final long serialVersionUID = 1L;

	private final IAlumno _alumnoService;
	private JTextField txtNombres;
	private JTextField txtApellidos;
	private JTextField txtEdad;
	private JTextField txtCelular;
    private JComboBox cmbEstado;
	private JButton btnSave;
	private JButton btnCancel;
	private JLabel lblErrorMessage;

    private boolean dialogResult = false;
	private Alumno selectedAlumno = null;

    public EditAlumnoModal(Frame parent, IAlumno alumnoService, Alumno selectedAlumno) {
		super(parent, "Agregar Nuevo Alumno", true);
		this._alumnoService = alumnoService;
        this.selectedAlumno = selectedAlumno;

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
		txtEdad = new JTextField(3);
		txtCelular = new JTextField(9);
        cmbEstado = new JComboBox<>(new String[]{"Registrado", "Matriculado", "Retirado"});

		TextFieldHelper.styleTextField(txtNombres, selectedAlumno.getNombres());
		TextFieldHelper.styleTextField(txtApellidos, selectedAlumno.getApellidos());
		TextFieldHelper.styleTextField(txtEdad, String.valueOf(selectedAlumno.getEdad()));
		TextFieldHelper.styleTextField(txtCelular, String.valueOf(selectedAlumno.getCelular()));

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
        
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(titleLabel, gbc);

		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0; gbc.gridy = 1;
		mainPanel.add(LabelHelper.createLabel("Nombres:", true), gbc);
		gbc.gridx = 1;
		mainPanel.add(txtNombres, gbc);

		gbc.gridx = 0; gbc.gridy = 2;
		mainPanel.add(LabelHelper.createLabel("Apellidos:", true), gbc);
		gbc.gridx = 1;
		mainPanel.add(txtApellidos, gbc);

		gbc.gridx = 0; gbc.gridy = 3;
		mainPanel.add(LabelHelper.createLabel("Edad:", true), gbc);
		gbc.gridx = 1;
		JPanel edadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		edadPanel.add(txtEdad);
		edadPanel.add(LabelHelper.createHintLabel(" (años)"));
		mainPanel.add(edadPanel, gbc);

		gbc.gridx = 0; gbc.gridy = 4;
		mainPanel.add(LabelHelper.createLabel("Celular:", true), gbc);
		gbc.gridx = 1;
		JPanel celularPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		celularPanel.add(txtCelular);
		celularPanel.add(LabelHelper.createHintLabel(" (9 dígitos)"));
		mainPanel.add(celularPanel, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
		mainPanel.add(LabelHelper.createLabel("Estado:", true), gbc);
		gbc.gridx = 1;
		JPanel estadoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		estadoPanel.add(cmbEstado);
		mainPanel.add(estadoPanel, gbc);

		JPanel errorContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
		errorContainer.setPreferredSize(new Dimension(0, 30));
		errorContainer.setBorder(new EmptyBorder(5, 10, 5, 10));
		errorContainer.add(lblErrorMessage);

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
		txtEdad.addKeyListener(enterKeyListener);
		txtCelular.addKeyListener(enterKeyListener);
        cmbEstado.addKeyListener(enterKeyListener);

		KeyAdapter clearErrorListener = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				ErrorHelper.clearErrorMessage(lblErrorMessage);
			}
		};

		txtNombres.addKeyListener(clearErrorListener);
		txtApellidos.addKeyListener(clearErrorListener);
		txtEdad.addKeyListener(clearErrorListener);
		txtCelular.addKeyListener(clearErrorListener);
        cmbEstado.addKeyListener(clearErrorListener);
	}

    private void setupValidation() {
		DocumentFilter asciiFilter = new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
				if (ValidationHelper.isAsciiOnly(string)) {
					super.insertString(fb, offset, string, attr);
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
				if (ValidationHelper.isAsciiOnly(text)) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		};

		((AbstractDocument) txtNombres.getDocument()).setDocumentFilter(asciiFilter);
		((AbstractDocument) txtApellidos.getDocument()).setDocumentFilter(asciiFilter);

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
				int edad = Integer.parseInt(txtEdad.getText().trim());
				int celular = Integer.parseInt(txtCelular.getText().trim());
                int estado = cmbEstado.getSelectedIndex();

				Result<Void> result = _alumnoService.editAlumno(selectedAlumno.getCodAlumno(), nombres, apellidos, edad, celular, estado);

				if (result.isSuccess()) {
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
}
