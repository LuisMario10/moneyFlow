package com.moneyFlow.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import com.moneyFlow.controller.AuthController;
import com.moneyFlow.util.ThemeManager;

public class LoginView extends JFrame {

    private final ThemeManager theme = ThemeManager.getInstance();
    private static final Color ERROR_RED = new Color(235, 87, 87);

    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel errorLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton clearBtn;
    private JButton loginBtn;
    private JButton signUpBtn;

    public LoginView() {
        setTitle("moneyFlow - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 700);
        setMinimumSize(new Dimension(850, 700));
        setLocationRelativeTo(null);
        getContentPane().setBackground(theme.getBgDark());
        setLayout(new GridBagLayout());
        add(createLoginCard());
    }

    private JPanel createLoginCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme.getBgCard());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(theme.getBorderColor());
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(700, 520));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(50, 60, 50, 60));

        JLabel labelLogo = new JLabel("moneyFlow - Seu APP para gerir dinheiro");
        labelLogo.setFont(new Font("SansSerif", Font.BOLD, 28));
        labelLogo.setForeground(theme.getAccentBlue());
        labelLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(labelLogo);
        card.add(Box.createVerticalStrut(10));
        card.add(createSeparator());
        card.add(Box.createVerticalStrut(35));

        usernameLabel = createFieldLabel("NOME");
        usernameField = createCustomTextField("Digite seu nome");
        card.add(usernameLabel);
        card.add(usernameField);
        card.add(Box.createVerticalStrut(25));

        passwordLabel = createFieldLabel("SENHA");
        passwordField = createCustomPasswordField("Digite sua senha");
        card.add(passwordLabel);
        card.add(passwordField);
        card.add(Box.createVerticalStrut(10));

        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        errorLabel.setForeground(ERROR_RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(createButtonPanel());
        return card;
    }

    private JComponent createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(400, 1));
        sep.setForeground(theme.getBorderColor());
        return sep;
    }

    private JTextField createCustomTextField(String hint) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(theme.getTextSecondary());
                    g2.setFont(getFont().deriveFont(Font.BOLD));
                    int padding = getInsets().left;
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(hint, padding, y);
                    g2.dispose();
                }
            }
        };
        setupFieldStyle(field);
        return field;
    }

    private JPasswordField createCustomPasswordField(String hint) {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(theme.getTextSecondary());
                    g2.setFont(getFont().deriveFont(Font.BOLD));
                    int padding = getInsets().left;
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(hint, padding, y);
                    g2.dispose();
                }
            }
        };
        setupFieldStyle(field);
        return field;
    }

    private void setupFieldStyle(JTextField field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        field.setBackground(theme.getBgDark());
        field.setForeground(theme.getTextPrimary());
        field.setCaretColor(theme.getTextPrimary());
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(theme.getBorderColor(), 1, true),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { field.repaint(); }
            @Override public void focusLost(FocusEvent e) { field.repaint(); }
        });
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        clearBtn = createStyledButton("Limpar", theme.getBorderColor(), theme.getTextSecondary());
        signUpBtn = createStyledButton("Criar Conta", theme.getAccentBlue(), Color.WHITE);
        loginBtn = createStyledButton("Entrar", theme.getBorderColor(), theme.getTextSecondary());
        clearBtn.addActionListener(e -> { usernameField.setText(""); passwordField.setText(""); errorLabel.setText(" "); });
        signUpBtn.addActionListener(e -> { new SignUpView().setVisible(true); this.dispose(); });
        loginBtn.addActionListener(e -> {
            String user = usernameField.getText().trim();
            String pass = new String(passwordField.getPassword());
            if (user.isEmpty()) { errorLabel.setText("Preencha o campo nome."); return; }
            if (pass.length() < 8) { errorLabel.setText("Senha deve conter no mínimo 8 caracteres"); return; }
            errorLabel.setText(" ");
            AuthController.login(user, pass);
        });
        panel.add(clearBtn); panel.add(signUpBtn); panel.add(loginBtn);
        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(fgColor);
        btn.setPreferredSize(new Dimension(150, 40));
        btn.setContentAreaFilled(false); btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(theme.getTextSecondary());
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    public JTextField getUsernameField() { return usernameField; }
    public JPasswordField getPasswordField() { return passwordField; }
    public JButton getClearBtn() { return clearBtn; }
    public JButton getSignUpBtn() { return signUpBtn; }
    public JButton getLoginBtn() { return loginBtn; }
    public void setUsernameField(JTextField usernameField) { this.usernameField = usernameField; }
    public void setPasswordField(JPasswordField passwordField) { this.passwordField = passwordField; }
}
