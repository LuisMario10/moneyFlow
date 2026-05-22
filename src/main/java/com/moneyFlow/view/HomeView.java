package com.moneyFlow.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import com.moneyFlow.controller.FinancialSummaryController;
import com.moneyFlow.controller.FinancialTransactionController;
import com.moneyFlow.model.FinancialTransactionModel;
import com.moneyFlow.model.EFinancialType;
import com.moneyFlow.util.CurrencyUtils;
import com.moneyFlow.util.ThemeManager;
import com.moneyFlow.util.CurrencyManager;
import com.moneyFlow.view.dialogs.NewTransactionDialog;

public class HomeView extends JFrame {

    private final ThemeManager theme = ThemeManager.getInstance();
    private final CurrencyManager currencyManager = CurrencyManager.getInstance();

    private static final Font FONT_LOGO = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONT_NAV = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_BTN = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FONT_SALDO = new Font("SansSerif", Font.BOLD, 28);
    private static final Font FONT_FEEDBACK = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_CARD_TITLE = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_CARD_VALUE = new Font("SansSerif", Font.BOLD, 20);
    private static final Font FONT_CARD_INFO = new Font("SansSerif", Font.PLAIN, 13);

    private int userId = 1;
    private FinancialTransactionController transactionController = new FinancialTransactionController();
    private FinancialSummaryController summaryController = new FinancialSummaryController();

    private int currentBalanceInCents = 0;
    private String feedbackMessage = "Bem-vindo ao moneyFlow!";
    private String titleLastOperation = "Sem lançamentos";
    private String categoryLastOperation = "-";
    private int valueLastOperationInCents = 0;
    private String dateLastOperation = "00/00/0000 - 00:00";

    private JLabel balanceLabel;
    private JLabel feedbackLabel;
    private JLabel lastOperationTitleLabel;
    private JLabel lastOperationValueLabel;
    private JLabel lastOperationDateLabel;
    private Runnable themeListener;
    private Runnable currencyListener;

    public HomeView() {
        if (com.moneyFlow.service.AuthService.loggedUser != null) {
            this.userId = com.moneyFlow.service.AuthService.loggedUser.getId();
        }
        windowConfig();
        loadDataFromDatabase();
        setContentPane(createMainPanel());
        registerListeners();
    }

    private void loadDataFromDatabase() {
        this.currentBalanceInCents = summaryController.getBalance(this.userId);
        if (this.currentBalanceInCents > 0) {
            this.feedbackMessage = "Voce Esta Indo Bem";
        } else if (this.currentBalanceInCents == 0) {
            this.feedbackMessage = "Fique Atento";
        } else {
            this.feedbackMessage = "Cuidado Com os Gastos";
        }
    }

    private void windowConfig() {
        setTitle("moneyFlow - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setMinimumSize(new Dimension(750, 550));
        setLocationRelativeTo(null);
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception e) { e.printStackTrace(); }
    }

