package presentation.consultas;

import javax.swing.*;
import java.awt.*;

import application.core.interfaces.IConsulta;
import global.Result;
import infrastructure.core.models.Alumno;
import infrastructure.core.models.Curso;
import infrastructure.core.models.Matricula;
import infrastructure.core.models.Retiro;
import presentation.helper.ButtonHelper;
import presentation.helper.ErrorHelper;
import presentation.helper.GridBagHelper;
import presentation.helper.TextFieldHelper;
import presentation.helper.WindowHelper;

public class MatriculasRetirosWindow extends JFrame {
  private final IConsulta consultaService;

  // MAtricula
  private JTextField txtNumMatricula;
  private JButton btnBuscarMatricula;

  // Retiro
  private JTextField txtNumRetiro;
  private JButton btnBuscarRetiro;

  // mensaje de error
  private JLabel lblErrorMessage;

  public MatriculasRetirosWindow(IConsulta consultaService) {
    this.consultaService = consultaService;

    // metodos
    initializeComponents();
    setupLayout();
    setupEvents();
  }

  private void initializeComponents() {
    setResizable(false);
    setTitle("Consultas de Matriculas y Retiros");
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(600, 450);
    WindowHelper.centerWindow(this);

    // Matricula
    txtNumMatricula = new JTextField(10);
    TextFieldHelper.styleTextField(txtNumMatricula);
    btnBuscarMatricula = new JButton("Buscar Matricula");
    ButtonHelper.styleButton(btnBuscarMatricula, new Color(46, 204, 113));
    btnBuscarMatricula.setForeground(Color.WHITE);

    // Retiro
    txtNumRetiro = new JTextField(10);
    TextFieldHelper.styleTextField(txtNumRetiro);
    btnBuscarRetiro = new JButton("Buscar Retiro");
    ButtonHelper.styleButton(btnBuscarRetiro, new Color(46, 204, 113));
    btnBuscarRetiro.setForeground(Color.WHITE);

    // Mensaje de error
    lblErrorMessage = new JLabel();
    lblErrorMessage.setHorizontalAlignment(SwingConstants.CENTER);
    lblErrorMessage.setForeground(Color.RED);

    setTitle("Consulta de Matrículas y Retiros");
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    setSize(700, 400);
    setResizable(false);
    setLocationRelativeTo(null);
  }

  private void setupLayout() {
    setLayout(new BorderLayout());

    // Panel para busqueda de matricula
    JPanel matriculaPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbcMatricula = new GridBagConstraints();
    gbcMatricula.insets = new Insets(8, 8, 8, 8);
    gbcMatricula.anchor = GridBagConstraints.WEST;

    gbcMatricula.fill = GridBagConstraints.HORIZONTAL;
    gbcMatricula.weightx = 1.0; // Esto permite que el campo se expanda con el panel

    GridBagHelper.addLabelAndComponent(matriculaPanel, gbcMatricula, 0, "N° Matrícula:", txtNumMatricula);

    gbcMatricula.gridx = 2;
    gbcMatricula.gridy = 0;
    // gbcMatricula.gridwidth = 1;
    matriculaPanel.add(btnBuscarMatricula, gbcMatricula);

    // Panel para busqueda de Retiro
    JPanel retiroPanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbcRetiro = new GridBagConstraints();
    gbcRetiro.insets = new Insets(8, 8, 8, 8);
    gbcRetiro.anchor = GridBagConstraints.WEST;

    gbcRetiro.fill = GridBagConstraints.HORIZONTAL;
    gbcRetiro.weightx = 1.0; // Esto permite que el campo se expanda con el panel

    GridBagHelper.addLabelAndComponent(retiroPanel, gbcRetiro, 0, "N° Retiro:", txtNumRetiro);
    gbcRetiro.gridx = 2;
    gbcRetiro.gridy = 0;
    retiroPanel.add(btnBuscarRetiro, gbcRetiro);

    // Panel de errores
    JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    errorPanel.add(lblErrorMessage);

    // Panel principal
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
    mainPanel.add(matriculaPanel);
    mainPanel.add(Box.createVerticalStrut(10));
    mainPanel.add(retiroPanel);
    mainPanel.add(Box.createVerticalStrut(10));
    mainPanel.add(errorPanel);

    setContentPane(mainPanel);
  }

