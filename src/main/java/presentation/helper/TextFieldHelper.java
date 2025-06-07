package presentation.helper;

import javax.swing.*;
import java.awt.*;

public class TextFieldHelper {
    public static void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 35));
    }

    public static void styleTextField(JTextField textField, String initialValue) {
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 35));

        if (initialValue != null) {
            textField.setText(initialValue);
        }
    }
}
