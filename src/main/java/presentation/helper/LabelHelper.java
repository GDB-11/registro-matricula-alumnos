package presentation.helper;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class LabelHelper {
	public static JLabel createLabel(String text, boolean required) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.BOLD, 12));
		if (required) {
			label.setText(text + " *");
		}
		return label;
	}

	public static JLabel createHintLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.ITALIC, 10));
		label.setForeground(Color.GRAY);
		return label;
	}

	//Metodo para mostrar en pantalla los datos con fuente simple y no se marca como obligatorio.
	public static JLabel createValueLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Arial", Font.PLAIN, 12));
		return label;
	}

}
