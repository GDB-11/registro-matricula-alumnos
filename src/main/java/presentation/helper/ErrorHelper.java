package presentation.helper;

import javax.swing.JLabel;

public class ErrorHelper {
    public static void showErrorMessage(JLabel lblErrorMessage, String message) {
		lblErrorMessage.setText(message);
	}

	public static void clearErrorMessage(JLabel lblErrorMessage) {
		lblErrorMessage.setText("");
	}
}
