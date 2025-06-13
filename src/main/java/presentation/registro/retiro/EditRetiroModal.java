package presentation.registro.retiro;

import application.core.interfaces.ICurso;
import application.core.interfaces.IMatricula;
import application.core.interfaces.IRetiro;
import global.Result;
import infrastructure.core.models.Curso;
import infrastructure.core.models.Matricula;
import infrastructure.core.models.Retiro;
import presentation.helper.ButtonHelper;
import presentation.helper.ComboBoxHelper;
import presentation.helper.ErrorHelper;
import presentation.helper.GridBagHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.Serial;
import java.util.List;

public class EditRetiroModal extends JDialog {
    @Serial
    private static final long serialVersionUID = 1L;

    private final IRetiro _retiroService;
    private final ICurso _cursoService;
    private final IMatricula _matriculaService;

    private JComboBox<Curso> cmbCurso;
    private JButton btnSave;
    private JButton btnCancel;
    private JLabel lblErrorMessage;

    private boolean dialogResult = false;
    private Retiro selectedRetiro = null;

    public EditRetiroModal(Frame parent, IRetiro retiroService, ICurso cursoService, IMatricula matriculaService, Retiro selectedRetiro) {
        super(parent, "Editar Retiro", true);
        this._retiroService = retiroService;
        this._cursoService = cursoService;
        this._matriculaService = matriculaService;
        this.selectedRetiro = selectedRetiro;

        initializeComponents();
        setupLayout();
        setupEventHandlers();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        Result<List<Curso>> cursos = _cursoService.getAllCursos();

        if (cursos.hasException()) {
            ErrorHelper.showErrorMessage(lblErrorMessage, "Error inesperado en cursos: " + cursos.getException().getMessage());
            return;
        }

        Result<Curso> cursoMatriculado = _cursoService.getCursoFromMatricula(selectedRetiro.getNumMatricula());

        if (cursoMatriculado.isError()) {
            ErrorHelper.showErrorMessage(lblErrorMessage, "Error inesperado buscando el curso: " + cursoMatriculado.getError());
            return;
        }

        cmbCurso = new JComboBox<>();

        int actualIndex = 0;
        for (int i = 0; i < cursos.getValue().size(); i++) {
            Curso curso = cursos.getValue().get(i);

            if (curso.getCodCurso() == cursoMatriculado.getValue().getCodCurso()) {
                actualIndex = i;
            }

            cmbCurso.addItem(curso);
        }

        cmbCurso.setRenderer(new ComboBoxHelper.GenericComboBoxRenderer<Curso>(
                curso -> curso.getCodCurso() + " - " + curso.getAsignatura()
        ));

        ComboBoxHelper.styleComboBox(cmbCurso, actualIndex);

        ComboBoxHelper.styleComboBox(cmbCurso);

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

        JLabel titleLabel = new JLabel("Datos de la Matrícula");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        GridBagHelper.addLabelAndComponent(mainPanel, gbc, 2, "Curso:", cmbCurso);

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

        cmbCurso.addKeyListener(enterKeyListener);

        KeyAdapter clearErrorListener = new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                ErrorHelper.clearErrorMessage(lblErrorMessage);
            }
        };

        cmbCurso.addKeyListener(clearErrorListener);
    }

    private void onSave() {
        try {
            Curso curso = (Curso) cmbCurso.getSelectedItem();

            Result<Matricula> result = _matriculaService.editMatricula(selectedRetiro.getNumMatricula(), curso.getCodCurso());;

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

    private void onCancel() {
        dialogResult = false;
        dispose();
    }

    public boolean getDialogResult() {
        return dialogResult;
    }

    public Retiro getEditedRetiro() {
        return selectedRetiro;
    }
}
