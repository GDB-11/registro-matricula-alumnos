package presentation.consultas;

import infrastructure.core.models.Alumno;
import infrastructure.core.models.Curso;
import presentation.helper.ButtonHelper;
import presentation.helper.GridBagHelper;
import presentation.helper.LabelHelper;
import presentation.helper.TextFieldHelper;

import javax.swing.*;
import java.awt.*;

public class AlumnoInfoModal extends JDialog {
  private final Alumno alumno;
  private final Curso curso;

  public AlumnoInfoModal(Frame parent, Alumno alumno, Curso curso) {
    super(parent, "Datos del alumno y del curso", true);
    this.alumno = alumno;
    this.curso = curso;

    initializeComponents();
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setResizable(false);
    setPreferredSize(new Dimension(500, curso == null ? 600 : 800));
    pack();
    setLocationRelativeTo(parent);

  }

  private void initializeComponents() {
    JPanel contentPanel = new JPanel(new GridBagLayout());
    contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(8, 8, 8, 8);
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;

    // Datos

    GridBagHelper.addLabelAndComponent(contentPanel, gbc, 0, "Código:", TextFieldHelper.createReadOnlyField(String.valueOf(alumno.getCodAlumno())));
    GridBagHelper.addLabelAndComponent(contentPanel, gbc, 1, "Nombres", TextFieldHelper.createReadOnlyField(alumno.getNombres()));
    // GridBagHelper.addLabelAndComponent(contentPanel, gbc, 1, "Nombres:",
    //     LabelHelper.createValueLabel(alumno.getNombres()));
    GridBagHelper.addLabelAndComponent(contentPanel, gbc, 2, "Apellidos:", TextFieldHelper.createReadOnlyField(alumno.getApellidos()));
    GridBagHelper.addLabelAndComponent(contentPanel, gbc, 3, "DNI:", TextFieldHelper.createReadOnlyField(alumno.getDni()));
    GridBagHelper.addLabelAndComponent(contentPanel, gbc, 4, "Edad:",
        TextFieldHelper.createReadOnlyField(String.valueOf(alumno.getEdad())));
    GridBagHelper.addLabelAndComponent(contentPanel, gbc, 5, "Celular:",
        TextFieldHelper.createReadOnlyField(String.valueOf(alumno.getCelular())));
    GridBagHelper.addLabelAndComponent(contentPanel, gbc, 6, "Estado:",
        TextFieldHelper.createReadOnlyField(getEstadoTexto(alumno.getEstado())));

    JButton btnCerrar = new JButton("Cerrar");
    ButtonHelper.styleButton(btnCerrar, new Color(127,140,141));
    btnCerrar.addActionListener(e -> dispose());
  

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
    buttonPanel.add(btnCerrar);

    add(contentPanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    if (curso != null) {
      GridBagHelper.addLabelAndComponent(contentPanel, gbc, 7, "Asignatura:",
          TextFieldHelper.createReadOnlyField(curso.getAsignatura()));
      GridBagHelper.addLabelAndComponent(contentPanel, gbc, 8, "Código Curso:",
          TextFieldHelper.createReadOnlyField(String.valueOf(curso.getCodCurso())));
      GridBagHelper.addLabelAndComponent(contentPanel, gbc, 9, "Ciclo:",
          TextFieldHelper.createReadOnlyField(String.valueOf(curso.getCiclo())));
      GridBagHelper.addLabelAndComponent(contentPanel, gbc, 10, "Créditos:",
          TextFieldHelper.createReadOnlyField(String.valueOf(curso.getCreditos())));
      GridBagHelper.addLabelAndComponent(contentPanel, gbc, 11, "Horas:",
          TextFieldHelper.createReadOnlyField(String.valueOf(curso.getHoras())));
    }
  }

  private String getEstadoTexto(int estado) {
    return switch (estado) {
      case 0 -> "Registrado";
      case 1 -> "Matriculado";
      case 2 -> "Retirado";
      default -> "Desconocido";
    };
  }

}