    private void registerListeners() {
        themeListener = () -> SwingUtilities.invokeLater(() -> { setContentPane(createMainPanel()); revalidate(); repaint(); });
        theme.addThemeChangeListener(themeListener);

        currencyListener = () -> SwingUtilities.invokeLater(() -> { setContentPane(createMainPanel()); revalidate(); repaint(); });
        currencyManager.addCurrencyChangeListener(currencyListener);

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
        topPanel.setBackground(theme.getBgDark());
        topPanel.add(createNavBar());
        topPanel.add(createActionsBar());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createNavBar() {
        JPanel navbar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) { super.paintComponent(g); g.setColor(theme.getBorderColor()); g.fillRect(0, getHeight()-1, getWidth(), 1); }
        };
        navbar.setBackground(theme.getBgNavbar());
        navbar.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel logo = new JLabel("moneyFlow");
        logo.setFont(FONT_LOGO);
        logo.setForeground(theme.getAccentBlue());
        logo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        navbar.add(logo, BorderLayout.WEST);
        JPanel menusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 0));
        menusPanel.setOpaque(false);
        for (String menu : new String[]{"Meu Perfil", "Balanços", "Configurações"}) { menusPanel.add(createMenuLabel(menu)); }
        navbar.add(menusPanel, BorderLayout.EAST);
        return navbar;
    }

    private JLabel createMenuLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONT_NAV);
        label.setForeground(theme.getTextSecondary());
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { label.setForeground(theme.getTextPrimary()); }
            @Override public void mouseExited(MouseEvent e) { label.setForeground(theme.getTextSecondary()); }
            @Override public void mouseClicked(MouseEvent e) {
                if ("Meu Perfil".equals(texto)) { new UserAccountManagementView().setVisible(true); }
                else if ("Configurações".equals(texto)) { new ConfigView().setVisible(true); }
            }
        });
        return label;
    }

    private JPanel createActionsBar() {
        JPanel barPanel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) { super.paintComponent(g); g.setColor(theme.getBorderColor()); g.fillRect(0, getHeight()-1, getWidth(), 1); }
        };
        barPanel.setBackground(theme.getBgNavbar());
        barPanel.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);
        leftPanel.add(createCustomButton("⚙ Relatório", theme.getBorderColor(), theme.getTextSecondary()));
        barPanel.add(leftPanel, BorderLayout.WEST);
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        centerPanel.setOpaque(false);
        JButton btnNovaReceita = createCustomButton("+ Nova Receita", new Color(40, 80, 60), theme.getAccentGreen());
        btnNovaReceita.addActionListener(e -> openNewTransactionDialog(true));
        JButton btnNovaDespesa = createCustomButton("- Nova Despesa", new Color(80, 35, 35), theme.getAccentRed());
        btnNovaDespesa.addActionListener(e -> openNewTransactionDialog(false));
        centerPanel.add(btnNovaReceita);
        centerPanel.add(btnNovaDespesa);
        barPanel.add(centerPanel, BorderLayout.CENTER);
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(createCustomButton("Histórico", theme.getBorderColor(), theme.getTextSecondary()));
        barPanel.add(rightPanel, BorderLayout.EAST);
        return barPanel;
    }

    private JButton createCustomButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bgColor.brighter() : bgColor);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BTN); btn.setForeground(fgColor); btn.setPreferredSize(new Dimension(145, 34));
        btn.setBorderPainted(false); btn.setContentAreaFilled(false); btn.setFocusPainted(false); btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(theme.getBgDark());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        contentPanel.add(createBalanceCard());
        contentPanel.add(Box.createVerticalStrut(25));
        contentPanel.add(createLastOperationCard());
        contentPanel.add(Box.createVerticalGlue());
        return contentPanel;
    }

    private JPanel createBalanceCard() {
        JPanel card = createBaseCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(theme.getBorderColor(), 1, true), BorderFactory.createEmptyBorder(25, 30, 25, 30)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        JLabel titleLabel = new JLabel("Saldo Atual");
        titleLabel.setFont(FONT_TITLE); titleLabel.setForeground(theme.getTextSecondary()); titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel); card.add(Box.createVerticalStrut(8));
        balanceLabel = new JLabel(formatCurrency(currentBalanceInCents));
        balanceLabel.setFont(FONT_SALDO); balanceLabel.setForeground(currentBalanceInCents >= 0 ? theme.getAccentGreen() : theme.getAccentRed()); balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(balanceLabel); card.add(Box.createVerticalStrut(8));
        JPanel feedbackPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        feedbackPanel.setOpaque(false);
        feedbackLabel = new JLabel("Feedback: " + feedbackMessage);
        feedbackLabel.setFont(FONT_FEEDBACK); feedbackLabel.setForeground(theme.getTextSecondary());
        feedbackPanel.add(feedbackLabel); feedbackPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(feedbackPanel);
        return card;
    }

    private JPanel createLastOperationCard() {
        JPanel card = createBaseCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(theme.getBorderColor(), 1, true), BorderFactory.createEmptyBorder(22, 30, 22, 30)));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        JLabel headerLabel = new JLabel("Ultima Operação Financeira");
        headerLabel.setFont(FONT_CARD_TITLE); headerLabel.setForeground(theme.getTextSecondary()); headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(headerLabel); card.add(Box.createVerticalStrut(12));
        lastOperationTitleLabel = new JLabel(titleLastOperation + " (" + categoryLastOperation + ")");
        lastOperationTitleLabel.setFont(FONT_CARD_TITLE); lastOperationTitleLabel.setForeground(theme.getTextPrimary()); lastOperationTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lastOperationTitleLabel); card.add(Box.createVerticalStrut(6));
        lastOperationValueLabel = new JLabel(formatCurrency(valueLastOperationInCents));
        lastOperationValueLabel.setFont(FONT_CARD_VALUE); lastOperationValueLabel.setForeground(valueLastOperationInCents >= 0 ? theme.getAccentGreen() : theme.getAccentRed()); lastOperationValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lastOperationValueLabel); card.add(Box.createVerticalStrut(10));
        lastOperationDateLabel = new JLabel(dateLastOperation);
        lastOperationDateLabel.setFont(FONT_CARD_INFO); lastOperationDateLabel.setForeground(theme.getTextSecondary()); lastOperationDateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(lastOperationDateLabel);
        return card;
    }

    private void openNewTransactionDialog(boolean isIncome) {
        NewTransactionDialog dialog = new NewTransactionDialog(this, isIncome);
        dialog.setVisible(true);
        if (dialog.isConfirmed()) {
            String title = dialog.getTitulo(); String value = dialog.getValor(); String date = dialog.getData();
            String category = dialog.getCategoria(); String description = dialog.getDescricao();
            int amountInCents = CurrencyUtils.parseValueToCents(value);
            if (!isIncome) { amountInCents = -Math.abs(amountInCents); }
            this.currentBalanceInCents += amountInCents;
            int currentResult = this.currentBalanceInCents;
            FinancialTransactionModel transaction = new FinancialTransactionModel(this.userId, title, EFinancialType.VARIABLE, amountInCents, currentResult, date, category, description);
            transactionController.post(transaction);
            summaryController.updateSummaryWithTransaction(this.userId, amountInCents, isIncome);
            updateBalance(currentResult);
            updateLastOperation(title, category, amountInCents, date);
        }
    }

    private JPanel createBaseCard() {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme.getBgCard());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        return card;
    }

    private String formatCurrency(int value) {
        return currencyManager.format(value);
    }

    public void updateBalance(int newBalance) {
        this.currentBalanceInCents = newBalance;
        this.balanceLabel.setText(formatCurrency(newBalance));
        this.balanceLabel.setForeground(newBalance >= 0 ? theme.getAccentGreen() : theme.getAccentRed());
        if (newBalance > 0) { this.feedbackMessage = "Voce Esta Indo Bem"; }
        else if (newBalance == 0) { this.feedbackMessage = "Fique Atento"; }
        else { this.feedbackMessage = "Cuidado Com os Gastos"; }
        this.feedbackLabel.setText("Feedback: " + this.feedbackMessage);
    }

    public void updateLastOperation(String title, String category, int valueInCents, String date) {
        this.titleLastOperation = title; this.categoryLastOperation = category;
        this.valueLastOperationInCents = valueInCents; this.dateLastOperation = date;
        this.lastOperationTitleLabel.setText(title + " (" + category + ")");
        this.lastOperationValueLabel.setText(formatCurrency(valueInCents));
        this.lastOperationValueLabel.setForeground(valueInCents >= 0 ? theme.getAccentGreen() : theme.getAccentRed());
        this.lastOperationDateLabel.setText(date);
    }
}
