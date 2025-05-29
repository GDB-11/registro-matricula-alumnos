package main;

import presentation.MainWindow;

public class WindowFactory {
    private final ServiceContainer container;

    public WindowFactory(ServiceContainer container) {
        this.container = container;
    }

    public MainWindow createMainWindow() {
        return container.createWindow(MainWindow.class);
    }
}