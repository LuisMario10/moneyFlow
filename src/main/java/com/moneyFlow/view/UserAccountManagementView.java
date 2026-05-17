package com.moneyFlow.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class UserAccountManagementView extends JFrame {

    private static final Color BG_DARK       = new Color(18, 18, 24);
    private static final Color BG_CARD       = new Color(30, 30, 42);
    private static final Color BG_NAVBAR     = new Color(22, 22, 32);
    private static final Color ACCENT_BLUE   = new Color(80, 140, 255);
    private static final Color TEXT_PRIMARY  = new Color(235, 235, 245);
    private static final Color TEXT_SECONDARY= new Color(160, 160, 180);
    private static final Color BORDER_COLOR  = new Color(55, 55, 75);
    private static final Color INPUT_BG      = new Color(24, 24, 36);

    private static final Font FONT_LOGO      = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONT_NAV       = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_BTN       = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FONT_TITLE     = new Font("SansSerif", Font.BOLD, 18);
    private static final Font FONT_LABEL     = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_INPUT     = new Font("SansSerif", Font.PLAIN, 14);

    private JTextField fieldUsername;
    private JPasswordField fieldPassword;
    private JPasswordField fieldConfirmPassword;

    public UserAccountManagementView() {
        windowConfig();
        setContentPane(createMainPanel());
    }

    private void windowConfig() {
        setTitle("moneyFlow - Meu Perfil");
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
        topPanel.setBackground(BG_DARK);
        topPanel.add(createNavBar());
        topPanel.add(createSubBar());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createNavBar() {
        JPanel navbar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BORDER_COLOR);
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
            }
        };
        navbar.setBackground(BG_NAVBAR);
        navbar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel logo = new JLabel("moneyFlow");
        logo.setFont(FONT_LOGO);
        logo.setForeground(ACCENT_BLUE);
        logo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navbar.add(logo, BorderLayout.WEST);
        JPanel menusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 0));
        menusPanel.setOpaque(false);
        String[] menus = {"Meu Perfil", "Balanços", "Configurações"};
        for (String menu : menus) {
            JLabel menuLabel = createMenuLabel(menu, menu.equals("Meu Perfil"));
            menusPanel.add(menuLabel);
        }
        navbar.add(menusPanel, BorderLayout.EAST);
        return navbar;
    }

    private JLabel createMenuLabel(String texto, boolean active) {
        JLabel label = new JLabel(texto);
        label.setFont(active ? new Font("SansSerif", Font.BOLD, 14) : FONT_NAV);
        label.setForeground(active ? TEXT_PRIMARY : TEXT_SECONDARY);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (!active) {
            label.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { label.setForeground(TEXT_PRIMARY); }
                @Override public void mouseExited(MouseEvent e)  { label.setForeground(TEXT_SECONDARY); }
            });
        }
        return label;
    }

    private JPanel createSubBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BORDER_COLOR);
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
            }
        };
        bar.setBackground(BG_NAVBAR);
        bar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel title = new JLabel("Gerenciar Conta");
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT_PRIMARY);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        bar.add(title, BorderLayout.CENTER);
        return bar;
    }

    private JPanel createContentPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_DARK);
        JPanel card = createFormCard();
        wrapper.add(card);
        return wrapper;
    }

    private JPanel createFormCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(35, 50, 30, 50)
        ));
        card.setPreferredSize(new Dimension(480, 380));
        card.add(createInputRow("Alterar Nome do Usuário"));
        fieldUsername = createTextField();
        card.add(fieldUsername);
        card.add(Box.createVerticalStrut(18));
        card.add(createInputRow("Alterar Senha"));
        fieldPassword = createPasswordField();
        card.add(fieldPassword);
        card.add(Box.createVerticalStrut(18));
        card.add(createInputRow("Confirmar Nova Senha"));
        fieldConfirmPassword = createPasswordField();
        card.add(fieldConfirmPassword);
        card.add(Box.createVerticalStrut(30));
        card.add(createButtonsRow());
        return card;
    }

    private JLabel createInputRow(String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(FONT_LABEL);
        label.setForeground(TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleInputField(field);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(INPUT_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleInputField(field);
        return field;
    }

    private void styleInputField(JTextField field) {
        field.setFont(FONT_INPUT);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(ACCENT_BLUE, 1, true),
                        BorderFactory.createEmptyBorder(8, 14, 8, 14)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(BORDER_COLOR, 1, true),
                        BorderFactory.createEmptyBorder(8, 14, 8, 14)
                ));
            }
        });
    }

    private JPanel createButtonsRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JButton btnVoltar = createCustomButton("Voltar", BORDER_COLOR, TEXT_SECONDARY);
        btnVoltar.addActionListener(e -> dispose());
        JButton btnConfirmar = createCustomButton("Confirmar", new Color(30, 60, 100), ACCENT_BLUE);
        btnConfirmar.addActionListener(e -> handleConfirm());
        row.add(btnVoltar, BorderLayout.WEST);
        row.add(btnConfirmar, BorderLayout.EAST);
        return row;
    }

    private JButton createCustomButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bgColor.brighter() : bgColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(fgColor);
        btn.setPreferredSize(new Dimension(140, 36));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void handleConfirm() {
        String username = fieldUsername.getText().trim();
        String password = new String(fieldPassword.getPassword());
        String confirm  = new String(fieldConfirmPassword.getPassword());
        if (username.isEmpty() && password.isEmpty()) {
            showMessage("Preencha ao menos um campo para atualizar.", false);
            return;
        }
        if (!password.isEmpty() && !password.equals(confirm)) {
            showMessage("As senhas não coincidem. Tente novamente.", false);
            return;
        }

        showMessage("Perfil atualizado com sucesso!", true);
    }

    private void showMessage(String msg, boolean success) {
        JOptionPane optionPane = new JOptionPane(msg, JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, success ? "Sucesso" : "Atenção");
        dialog.setVisible(true);
    }
}