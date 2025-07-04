package presentation.consultas;

import infrastructure.core.models.Alumno;
import infrastructure.core.models.Curso;
import infrastructure.core.models.Matricula;
import presentation.helper.ButtonHelper;
import presentation.helper.GridBagHelper;
import presentation.helper.TextFieldHelper;
import presentation.helper.WindowHelper;

import javax.swing.*;
import java.awt.*;

public class MatriculaInfoModal extends JDialog {

    public MatriculaInfoModal(Frame parent, Matricula matricula, Alumno alumno, Curso curso) {
        super(parent, "Información de Matrícula", true);
        initializeComponents(matricula, alumno, curso);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        // WindowHelper.centerWindow(this);
    }

    private void initializeComponents(Matricula matricula, Alumno alumno, Curso curso) {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Datos de matrícula
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 0, "Número:",
                TextFieldHelper.createReadOnlyField(String.valueOf(matricula.getNumMatricula())));
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 1, "Fecha:",
                TextFieldHelper.createReadOnlyField(matricula.getFecha()));
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 2, "Hora:",
                TextFieldHelper.createReadOnlyField(matricula.getHora()));

        // Datos del alumno
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 3, "Alumno:",
                TextFieldHelper.createReadOnlyField(alumno.getNombres() + " " + alumno.getApellidos()));
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 4, "DNI:",
                TextFieldHelper.createReadOnlyField(alumno.getDni()));

        // Datos del curso
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 5, "Curso:",
                TextFieldHelper.createReadOnlyField(curso.getAsignatura()));
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 6, "Código Curso:",
                TextFieldHelper.createReadOnlyField(String.valueOf(curso.getCodCurso())));

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        ButtonHelper.styleButton(btnCerrar, new Color(127, 140, 141));
        btnCerrar.addActionListener(e -> dispose());

        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(btnCerrar, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }
}
