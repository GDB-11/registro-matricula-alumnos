package presentation.helper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;

public class ComboBoxHelper {
    
    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(3, 8, 3, 8)));
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 35));
    }
    
    public static void styleComboBox(JComboBox<?> comboBox, int selectedIndex) {
        comboBox.setFont(new Font("Arial", Font.PLAIN, 12));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(3, 8, 3, 8)));
        comboBox.setPreferredSize(new Dimension(comboBox.getPreferredSize().width, 35));
        
        if (selectedIndex >= 0 && selectedIndex < comboBox.getItemCount()) {
            comboBox.setSelectedIndex(selectedIndex);
        }
    }
}
