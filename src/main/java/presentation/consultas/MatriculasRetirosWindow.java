package presentation.consultas;

import javax.swing.*;
import java.awt.*;

import application.core.interfaces.IConsulta;
import global.Result;
import presentation.helper.WindowHelper;

public class MatriculasRetirosWindow extends JFrame {
  private final IConsulta consultaService;

  // MAtricula
  private JTextField txtNumMatricula;
  private JButton btnBuscarMatricula;
  private JTextArea txtResultadoMatricula;

  // Retiro
  private JTextField txtNumRetiro;
  private JButton btnBuscarRetiro;
  private JTextArea txtResultadoRetiro;

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
    btnBuscarMatricula = new JButton("Buscar Matricula");
    txtResultadoMatricula = new JTextArea(7, 40);
    txtResultadoMatricula.setEditable(false);
    txtResultadoMatricula.setLineWrap(true);
    txtResultadoMatricula.setWrapStyleWord(true);

    // Retiro
    txtNumRetiro = new JTextField(10);
    btnBuscarRetiro = new JButton("Buscar REtiro");
    txtResultadoRetiro = new JTextArea(7, 40);
    txtResultadoRetiro.setEditable(false);
    txtResultadoRetiro.setLineWrap(true);
    txtResultadoRetiro.setWrapStyleWord(true);
  }

  private void setupLayout() {
    JTabbedPane tabbedPane = new JTabbedPane();

    // Agregar metodos
    tabbedPane.addTab("Matricula", createMatriculaPanel());
    tabbedPane.addTab("Retiro", createRetiroPanel());

    setContentPane(tabbedPane);
  }

  private JPanel createMatriculaPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    searchPanel.setBorder(BorderFactory.createTitledBorder("Buscar Matrícula por Número"));
    searchPanel.add(new JLabel("N° Matrícula:"));
    searchPanel.add(txtNumMatricula);
    searchPanel.add(btnBuscarMatricula);

    JScrollPane resultScroll = new JScrollPane(txtResultadoMatricula);
    resultScroll.setBorder(BorderFactory.createTitledBorder("Resultado de la Búsqueda"));

    panel.add(searchPanel, BorderLayout.NORTH);
    panel.add(resultScroll, BorderLayout.CENTER);

    return panel;
  }

  private JPanel createRetiroPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    searchPanel.setBorder(BorderFactory.createTitledBorder("Buscar Retiro por Número"));
    searchPanel.add(new JLabel("N° Retiro:"));
    searchPanel.add(txtNumRetiro);
    searchPanel.add(btnBuscarRetiro);

    JScrollPane resultScroll = new JScrollPane(txtResultadoRetiro);
    resultScroll.setBorder(BorderFactory.createTitledBorder("Resultado de la Búsqueda"));

    panel.add(searchPanel, BorderLayout.NORTH);
    panel.add(resultScroll, BorderLayout.CENTER);

    return panel;
  }

  private void setupEvents() {
    // Agregar metodos
    btnBuscarMatricula.addActionListener(e -> buscarMatricula());
    btnBuscarRetiro.addActionListener(e -> buscarRetiro());
  }

  private void buscarMatricula() {
    try {
      int numMatricula = Integer.parseInt(txtNumMatricula.getText().trim());

      Result<String> result = consultaService.consultarDatosDeMatricula(numMatricula);
      txtResultadoMatricula.setText(result.isSuccess()
          ? result.getValue()
          : "⚠ " + result.getError());

    } catch (NumberFormatException ex) {
      txtResultadoMatricula.setText("⚠ El número debe ser un valor entero.");
    }
  }

  private void buscarRetiro() {
    try {
      int numRetiro = Integer.parseInt(txtNumRetiro.getText().trim());

      Result<String> result = consultaService.consultarDatosDeRetiro(numRetiro);
      txtResultadoRetiro.setText(result.isSuccess()
          ? result.getValue()
          : "⚠ " + result.getError());

    } catch (NumberFormatException ex) {
      txtResultadoRetiro.setText("⚠ El número debe ser un valor entero.");
    }
  }
}
