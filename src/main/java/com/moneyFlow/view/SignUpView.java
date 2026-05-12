package com.moneyFlow.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class SignUpView extends JFrame {

    private static final Color BG_DARK = new Color(18, 18, 24);
    private static final Color BG_CARD = new Color(30, 30, 42);
    private static final Color ACCENT_BLUE = new Color(80, 140, 255);
    private static final Color TEXT_PRIMARY = new Color(235, 235, 245);
    private static final Color TEXT_SECONDARY = new Color(160, 160, 180);
    private static final Color BORDER_COLOR = new Color(55, 55, 75);

    //Atributos da classe
    private JButton btnLimpar;
    private JButton btnCriarConta;
    private JButton btnEntrar;

    public SignUpView() {
        setTitle("moneyFlow - Cadastro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 700);
        setMinimumSize(new Dimension(850, 700));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new GridBagLayout());

        add(createSignUpCard());
    }

    private JPanel createSignUpCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(BORDER_COLOR);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(700, 550));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(40, 60, 40, 60));

        // Logo
        JLabel labelLogo = new JLabel("moneyFlow - Seu APP para gerir dinheiro");
        labelLogo.setFont(new Font("SansSerif", Font.BOLD, 28));
        labelLogo.setForeground(ACCENT_BLUE);
        labelLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(labelLogo);
        card.add(Box.createVerticalStrut(10));

        card.add(createSeparator());
        card.add(Box.createVerticalStrut(30));

        // Campo Usuário/Email
        card.add(createFieldLabel("E-MAIL"));
        JTextField emailField = createCustomTextField("Nome do Usuário");
        card.add(emailField);

        card.add(Box.createVerticalStrut(20));

        // Campo Senha
        card.add(createFieldLabel("SENHA"));
        JPasswordField passField = createCustomPasswordField("Senha");
        card.add(passField);

        card.add(Box.createVerticalStrut(20));

        // Campo Confirmar Senha
        card.add(createFieldLabel("CONFIRMAR SENHA"));
        JPasswordField confirmPassField = createCustomPasswordField("Confirmar Senha");
        card.add(confirmPassField);

        card.add(Box.createVerticalStrut(40));

        // Painel de Botões
        card.add(createButtonPanel());

        return card;
    }

    private JComponent createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(400, 1));
        sep.setForeground(BORDER_COLOR);
        return sep;
    }

    private JTextField createCustomTextField(String hint) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(TEXT_SECONDARY);
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
                    g2.setColor(TEXT_SECONDARY);
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
        field.setBackground(BG_DARK);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) { field.repaint(); }
            @Override public void focusLost(FocusEvent e) { field.repaint(); }
        });
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        btnLimpar = createStyledButton("Limpar", BORDER_COLOR, TEXT_SECONDARY);
        btnCriarConta = createStyledButton("Já Tenho Conta", ACCENT_BLUE, Color.WHITE);
        btnEntrar = createStyledButton("Entrar", BORDER_COLOR, TEXT_SECONDARY);

        panel.add(btnLimpar);
        panel.add(btnCriarConta);
        panel.add(btnEntrar);

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
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(TEXT_SECONDARY);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    // ==================== GETTERS ====================

    public JButton getBtnCriarConta() {
        return btnCriarConta;
    }

    public JButton getBtnLimpar() {
        return btnLimpar;
    }

    public JButton getBtnEntrar() {
        return btnEntrar;
    }
}