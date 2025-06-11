package presentation.helper;

import java.awt.*;
import java.util.function.Function;
import javax.swing.*;

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

    public static class GenericComboBoxRenderer<T> extends DefaultListCellRenderer {
        private final Function<T, String> displayFunction;

        public GenericComboBoxRenderer(Function<T, String> displayFunction) {
            this.displayFunction = displayFunction;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value != null) {
                setText(displayFunction.apply((T) value));
            }

            return this;
        }
    }
}
