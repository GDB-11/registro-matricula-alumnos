package presentation.consultas;

import infrastructure.core.models.Alumno;
import infrastructure.core.models.Curso;
import infrastructure.core.models.Matricula;
import infrastructure.core.models.Retiro;
import presentation.helper.ButtonHelper;
import presentation.helper.GridBagHelper;
import presentation.helper.TextFieldHelper;

import javax.swing.*;
import java.awt.*;

public class RetirosInfoModal extends JDialog {

    public RetirosInfoModal(Frame parent, Retiro retiro, Matricula matricula, Alumno alumno, Curso curso) {
        super(parent, "Información del Retiro", true);
        // setSize(500, 420);
        initializeComponents(retiro, matricula, alumno, curso);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
    }

    private void initializeComponents(Retiro retiro, Matricula matricula, Alumno alumno, Curso curso) {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Datos del retiro
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 0, "N° Retiro:",
                TextFieldHelper.createReadOnlyField(String.valueOf(retiro.getNumRetiro())));
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 1, "Fecha Retiro:",
                TextFieldHelper.createReadOnlyField(retiro.getFecha()));
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 2, "Hora Retiro:",
                TextFieldHelper.createReadOnlyField(retiro.getHora()));

        // Datos de la matrícula asociada
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 3, "N° Matrícula:",
                TextFieldHelper.createReadOnlyField(String.valueOf(matricula.getNumMatricula())));

        // Datos del alumno
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 4, "Alumno:",
                TextFieldHelper.createReadOnlyField(alumno.getNombres() + " " + alumno.getApellidos()));
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 5, "DNI:",
                TextFieldHelper.createReadOnlyField(alumno.getDni()));

        // Datos del curso
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 6, "Curso:",
                TextFieldHelper.createReadOnlyField(curso.getAsignatura()));
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 7, "Código Curso:",
                TextFieldHelper.createReadOnlyField(String.valueOf(curso.getCodCurso())));

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        ButtonHelper.styleButton(btnCerrar, new Color(127, 140, 141));
        btnCerrar.addActionListener(e -> dispose());

        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(btnCerrar, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }
}
