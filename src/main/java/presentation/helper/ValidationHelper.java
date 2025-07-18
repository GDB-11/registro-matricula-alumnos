package presentation.helper;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class ValidationHelper {
    public static boolean isAsciiAndSpanishOnly(String text) {
        return text.chars().allMatch(c -> (c < 128 && (Character.isLetter(c) || Character.isWhitespace(c))) ||
                isSpanishAccentedChar(c)
        );
    }

    private static boolean isSpanishAccentedChar(int c) {
        return "áéíóúñÁÉÍÓÚÑ".indexOf(c) != -1;
    }

    public static boolean isAsciiSpanishNumbersAndSymbolsOnly(String text) {
        return text.chars().allMatch(c ->
                (c < 128 && (Character.isLetter(c) || Character.isWhitespace(c) || Character.isDigit(c))) ||
                        isSpanishAccentedChar(c) ||
                        isAllowedSymbol(c)
        );
    }

    private static boolean isAllowedSymbol(int c) {
        return "#$%+-*/.".indexOf(c) != -1;
    }

    public static class DigitsOnlyFilter extends DocumentFilter {
        private final int maxLength;

        public DigitsOnlyFilter(int maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && string.matches("\\d*") &&
                    (fb.getDocument().getLength() + string.length()) <= maxLength) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text != null && text.matches("\\d*") &&
                    (fb.getDocument().getLength() - length + text.length()) <= maxLength) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}
