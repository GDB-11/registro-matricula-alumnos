package presentation.consultas;

import infrastructure.core.models.Curso;
import presentation.helper.ButtonHelper;
import presentation.helper.GridBagHelper;
import presentation.helper.LabelHelper;
import presentation.helper.TextFieldHelper;

import javax.swing.*;
import java.awt.*;

public class CursoInfoModal extends JDialog {
    private final Curso curso;

    public CursoInfoModal(Frame parent, Curso curso) {
        super(parent, "Información del Curso", true);
        this.curso = curso;

        initializeComponents();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 0, "Código:", TextFieldHelper.createReadOnlyField(String.valueOf(curso.getCodCurso())));
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 1, "Asignatura:", TextFieldHelper.createReadOnlyField(curso.getAsignatura()));
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 2, "Ciclo:", TextFieldHelper.createReadOnlyField(String.valueOf(curso.getCiclo())));
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 3, "Créditos:", TextFieldHelper.createReadOnlyField(String.valueOf(curso.getCreditos())));
        GridBagHelper.addLabelAndComponent(contentPanel, gbc, 4, "Horas:", TextFieldHelper.createReadOnlyField(String.valueOf(curso.getHoras())));

        JButton btnCerrar = new JButton("Cerrar");
        ButtonHelper.styleButton(btnCerrar, new Color(127, 140, 141));
        btnCerrar.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnCerrar);

        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
