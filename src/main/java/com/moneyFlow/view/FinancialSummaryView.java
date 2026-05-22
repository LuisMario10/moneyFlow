package com.moneyFlow.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.moneyFlow.DAO.FinancialTransactionDAO;
import com.moneyFlow.model.FinancialTransactionModel;
import com.moneyFlow.service.AuthService;
import com.moneyFlow.util.ThemeManager;
import com.moneyFlow.util.CurrencyManager;

public class FinancialSummaryView extends JFrame {

    private final ThemeManager theme = ThemeManager.getInstance();
    private final CurrencyManager currencyManager = CurrencyManager.getInstance();

    private static final Font FONT_LOGO      = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONT_NAV       = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_BTN       = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FONT_TITLE     = new Font("SansSerif", Font.BOLD, 18);
    private static final Font FONT_SECTION   = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FONT_CARD_LBL = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_CARD_VAL = new Font("SansSerif", Font.BOLD, 20);
    private static final Font FONT_ITEM      = new Font("SansSerif", Font.BOLD, 14);

    private int userId = 1;
    private Runnable themeListener;
    private Runnable currencyListener;

    // Dados de balanço agrupados
    private int totalCreditInCents = 0;
    private int totalDebitInCents = 0;
    private final Map<String, Integer> creditsByCategory = new LinkedHashMap<>();
    private final Map<String, Integer> debitsByCategory = new LinkedHashMap<>();
    private String currentMonthName = "";

    public FinancialSummaryView() {
        if (AuthService.loggedUser != null) {
            this.userId = AuthService.loggedUser.getId();
        }
        windowConfig();
        loadAndGroupData();
        setContentPane(createMainPanel());
        registerListeners();
    }

    private void windowConfig() {
        setTitle("moneyFlow - Balanço Financeiro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 680);
        setMinimumSize(new Dimension(800, 580));
        setLocationRelativeTo(null);
        setBackground(theme.getBgDark());
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAndGroupData() {
        totalCreditInCents = 0;
        totalDebitInCents = 0;
        creditsByCategory.clear();
        debitsByCategory.clear();

        // Determina o mês atual e formata sufixo
        LocalDate now = LocalDate.now();
        String suffix = String.format("/%02d/%d", now.getMonthValue(), now.getYear());

        // Nome amigável do mês
        String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        currentMonthName = meses[now.getMonthValue() - 1] + " de " + now.getYear();

        // Busca lançamentos no DB
        FinancialTransactionDAO dao = new FinancialTransactionDAO();
        List<FinancialTransactionModel> transactions = dao.findAllByUserId(userId);

        for (FinancialTransactionModel t : transactions) {
            String date = t.getDate();
            if (date != null && date.endsWith(suffix)) {
                int amount = t.getAmountInCents();
                String category = t.getCategory();
                if (category == null || category.trim().isEmpty()) {
                    category = "Outros";
                }

                if (amount >= 0) {
                    totalCreditInCents += amount;
                    creditsByCategory.put(category, creditsByCategory.getOrDefault(category, 0) + amount);
                } else {
                    int absAmount = Math.abs(amount);
                    totalDebitInCents += absAmount;
                    debitsByCategory.put(category, debitsByCategory.getOrDefault(category, 0) + absAmount);
                }
            }
        }
    }

    private void registerListeners() {
        themeListener = () -> SwingUtilities.invokeLater(() -> {
            setContentPane(createMainPanel());
            revalidate();
            repaint();
        });
        theme.addThemeChangeListener(themeListener);

        currencyListener = () -> SwingUtilities.invokeLater(() -> {
            setContentPane(createMainPanel());
            revalidate();
            repaint();
        });
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
        topPanel.add(createSubBar());

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(createContentScrollPanel(), BorderLayout.CENTER);

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
                dispose(); // Volta para a Home
            }
        });
        navbar.add(logo, BorderLayout.WEST);

