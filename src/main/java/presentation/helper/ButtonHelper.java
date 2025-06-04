package presentation.helper;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonHelper {

    public static void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(140, 35));
    }

    public static class ButtonRenderer extends JButton implements TableCellRenderer {
        private final Color buttonColor;

        public ButtonRenderer(String text, Color backgroundColor) {
            super(text);
            this.buttonColor = backgroundColor;
            setOpaque(true);
            setFont(new Font("Arial", Font.BOLD, 10));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorder(BorderFactory.createRaisedBevelBorder());
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setBackground(buttonColor);
            return this;
        }
    }

    public static class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final ButtonActionListener actionListener;
        private int currentRow;

        @FunctionalInterface
        public interface ButtonActionListener {
            void actionPerformed(int rowIndex);
        }

        public ButtonEditor(String text, ButtonActionListener listener) {
            super(new JCheckBox());
            this.actionListener = listener;

            button = new JButton(text);
            button.setOpaque(true);
            button.setFont(new Font("Arial", Font.BOLD, 10));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createRaisedBevelBorder());

            // Set button color based on text
            if (text.equals("Editar")) {
                button.setBackground(new Color(241, 196, 15));
            } else if (text.equals("Eliminar")) {
                button.setBackground(new Color(231, 76, 60));
            }

            button.addActionListener(e -> {
                fireEditingStopped();
                actionListener.actionPerformed(currentRow);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        @Override
        public boolean stopCellEditing() {
            return super.stopCellEditing();
        }
    }
}
