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
    //Estilos para campos no editables
    public static JTextField createReadOnlyField(String value) {
    JTextField textField = new JTextField(value);
    textField.setEditable(false);
    textField.setFocusable(false); // evita el cursor de texto
    textField.setFont(new Font("Arial", Font.PLAIN, 12));
    textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)));
    textField.setPreferredSize(new Dimension(320, 35));
    textField.setMinimumSize(new Dimension(320, 35));
    textField.setBackground(new Color(44, 62, 80));
    textField.setForeground(Color.WHITE); // texto blanco
    return textField;
}

}
