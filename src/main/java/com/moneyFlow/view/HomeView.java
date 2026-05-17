package com.moneyFlow.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.util.Locale;

import com.moneyFlow.view.UserAccountManagementView;
import com.moneyFlow.util.CurrencyUtils;
import com.moneyFlow.view.dialogs.NewTransactionDialog;

public class HomeView extends JFrame {

    private static final Color BG_DARK = new Color(18, 18, 24);
    private static final Color BG_CARD = new Color(30, 30, 42);
    private static final Color BG_NAVBAR = new Color(22, 22, 32);
    private static final Color ACCENT_BLUE = new Color(80, 140, 255);
    private static final Color ACCENT_GREEN = new Color(72, 199, 142);
    private static final Color ACCENT_RED = new Color(235, 87, 87);
    private static final Color TEXT_PRIMARY = new Color(235, 235, 245);
    private static final Color TEXT_SECONDARY = new Color(160, 160, 180);
    private static final Color BORDER_COLOR = new Color(55, 55, 75);

    private static final Font FONT_LOGO = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONT_NAV = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_BTN = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FONT_SALDO = new Font("SansSerif", Font.BOLD, 28);
    private static final Font FONT_FEEDBACK = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_CARD_TITLE = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_CARD_VALUE = new Font("SansSerif", Font.BOLD, 20);
    private static final Font FONT_CARD_INFO = new Font("SansSerif", Font.PLAIN, 13);

    private int currentBalanceInCents = 10000;
    private String feedbackMessage = "Você Esta Indo Bem";
    private String titleLastOperation = "Hort Fruit";
    private String categoryLastOperation = "Variável";
    private int valueLastOperationInCents = -15000;
    private String dateLastOperation = "00/00/0000 - 00:00";

    private JLabel balanceLabel;
    private JLabel feedbackLabel;
    private JLabel lastOperationTitleLabel;
    private JLabel lastOperationValueLabel;
    private JLabel lastOperationDateLabel;

    public HomeView() {
        windowConfig();
        JPanel mainPanel = createMainPanel();
        setContentPane(mainPanel);
    }

