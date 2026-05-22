package com.moneyFlow.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import com.moneyFlow.DAO.UserDAO;
import com.moneyFlow.model.UserModel;
import com.moneyFlow.security.PasswordHasher;
import com.moneyFlow.service.AuthService;
import com.moneyFlow.util.ThemeManager;

public class UserAccountManagementView extends JFrame {

    private final ThemeManager theme = ThemeManager.getInstance();

    private static final Font FONT_LOGO      = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONT_NAV       = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_BTN       = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FONT_TITLE     = new Font("SansSerif", Font.BOLD, 18);
    private static final Font FONT_LABEL     = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_INPUT     = new Font("SansSerif", Font.PLAIN, 14);

    private JTextField fieldUsername;
    private JPasswordField fieldPassword;
    private JPasswordField fieldConfirmPassword;
    private JLabel errorLabel;
    private Runnable themeListener;

    private UserModel loggedUser;

    public UserAccountManagementView() {
        // Obter o usuário logado atualmente da sessão
        this.loggedUser = AuthService.loggedUser;
        if (this.loggedUser == null) {
            // Backup seguro caso não esteja logado
            this.loggedUser = new UserModel(1, "ADMIN", "", "12345678");
        }

        windowConfig();
        setContentPane(createMainPanel());
        registerThemeListener();
    }

    private void windowConfig() {
        setTitle("moneyFlow - Meu Perfil");
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

    private void registerThemeListener() {
        themeListener = () -> {
            SwingUtilities.invokeLater(() -> {
                setContentPane(createMainPanel());
                revalidate();
                repaint();
            });
        };
        theme.addThemeChangeListener(themeListener);

        // Remove o listener ao fechar a janela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                theme.removeThemeChangeListener(themeListener);
            }
        });
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(theme.getBgDark());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(theme.getBgDark());
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
                g2.setColor(theme.getBorderColor());
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
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
            JLabel menuLabel = createMenuLabel(menu, menu.equals("Meu Perfil"));
            menusPanel.add(menuLabel);
        }
        navbar.add(menusPanel, BorderLayout.EAST);
        return navbar;
    }

    private JLabel createMenuLabel(String texto, boolean active) {
        JLabel label = new JLabel(texto);
        label.setFont(active ? new Font("SansSerif", Font.BOLD, 14) : FONT_NAV);
        label.setForeground(active ? theme.getTextPrimary() : theme.getTextSecondary());
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (!active) {
            label.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { label.setForeground(theme.getTextPrimary()); }
                @Override public void mouseExited(MouseEvent e)  { label.setForeground(theme.getTextSecondary()); }
                @Override public void mouseClicked(MouseEvent e) {
                    if ("Configurações".equals(texto)) {
                        dispose();
                        new ConfigView().setVisible(true);
                    }
                }
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
                g2.setColor(theme.getBorderColor());
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
            }
        };
        bar.setBackground(theme.getBgNavbar());
        bar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel title = new JLabel("Gerenciar Conta");
        title.setFont(FONT_TITLE);
        title.setForeground(theme.getTextPrimary());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        bar.add(title, BorderLayout.CENTER);
        return bar;
    }

    private JPanel createContentPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(theme.getBgDark());
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
                g2.setColor(theme.getBgCard());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(theme.getBorderColor(), 1, true),
                BorderFactory.createEmptyBorder(35, 50, 30, 50)
        ));
        card.setPreferredSize(new Dimension(480, 430));

        card.add(createInputRow("Alterar Nome do Usuário"));
        fieldUsername = createTextField();
        fieldUsername.setText(loggedUser.getName()); // Preenche o nome atual
        card.add(fieldUsername);

        card.add(Box.createVerticalStrut(18));

        card.add(createInputRow("Alterar Senha"));
        fieldPassword = createPasswordField();
        card.add(fieldPassword);

        card.add(Box.createVerticalStrut(18));

        card.add(createInputRow("Confirmar Nova Senha"));
        fieldConfirmPassword = createPasswordField();
        card.add(fieldConfirmPassword);

        card.add(Box.createVerticalStrut(10));

        // Label de erros
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        errorLabel.setForeground(new Color(235, 87, 87));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(errorLabel);

        card.add(Box.createVerticalStrut(15));

        card.add(createButtonsRow());
        return card;
    }

    private JLabel createInputRow(String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(FONT_LABEL);
        label.setForeground(theme.getTextSecondary());
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
                g2.setColor(theme.getBgInput());
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
                g2.setColor(theme.getBgInput());
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
        field.setForeground(theme.getTextPrimary());
        field.setCaretColor(theme.getTextPrimary());
        field.setOpaque(false);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(theme.getBorderColor(), 1, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(theme.getAccentBlue(), 1, true),
                        BorderFactory.createEmptyBorder(8, 14, 8, 14)
                ));
            }
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(theme.getBorderColor(), 1, true),
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
        JButton btnVoltar = createCustomButton("Voltar", theme.getBorderColor(), theme.getTextSecondary());
        btnVoltar.addActionListener(e -> dispose());
        JButton btnConfirmar = createCustomButton("Confirmar", new Color(30, 60, 100), theme.getAccentBlue());
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

        if (username.isEmpty()) {
            errorLabel.setText("O nome do usuário não pode ser vazio.");
            return;
        }

        if (username.length() < 5) {
            errorLabel.setText("O nome deve conter no mínimo 5 caracteres.");
            return;
        }

        // Valida se o novo nome de usuário já pertence a outra pessoa
        UserDAO userDAO = new UserDAO();
        UserModel existing = userDAO.findByAccessName(username);
        if (existing != null && existing.getId() != loggedUser.getId()) {
            errorLabel.setText("Este nome de usuário já está em uso.");
            return;
        }

        // Valida senha se foi informada
        if (!password.isEmpty()) {
            if (password.length() < 8) {
                errorLabel.setText("A nova senha deve conter no mínimo 8 caracteres.");
                return;
            }
            if (!password.equals(confirm)) {
                errorLabel.setText("As senhas não coincidem.");
                return;
            }
        }

        errorLabel.setText(" "); // Limpa erros

        // Atualiza os dados
        loggedUser.setName(username);
        if (!password.isEmpty()) {
            String hashedPassword = PasswordHasher.hash(password);
            loggedUser.setPassword(hashedPassword);
        }

        // Salva persistido no Banco de Dados
        userDAO.update(loggedUser);

        // Atualiza os dados da sessão
        AuthService.loggedUser = loggedUser;

        JOptionPane.showMessageDialog(this, "Perfil atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