  private void setupEvents() {
    // Agregar metodos
    btnBuscarMatricula.addActionListener(e -> onBuscarMatricula());
    btnBuscarRetiro.addActionListener(e -> onBuscarRetiro());
  }

  private void onBuscarMatricula() {
    ErrorHelper.clearErrorMessage(lblErrorMessage);
    String textoCodigo = txtNumMatricula.getText().trim();

    if (textoCodigo.isEmpty()) {
      ErrorHelper.showErrorMessage(lblErrorMessage, "Debe ingresar un número de matrícula");
      return;
    }

    try {
      int numMatricula = Integer.parseInt(textoCodigo);

      Result<Matricula> resultMatricula = consultaService.getMatriculaByCodigo(numMatricula);
      if (resultMatricula.isError()) {
        ErrorHelper.showErrorMessage(lblErrorMessage, resultMatricula.getError());
        return;
      }

      Matricula matricula = resultMatricula.getValue();

      Result<Alumno> resultAlumno = consultaService.consultarAlumnoPorCodigo(matricula.getCodAlumno());
      if (resultAlumno.isError()) {
        ErrorHelper.showErrorMessage(lblErrorMessage, resultAlumno.getError());
        return;
      }

      Result<Curso> resultCurso = consultaService.consultarCursoPorCodigo(matricula.getCodCurso());
      if (resultCurso.isError()) {
        ErrorHelper.showErrorMessage(lblErrorMessage, resultCurso.getError());
        return;
      }

      MatriculaInfoModal modal = new MatriculaInfoModal(this, matricula, resultAlumno.getValue(),
          resultCurso.getValue());
      modal.setVisible(true);

    } catch (NumberFormatException e) {
      ErrorHelper.showErrorMessage(lblErrorMessage, "El código debe ser un número válido");
    }
  }

  private void onBuscarRetiro() {
    ErrorHelper.clearErrorMessage(lblErrorMessage);
    String textoCodigo = txtNumRetiro.getText().trim();

    if (textoCodigo.isEmpty()) {
      ErrorHelper.showErrorMessage(lblErrorMessage, "Debe ingresar un número de retiro");
      return;
    }

    try {
      int numRetiro = Integer.parseInt(textoCodigo);

      Result<Retiro> resultRetiro = consultaService.getRetiroByCodigo(numRetiro);
      if (resultRetiro.isError()) {
        ErrorHelper.showErrorMessage(lblErrorMessage, resultRetiro.getError());
        return;
      }

      Retiro retiro = resultRetiro.getValue();

      Result<Matricula> resultMatricula = consultaService.getMatriculaByCodigo(retiro.getNumMatricula());
      if (resultMatricula.isError()) {
        ErrorHelper.showErrorMessage(lblErrorMessage, resultMatricula.getError());
        return;
      }

      Matricula matricula = resultMatricula.getValue();

      Result<Alumno> resultAlumno = consultaService.consultarAlumnoPorCodigo(matricula.getCodAlumno());
      if (resultAlumno.isError()) {
        ErrorHelper.showErrorMessage(lblErrorMessage, resultAlumno.getError());
        return;
      }

      Result<Curso> resultCurso = consultaService.consultarCursoPorCodigo(matricula.getCodCurso());
      if (resultCurso.isError()) {
        ErrorHelper.showErrorMessage(lblErrorMessage, resultCurso.getError());
        return;
      }

      RetirosInfoModal modal = new RetirosInfoModal(this, retiro, matricula, resultAlumno.getValue(),
          resultCurso.getValue());
      modal.setVisible(true);

    } catch (NumberFormatException e) {
      ErrorHelper.showErrorMessage(lblErrorMessage, "El código debe ser un número válido");
    }
  }
}
