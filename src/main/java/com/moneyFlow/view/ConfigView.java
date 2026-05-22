package com.moneyFlow.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import com.moneyFlow.util.ThemeManager;
import com.moneyFlow.util.CurrencyManager;

public class ConfigView extends JFrame {

    private final ThemeManager theme = ThemeManager.getInstance();
    private final CurrencyManager currencyManager = CurrencyManager.getInstance();

    // Fontes
    private static final Font FONT_LOGO = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONT_NAV = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_BTN = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 16);

    // Referências para os botões de tema (para destacar o ativo)
    private JButton btnLight;
    private JButton btnDark;

    // Listeners de eventos
    private Runnable themeListener;
    private Runnable currencyListener;

    public ConfigView() {
        windowConfig();
        JPanel mainPanel = createMainPanel();
        setContentPane(mainPanel);
        registerListeners();
    }

    private void windowConfig() {
        setTitle("moneyFlow - Configurações");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(750, 550));
        setLocationRelativeTo(null);
        setBackground(theme.getBgDark());

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerListeners() {
        themeListener = () -> {
            SwingUtilities.invokeLater(() -> {
                setContentPane(createMainPanel());
                revalidate();
                repaint();
            });
        };
        theme.addThemeChangeListener(themeListener);

        currencyListener = () -> {
            SwingUtilities.invokeLater(() -> {
                setContentPane(createMainPanel());
                revalidate();
                repaint();
            });
        };
        currencyManager.addCurrencyChangeListener(currencyListener);

        // Remove os listeners ao fechar a janela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                theme.removeThemeChangeListener(themeListener);
                currencyManager.removeCurrencyChangeListener(currencyListener);
            }
        });
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(theme.getBgDark());

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
                g.setColor(theme.getBorderColor());
                g.fillRect(0, getHeight() - 1, getWidth(), 1);
            }
        };
        navbar.setBackground(theme.getBgNavbar());
        navbar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel logo = new JLabel("moneyFlow");
        logo.setFont(FONT_LOGO);
        logo.setForeground(theme.getAccentBlue());
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
        Color defaultColor = texto.equals("Configurações") ? theme.getAccentBlue() : theme.getTextSecondary();
        label.setForeground(defaultColor);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(theme.getTextPrimary());
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
        barPanel.setBackground(theme.getBgNavbar());
        barPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

        JLabel title = new JLabel("Configurações");
        title.setFont(FONT_TITLE);
        title.setForeground(theme.getTextPrimary());
        barPanel.add(title);

        return barPanel;
    }

    // ==================== CONTEÚDO CENTRAL ====================
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(theme.getBgDark());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        contentPanel.add(createThemeCard());
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(createCurrencyCard());

        return contentPanel;
    }

    // ==================== CARD DE TEMA ====================
    private JPanel createThemeCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(theme.getBgCard());
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(theme.getBorderColor(), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(800, 120));

        JLabel titleLabel = new JLabel("Defina o Tema:");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(theme.getTextPrimary());
        card.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        buttonsPanel.setOpaque(false);

        // Botão Light
        boolean isLight = !theme.isDarkMode();
        btnLight = createActiveButton("☀ Light", isLight);
        btnLight.addActionListener(e -> theme.setTheme(false));

        // Botão Dark
        boolean isDark = theme.isDarkMode();
        btnDark = createActiveButton("🌙 Dark", isDark);
        btnDark.addActionListener(e -> theme.setTheme(true));

        buttonsPanel.add(btnLight);
        buttonsPanel.add(btnDark);

        card.add(buttonsPanel, BorderLayout.CENTER);
        return card;
    }

    // ==================== CARD DE MOEDA ====================
    private JPanel createCurrencyCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(theme.getBgCard());
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(theme.getBorderColor(), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setMaximumSize(new Dimension(800, 120));

        JLabel titleLabel = new JLabel("Moeda Corrente:");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(theme.getTextPrimary());
        card.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        buttonsPanel.setOpaque(false);

        CurrencyManager.ECurrency activeCurrency = currencyManager.getCurrency();

        for (CurrencyManager.ECurrency cur : CurrencyManager.ECurrency.values()) {
            boolean active = (cur == activeCurrency);
            JButton btn = createActiveButton(cur.getDisplayName(), active);
            btn.addActionListener(e -> currencyManager.setCurrency(cur));
            buttonsPanel.add(btn);
        }

        card.add(buttonsPanel, BorderLayout.CENTER);
        return card;
    }

    /**
     * Cria um botão estilizado, destacando em azul caso esteja ativo.
     */
    private JButton createActiveButton(String text, boolean active) {
        Color bgColor = active ? theme.getAccentBlue() : theme.getBorderColor();
        Color fgColor = active ? Color.WHITE : theme.getTextSecondary();

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
        btn.setPreferredSize(new Dimension(130, 35));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}