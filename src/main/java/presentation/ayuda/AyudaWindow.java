package presentation.ayuda;

import javax.swing.*;
import java.awt.*;

public class AyudaWindow extends JFrame {

    public AyudaWindow() {
        initializeComponents();
        setupWindow();
    }

    private void initializeComponents() {
        setTitle("Acerca de - Red Dev");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Panel superior con logo/título
        JPanel headerPanel = createHeaderPanel();

        // Panel central con información
        JPanel infoPanel = createInfoPanel();

        // Panel inferior con botón cerrar
        JPanel footerPanel = createFooterPanel();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Título principal
        JLabel titleLabel = new JLabel("Red Dev");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(220, 20, 60)); // Color rojo elegante
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo
        JLabel subtitleLabel = new JLabel("Academia Peruana de Programación");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.DARK_GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Versión
        JLabel versionLabel = new JLabel("Versión 1.0");
        versionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        versionLabel.setForeground(Color.GRAY);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(versionLabel);

        return headerPanel;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Información del Sistema",
                0, 0, new Font("Arial", Font.BOLD, 12)
        ));

        // Descripción
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText("Red Dev es una academia peruana de programación que revoluciona la educación " +
                "tecnológica con una metodología única y probada. Nuestro programa formativo se " +
                "divide en dos etapas fundamentales:\n\n" +
                "PRIMERA MITAD - Fundamentos Sólidos: Iniciamos con programación de bajo nivel, " +
                "enseñando conceptos fundamentales como manejo de memoria, estructuras de datos, " +
                "algoritmos, lenguajes como C/C++, ensamblador y arquitectura de computadoras. " +
                "Esta base sólida garantiza que nuestros estudiantes comprendan realmente cómo " +
                "funciona el software a nivel del sistema.\n\n" +
                "SEGUNDA MITAD - Especialización Moderna: Progresamos hacia programación de alto " +
                "nivel, frameworks modernos, desarrollo web, aplicaciones móviles, bases de datos " +
                "y tecnologías de vanguardia. Los estudiantes aplican sus conocimientos fundamentales " +
                "en proyectos reales usando tecnologías como React, Spring, Django y más.\n\n" +
                "Nuestro objetivo es formar desarrolladores excepcionales que no solo sepan usar " +
                "herramientas, sino que entiendan profundamente la tecnología, preparándolos para " +
                "liderar el mercado tecnológico peruano e internacional.");
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 11));
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setEditable(false);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel de desarrolladores
        JPanel developersPanel = createDevelopersPanel();

        // Copyright
        JPanel copyrightPanel = createCopyrightPanel();

        infoPanel.add(descriptionArea);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(developersPanel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(copyrightPanel);

        return infoPanel;
    }

    private JPanel createDevelopersPanel() {
        JPanel developersPanel = new JPanel();
        developersPanel.setLayout(new BoxLayout(developersPanel, BoxLayout.Y_AXIS));
        developersPanel.setBackground(Color.WHITE);
        developersPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                "Equipo de Desarrollo",
                0, 0, new Font("Arial", Font.BOLD, 11)
        ));

        // Desarrollador 1
        JPanel dev1Panel = createDeveloperInfo(
                "DÍAZ BADOINO, GIANFRANCO",
                "Desarrollador",
                "I202413435"
        );

        // Desarrollador 2
        JPanel dev2Panel = createDeveloperInfo(
                "COLONA MERINO DANIEL CRYSTOPHER",
                "Desarrollador",
                "I202410503"
        );

        developersPanel.add(dev1Panel);
        developersPanel.add(Box.createVerticalStrut(10));
        developersPanel.add(dev2Panel);

        return developersPanel;
    }

    private JPanel createDeveloperInfo(String name, String role, String code) {
        JPanel devPanel = new JPanel();
        devPanel.setLayout(new BoxLayout(devPanel, BoxLayout.Y_AXIS));
        devPanel.setBackground(new Color(248, 248, 248));
        devPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        roleLabel.setForeground(Color.DARK_GRAY);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel codeLabel = new JLabel(code);
        codeLabel.setFont(new Font("Consolas", Font.PLAIN, 9));
        codeLabel.setForeground(new Color(100, 100, 100));
        codeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        devPanel.add(nameLabel);
        devPanel.add(roleLabel);
        devPanel.add(Box.createVerticalStrut(3));
        devPanel.add(codeLabel);

        return devPanel;
    }

    private JPanel createCopyrightPanel() {
        JPanel copyrightPanel = new JPanel();
        copyrightPanel.setLayout(new BoxLayout(copyrightPanel, BoxLayout.Y_AXIS));
        copyrightPanel.setBackground(Color.WHITE);
        copyrightPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JLabel copyrightLabel = new JLabel("© 2024 Red Dev: Todos los derechos reservados");
        copyrightLabel.setFont(new Font("Arial", Font.BOLD, 12));
        copyrightLabel.setForeground(new Color(220, 20, 60));
        copyrightLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel additionalLabel = new JLabel("Academia Peruana de Programación - Lima, Perú");
        additionalLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        additionalLabel.setForeground(Color.GRAY);
        additionalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        copyrightPanel.add(copyrightLabel);
        copyrightPanel.add(Box.createVerticalStrut(5));
        copyrightPanel.add(additionalLabel);

        return copyrightPanel;
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton closeButton = new JButton("Cerrar");
        closeButton.setPreferredSize(new Dimension(100, 30));
        closeButton.setFont(new Font("Arial", Font.PLAIN, 12));
        closeButton.setBackground(new Color(220, 20, 60));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorderPainted(false);

        closeButton.addActionListener(e -> dispose());

        footerPanel.add(closeButton);

        return footerPanel;
    }

    private void setupWindow() {
        setSize(600, 850);
        setLocationRelativeTo(null);
    }
}