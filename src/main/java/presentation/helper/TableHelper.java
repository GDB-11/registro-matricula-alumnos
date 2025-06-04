package presentation.helper;

import javax.swing.*;

public class TableHelper {
    public static ListSelectionModel getNoSelectionModel() {
        return new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {

            }
        };
    }
}