        JPanel menusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 0));
        menusPanel.setOpaque(false);
        String[] menus = {"Meu Perfil", "Balanços", "Configurações"};
        for (String menu : menus) {
            JLabel menuLabel = createMenuLabel(menu, menu.equals("Balanços"));
            menusPanel.add(menuLabel);
        }
        navbar.add(menusPanel, BorderLayout.EAST);
        return navbar;
    }

    private JLabel createMenuLabel(String texto, boolean active) {
        JLabel label = new JLabel(texto);
        label.setFont(active ? new Font("SansSerif", Font.BOLD, 14) : FONT_NAV);
        label.setForeground(active ? theme.getAccentBlue() : theme.getTextSecondary());
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (!active) {
            label.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { label.setForeground(theme.getTextPrimary()); }
                @Override public void mouseExited(MouseEvent e)  { label.setForeground(theme.getTextSecondary()); }
                @Override public void mouseClicked(MouseEvent e) {
                    dispose();
                    if ("Meu Perfil".equals(texto)) {
                        new UserAccountManagementView().setVisible(true);
                    } else if ("Configurações".equals(texto)) {
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

        JLabel title = new JLabel("Balanço Financeiro - " + currentMonthName);
        title.setFont(FONT_TITLE);
        title.setForeground(theme.getTextPrimary());
        title.setHorizontalAlignment(SwingConstants.CENTER);
        bar.add(title, BorderLayout.CENTER);
        return bar;
    }

    private JScrollPane createContentScrollPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(theme.getBgDark());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 45, 25, 45));

        // Resumos Mensais (Cards superiores)
        contentPanel.add(createSummaryPanel());
        contentPanel.add(Box.createVerticalStrut(30));

        // Divisão lado a lado das Receitas vs Despesas
        contentPanel.add(createSplitBreakdownPanel());

        JScrollPane scroll = new JScrollPane(contentPanel);
        scroll.setBorder(null);
        scroll.setBackground(theme.getBgDark());
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.setMaximumSize(new Dimension(1200, 100));

        // Total Receitas
        summaryPanel.add(createSummaryCard("Receitas do Mês", totalCreditInCents, theme.getAccentGreen()));

        // Total Despesas
        summaryPanel.add(createSummaryCard("Despesas do Mês", -totalDebitInCents, theme.getAccentRed()));

        // Saldo do Mês
        int balance = totalCreditInCents - totalDebitInCents;
        Color balanceColor = balance >= 0 ? theme.getAccentGreen() : theme.getAccentRed();
        summaryPanel.add(createSummaryCard("Saldo do Mês", balance, balanceColor));

        return summaryPanel;
    }

    private JPanel createSummaryCard(String labelText, int valueInCents, Color valueColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme.getBgCard());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(theme.getBorderColor());
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JLabel label = new JLabel(labelText);
        label.setFont(FONT_CARD_LBL);
        label.setForeground(theme.getTextSecondary());
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel value = new JLabel(currencyManager.format(valueInCents));
        value.setFont(FONT_CARD_VAL);
        value.setForeground(valueColor);
        value.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(label);
        card.add(Box.createVerticalStrut(6));
        card.add(value);

        return card;
    }

    private JPanel createSplitBreakdownPanel() {
        JPanel splitPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        splitPanel.setOpaque(false);

        // Painel das Receitas (Coluna Esquerda)
        splitPanel.add(createBreakdownColumn("Fontes de Receitas", creditsByCategory, theme.getAccentGreen(), true));

        // Painel das Despesas (Coluna Direita)
        splitPanel.add(createBreakdownColumn("Distribuição de Despesas", debitsByCategory, theme.getAccentRed(), false));

        return splitPanel;
    }

    private JPanel createBreakdownColumn(String titleText, Map<String, Integer> data, Color accentColor, boolean isCredit) {
        JPanel colPanel = new JPanel();
        colPanel.setLayout(new BoxLayout(colPanel, BoxLayout.Y_AXIS));
        colPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(titleText);
        titleLabel.setFont(FONT_SECTION);
        titleLabel.setForeground(theme.getTextPrimary());
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 12, 0));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        colPanel.add(titleLabel);

        JPanel listCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme.getBgCard());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(theme.getBorderColor());
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
            }
        };
        listCard.setOpaque(false);
        listCard.setLayout(new BoxLayout(listCard, BoxLayout.Y_AXIS));
        listCard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        listCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (data.isEmpty()) {
            JLabel emptyLabel = new JLabel(isCredit ? "Nenhuma receita registrada este mês." : "Nenhuma despesa registrada este mês.");
            emptyLabel.setFont(FONT_CARD_LBL);
            emptyLabel.setForeground(theme.getTextSecondary());
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listCard.add(Box.createVerticalGlue());
            listCard.add(emptyLabel);
            listCard.add(Box.createVerticalGlue());
            // Garante tamanho mínimo amigável
            listCard.setMinimumSize(new Dimension(300, 180));
            listCard.setPreferredSize(new Dimension(300, 180));
        } else {
            // Calcula soma total da coluna para fins de porcentagem
            long sum = 0;
            for (int val : data.values()) {
                sum += val;
            }

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                double pct = sum > 0 ? (entry.getValue() * 100.0 / sum) : 0.0;
                listCard.add(createCategoryRow(entry.getKey(), entry.getValue(), pct, accentColor, isCredit));
                listCard.add(Box.createVerticalStrut(15));
            }
        }

        colPanel.add(listCard);
        return colPanel;
    }

    private JPanel createCategoryRow(String name, int amountInCents, double percentage, Color barColor, boolean isCredit) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        // Cabeçalho da Categoria (Nome + Porcentagem)
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(FONT_ITEM);
        nameLabel.setForeground(theme.getTextPrimary());
        leftPanel.add(nameLabel);

        // Badge de porcentagem
        JLabel pctBadge = new JLabel(String.format(" %.1f%% ", percentage)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(theme.getBgInput());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 6, 6));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        pctBadge.setFont(new Font("SansSerif", Font.BOLD, 11));
        pctBadge.setForeground(theme.getTextSecondary());
        leftPanel.add(pctBadge);

        row.add(leftPanel, BorderLayout.WEST);

        // Valor Formatado na moeda ativa (Direita)
        int displayValue = isCredit ? amountInCents : -amountInCents;
        JLabel valueLabel = new JLabel(currencyManager.format(displayValue));
        valueLabel.setFont(FONT_ITEM);
        valueLabel.setForeground(barColor);
        row.add(valueLabel, BorderLayout.EAST);

        // Barra de progresso visual abaixo da row
        JPanel bottomPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Trilha de fundo da barra
                g2.setColor(theme.getBgInput());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 6, 6, 6));
                
                // Preenchimento proporcional
                g2.setColor(barColor);
                int filledWidth = (int) (getWidth() * (percentage / 100.0));
                if (filledWidth > 0) {
                    g2.fill(new RoundRectangle2D.Float(0, 0, filledWidth, 6, 6, 6));
                }
                g2.dispose();
            }
        };
        bottomPanel.setOpaque(false);
        bottomPanel.setPreferredSize(new Dimension(100, 6));

        JPanel container = new JPanel(new BorderLayout(0, 5));
        container.setOpaque(false);
        container.add(row, BorderLayout.NORTH);
        container.add(bottomPanel, BorderLayout.SOUTH);

        return container;
    }
}
