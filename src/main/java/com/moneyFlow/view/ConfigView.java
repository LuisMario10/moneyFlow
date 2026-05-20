package com.moneyFlow.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class ConfigView extends JFrame {
    // Cores do tema
    private static final Color BG_DARK = new Color(18, 18, 24);
    private static final Color BG_CARD = new Color(30, 30, 42);
    private static final Color BG_NAVBAR = new Color(22, 22, 32);
    private static final Color ACCENT_BLUE = new Color(80, 140, 255);
    // private static final Color ACCENT_GREEN = new Color(72, 199, 142);
    // private static final Color ACCENT_RED = new Color(235, 87, 87);
    private static final Color TEXT_PRIMARY = new Color(235, 235, 245);
    private static final Color TEXT_SECONDARY = new Color(160, 160, 180);
    private static final Color BORDER_COLOR = new Color(55, 55, 75);

    // Fontes
    private static final Font FONT_LOGO = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONT_NAV = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_BTN = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 16);

    public ConfigView() {
        windowConfig();
        JPanel mainPanel = createMainPanel();
        setContentPane(mainPanel);
    }

    private void windowConfig() {
        setTitle("moneyFlow - Configurações");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(750, 550));
        setLocationRelativeTo(null);
        setBackground(BG_DARK);

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_DARK);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(this.createNavBar());
        topPanel.add(this.createActionsBar());

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);

        return mainPanel;
    }

    // ==================== NAVBAR ====================
    private JPanel createNavBar() {
        JPanel navbar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(BORDER_COLOR);
                g.fillRect(0, getHeight() - 1, getWidth(), 1);
            }
        };
        navbar.setBackground(BG_NAVBAR);
        navbar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel logo = new JLabel("moneyFlow");
        logo.setFont(FONT_LOGO);
        logo.setForeground(ACCENT_BLUE);
        logo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose(); // Volta para a tela principal (HomeView)
            }
        });
        navbar.add(logo, BorderLayout.WEST);

        JPanel menusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 0));
        menusPanel.setOpaque(false);

        String[] menus = {"Meu Perfil", "Balanços", "Configurações"};
        for (String menu : menus) {
            menusPanel.add(createMenuLabel(menu));
        }
        navbar.add(menusPanel, BorderLayout.EAST);
        return navbar;
    }

    private JLabel createMenuLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONT_NAV);

        // Define dinamicamente a cor padrão da aba
        Color defaultColor = texto.equals("Configurações") ? ACCENT_BLUE : TEXT_SECONDARY;
        label.setForeground(defaultColor);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(TEXT_PRIMARY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Retorna perfeitamente para a cor padrão sem bugar
                label.setForeground(defaultColor);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if ("Meu Perfil".equals(texto)) {
                    dispose();
                    new UserAccountManagementView().setVisible(true);
                }
            }
        });

        return label;
    }

    // ==================== BARRA DE AÇÕES ====================
    private JPanel createActionsBar() {
        // GridBagLayout garante centralização matemática real do título
        JPanel barPanel = new JPanel(new GridBagLayout());
        barPanel.setBackground(BG_NAVBAR);
        barPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JLabel title = new JLabel("Configurações");
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT_PRIMARY);
        barPanel.add(title);

        return barPanel;
    }

    // ==================== CONTEÚDO CENTRAL ====================
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_DARK);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        contentPanel.add(createSelectionCard("Defina o Tema:", new String[]{"Light", "Dark"}));
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(createSelectionCard("Moeda Corrente:", new String[]{"Real (R$)", "Euro (€)", "Libra (£)", "Dólar ($)"}));

        return contentPanel;
    }

    private JPanel createSelectionCard(String title, String[] options) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(800, 120));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(TEXT_PRIMARY);
        card.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        buttonsPanel.setOpaque(false);

        for (String opt : options) {
            buttonsPanel.add(createCustomButton(opt, BORDER_COLOR, TEXT_SECONDARY));
        }

        card.add(buttonsPanel, BorderLayout.CENTER);
        return card;
    }

    private JButton createCustomButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bgColor.brighter() : bgColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(fgColor);
        btn.setPreferredSize(new Dimension(110, 35));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}