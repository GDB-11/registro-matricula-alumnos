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

import application.core.interfaces.ICurso;
import global.Result;
import infrastructure.core.models.Curso;
import presentation.helper.ButtonHelper;
import presentation.helper.ComboBoxHelper;
import presentation.helper.ErrorHelper;
import presentation.helper.GridBagHelper;
import presentation.helper.TextFieldHelper;
import presentation.helper.ValidationHelper;

public class EditCursoModal extends JDialog {
    @Serial
	private static final long serialVersionUID = 1L;

	private final ICurso _cursoService;

	private JTextField txtAsignatura;
	private JComboBox cmbCiclo;
	private JTextField txtCredito;
	private JTextField txtHora;
	private JButton btnSave;
	private JButton btnCancel;
	private JLabel lblErrorMessage;

	private boolean dialogResult = false;
	private Curso selectedCurso = null;
    private Curso editedCurso = null;

    public EditCursoModal(Frame parent, ICurso cursoService, Curso selectedCurso) {
		super(parent, "Editar Curso", true);
		this._cursoService = cursoService;
        this.selectedCurso = selectedCurso;

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
		txtAsignatura = new JTextField(20);
		cmbCiclo = new JComboBox<>(new String[] {"Primero", "Segundo", "Tercero", "Cuarto", "Quinto", "Sexto"});
		txtCredito = new JTextField(3);
		txtHora = new JTextField(9);

		TextFieldHelper.styleTextField(txtAsignatura, selectedCurso.getAsignatura());
        ComboBoxHelper.styleComboBox(cmbCiclo, selectedCurso.getCiclo());
		TextFieldHelper.styleTextField(txtCredito, String.valueOf(selectedCurso.getCreditos()));
		TextFieldHelper.styleTextField(txtHora, String.valueOf(selectedCurso.getHoras()));

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

		JLabel titleLabel = new JLabel("Datos del Curso");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(titleLabel, gbc);

		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;

    	GridBagHelper.addLabelAndComponent(mainPanel, gbc, 1, "Asignatura:", txtAsignatura);
    	GridBagHelper.addLabelAndComponent(mainPanel, gbc, 2, "Ciclo:", cmbCiclo);
    	GridBagHelper.addLabelAndComponent(mainPanel, gbc, 3, "Créditos:", txtCredito);
    	GridBagHelper.addLabelAndComponent(mainPanel, gbc, 4, "Horas:", txtHora);

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

		txtAsignatura.addKeyListener(enterKeyListener);
		cmbCiclo.addKeyListener(enterKeyListener);
		txtCredito.addKeyListener(enterKeyListener);
		txtHora.addKeyListener(enterKeyListener);

		KeyAdapter clearErrorListener = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				ErrorHelper.clearErrorMessage(lblErrorMessage);
			}
		};

		txtAsignatura.addKeyListener(clearErrorListener);
		cmbCiclo.addKeyListener(clearErrorListener);
		txtCredito.addKeyListener(clearErrorListener);
		txtHora.addKeyListener(clearErrorListener);
	}

    private void setupValidation() {
		DocumentFilter asciiFilter = new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
				if (ValidationHelper.isAsciiSpanishNumbersAndSymbolsOnly(string)) {
					super.insertString(fb, offset, string, attr);
				}
			}

			@Override
			public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
				if (ValidationHelper.isAsciiSpanishNumbersAndSymbolsOnly(text)) {
					super.replace(fb, offset, length, text, attrs);
				}
			}
		};

		((AbstractDocument) txtAsignatura.getDocument()).setDocumentFilter(asciiFilter);
		((AbstractDocument) txtCredito.getDocument()).setDocumentFilter(new ValidationHelper.DigitsOnlyFilter(2));
		((AbstractDocument) txtHora.getDocument()).setDocumentFilter(new ValidationHelper.DigitsOnlyFilter(3));
	}

    private void onSave() {
		if (validateForm()) {
			try {
				String asignatura = txtAsignatura.getText().trim();
				int ciclo = cmbCiclo.getSelectedIndex();
				int creditos = Integer.parseInt(txtCredito.getText().trim());
				int horas = Integer.parseInt(txtHora.getText().trim());

				Result<Curso> result = _cursoService.editCurso(selectedCurso.getCodCurso(), asignatura, ciclo, creditos, horas);

				if (result.isSuccess()) {
					editedCurso = result.getValue();
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

		// Asignatura
		if (txtAsignatura.getText().trim().isEmpty()) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "El campo Asignatura es obligatorio");
			txtAsignatura.requestFocus();
			return false;
		}

		if (txtAsignatura.getText().trim().length() < 3) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "La Asignatura debe tener al menos 3 caracteres");
			txtAsignatura.requestFocus();
			return false;
		}

		// Credito
		String creditoText = txtCredito.getText().trim();
		if (creditoText.isEmpty()) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "El campo Crédito es obligatorio");
			txtCredito.requestFocus();
			return false;
		}

		try {
			int creditos = Integer.parseInt(creditoText);
			if (creditos < 0 || creditos > 8) {
				ErrorHelper.showErrorMessage(lblErrorMessage, "Los créditos debe estar entre 0 y 8");
				txtCredito.requestFocus();
				return false;
			}
		} catch (NumberFormatException e) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "Créditos debe tener un número válido");
			txtCredito.requestFocus();
			return false;
		}

		// Horas
		String horaText = txtHora.getText().trim();
		if (horaText.isEmpty()) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "El campo Horas es obligatorio");
			txtHora.requestFocus();
			return false;
		}

		try {
			int horas = Integer.parseInt(horaText);
			if (horas < 1 || horas > 99) {
				ErrorHelper.showErrorMessage(lblErrorMessage, "Los créditos debe estar entre 1 y 99");
				txtHora.requestFocus();
				return false;
			}
		} catch (NumberFormatException e) {
			ErrorHelper.showErrorMessage(lblErrorMessage, "Horas debe tener un número válido");
			txtHora.requestFocus();
			return false;
		}

		return true;
	}

	public boolean getDialogResult() {
		return dialogResult;
	}

	public Curso getEditedCurso() {
		return editedCurso;
	}
}