    private void windowConfig() {
        setTitle("moneyFlow - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        topPanel.add(this.createNavBar());
        topPanel.add(this.createActionsBar());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        JPanel contentPanel = this.createContentPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);
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
            JLabel menuLabel = this.createMenuLabel(menu);
            menusPanel.add(menuLabel);
        }
        navbar.add(menusPanel, BorderLayout.EAST);
        return navbar;
    }

    private JLabel createMenuLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONT_NAV);
        label.setForeground(TEXT_SECONDARY);
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(TEXT_PRIMARY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(TEXT_SECONDARY);
            }
        });
        return label;
    }

    private JPanel createActionsBar() {
        JPanel barPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(BORDER_COLOR);
                g2.fillRect(0, getHeight() - 1, getWidth(), 1);
                g2.dispose();
            }
        };
        barPanel.setBackground(BG_NAVBAR);
        barPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(this.createCustomButton("⚙ Relatório", BORDER_COLOR, TEXT_SECONDARY));
        barPanel.add(leftPanel, BorderLayout.WEST);
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        centerPanel.setOpaque(false);
        JButton btnReceita = this.createCustomButton("+ Nova Receita", new Color(40, 80, 60), ACCENT_GREEN);
        btnReceita.addActionListener(e -> openNewTransactionDialog(true));
        centerPanel.add(btnReceita);
        JButton btnDespesa = this.createCustomButton("- Nova Despesa", new Color(80, 35, 35), ACCENT_RED);
        btnDespesa.addActionListener(e -> openNewTransactionDialog(false));
        centerPanel.add(btnDespesa);
        barPanel.add(centerPanel, BorderLayout.CENTER);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(this.createCustomButton("Histórico", BORDER_COLOR, TEXT_SECONDARY));
        barPanel.add(rightPanel, BorderLayout.EAST);
        return barPanel;
    }

    private JButton createCustomButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(fgColor);
        btn.setPreferredSize(new Dimension(145, 34));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_DARK);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        contentPanel.add(this.createBalanceCard());
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(this.createLastOperationCard());
        contentPanel.add(Box.createVerticalGlue());
        return contentPanel;
    }

    private JPanel createBalanceCard() {
        JPanel card = this.createBaseCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        JLabel titleLabel = new JLabel("Saldo Atual");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(8));
        this.balanceLabel = new JLabel(formatCurrency(currentBalanceInCents));
        this.balanceLabel.setFont(FONT_SALDO);
        this.balanceLabel.setForeground(currentBalanceInCents >= 0 ? ACCENT_GREEN : ACCENT_RED);
        this.balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(balanceLabel);
        card.add(Box.createVerticalStrut(8));
        JPanel feedbackPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        feedbackPanel.setOpaque(false);
        feedbackLabel = new JLabel("Feedback: " + feedbackMessage);
        feedbackLabel.setFont(FONT_FEEDBACK);
        feedbackLabel.setForeground(TEXT_SECONDARY);
        feedbackPanel.add(feedbackLabel);
        feedbackPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(feedbackPanel);
        return card;
    }

    private JPanel createLastOperationCard() {
        JPanel card = this.createBaseCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                BorderFactory.createEmptyBorder(22, 30, 22, 30)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        JLabel headerLabel = new JLabel("Ultima Operação Financeira");
        headerLabel.setFont(FONT_CARD_TITLE);
        headerLabel.setForeground(TEXT_SECONDARY);
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(headerLabel);
        card.add(Box.createVerticalStrut(12));
        lastOperationTitleLabel = new JLabel(titleLastOperation + " (" + categoryLastOperation + ")");
        lastOperationTitleLabel.setFont(FONT_CARD_TITLE);
        lastOperationTitleLabel.setForeground(TEXT_PRIMARY);
        lastOperationTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lastOperationTitleLabel);
        card.add(Box.createVerticalStrut(6));
        lastOperationValueLabel = new JLabel(formatCurrency(valueLastOperationInCents));
        lastOperationValueLabel.setFont(FONT_CARD_VALUE);
        lastOperationValueLabel.setForeground(valueLastOperationInCents >= 0 ? ACCENT_GREEN : ACCENT_RED);
        lastOperationValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lastOperationValueLabel);
        card.add(Box.createVerticalStrut(10));
        lastOperationDateLabel = new JLabel(dateLastOperation);
        lastOperationDateLabel.setFont(FONT_CARD_INFO);
        lastOperationDateLabel.setForeground(TEXT_SECONDARY);
        lastOperationDateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lastOperationDateLabel);
        return card;
    }

    private void openNewTransactionDialog(boolean isIncome) {
        NewTransactionDialog dialog = new NewTransactionDialog(this, isIncome);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            String title = dialog.getTitulo();
            String value = dialog.getValor();
            String date = dialog.getData();
            String category = dialog.getCategoria();
            String description = dialog.getDescricao();
            int amountInCents = parseValueToCents(value);
            if (!isIncome) {
                amountInCents = -Math.abs(amountInCents);
            }
        }
    }

    private int parseValueToCents(String value) {
        try {
            value = value.replaceAll("[R$\\s]", "");
            value = value.replace(".", "");
            value = value.replace(",", ".");
            double parsed = Double.parseDouble(value);
            return (int) Math.round(parsed * 100);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private JPanel createBaseCard() {
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
        return card;
    }

    private String formatCurrency(int value) {
        double valueInDouble = CurrencyUtils.currencyConverter(value);
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
        String formatted = nf.format(Math.abs(valueInDouble));
        if (value < 0) {
            return "- " + formatted;
        }
        return formatted;
    }

    public void updateBalance(int newBalance) {
        this.currentBalanceInCents = newBalance;
        this.balanceLabel.setText(this.formatCurrency(newBalance));
        this.balanceLabel.setForeground(newBalance >= 0 ? ACCENT_GREEN : ACCENT_RED);
        if (newBalance > 0) {
            this.feedbackMessage = "Voce Esta Indo Bem";
        } else if (newBalance == 0) {
            this.feedbackMessage = "Fique Atento";
        } else {
            this.feedbackMessage = "Cuidado Com os Gastos";
        }
        this.feedbackLabel.setText("Feedback: " + this.feedbackMessage);
    }

    public void updateLastOperation(String title, String category, int valueInCents, String date) {
        this.titleLastOperation = title;
        this.categoryLastOperation = category;
        this.valueLastOperationInCents = valueInCents;
        this.dateLastOperation = date;
        this.lastOperationTitleLabel.setText(title + " (" + category + ")");
        this.lastOperationValueLabel.setText(this.formatCurrency(valueInCents));
        this.lastOperationValueLabel.setForeground(valueInCents >= 0 ? ACCENT_GREEN : ACCENT_RED);
        this.lastOperationDateLabel.setText(date);
    }
}
