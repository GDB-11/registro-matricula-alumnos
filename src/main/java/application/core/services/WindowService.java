package application.core.services;

import application.core.interfaces.IWindow;

import javax.swing.*;
import java.awt.*;

public class WindowService implements IWindow {
    private final ImageIcon icon;

    public WindowService() {
        icon = new ImageIcon("red-dev-simplified-logo.png");
    }

    public Image getWindowIcon() {
         return icon.getImage();
    }
}
