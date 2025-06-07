package presentation.helper;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class GridBagHelper {
    public static void addLabelAndComponent(JPanel panel, GridBagConstraints gbc,
            int row, String labelText, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(LabelHelper.createLabel(labelText, true), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    public static void addLabelAndComponentWithHint(JPanel panel, GridBagConstraints gbc,
            int row, String labelText,
            JComponent component, String hint) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(LabelHelper.createLabel(labelText, true), gbc);
        gbc.gridx = 1;

        JPanel hintPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        hintPanel.add(component);
        hintPanel.add(LabelHelper.createHintLabel(hint));
        panel.add(hintPanel, gbc);
    }
}
