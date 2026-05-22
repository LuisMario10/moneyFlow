package com.moneyFlow.view.dialogs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.moneyFlow.util.ThemeManager;
import com.moneyFlow.util.CurrencyManager;

public class NewTransactionDialog extends JDialog {

    private final ThemeManager theme = ThemeManager.getInstance();

    private JTextField txtTitle;
    private JTextField txtValue;
    private JTextField txtDate;
    private JComboBox<String> cbCategory;
    private JTextArea txtDescription;

    private boolean confirmed = false;
    private boolean isReceita;

    public NewTransactionDialog(JFrame parent, boolean isReceita) {
        super(parent, isReceita ? "Nova Receita" : "Nova Despesa", true);
        this.isReceita = isReceita;
        setSize(400, 550);
        setLocationRelativeTo(parent);
        setResizable(false);
        getContentPane().setBackground(theme.getBgCard());
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(theme.getBgCard());
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel lblHeader = new JLabel(isReceita ? "Adicionar Receita" : "Adicionar Despesa");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblHeader.setForeground(isReceita ? theme.getAccentGreen() : theme.getAccentRed());
        lblHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblHeader);
        mainPanel.add(Box.createVerticalStrut(25));

        txtTitle = createTextField();
        mainPanel.add(createFieldPanel("Título", txtTitle));

        txtValue = createTextField();
        mainPanel.add(createFieldPanel("Valor (" + CurrencyManager.getInstance().getCurrency().getSymbol() + ")", txtValue));

        txtDate = createTextField();
        txtDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        mainPanel.add(createFieldPanel("Data (DD/MM/AAAA)", txtDate));

        String[] categories = isReceita ? new String[]{"Salário", "Investimento", "Outros"}
                                        : new String[]{"Alimentação", "Transporte", "Moradia", "Lazer", "Outros"};
        cbCategory = new JComboBox<>(categories);
        cbCategory.setBackground(theme.getBgInput());
        cbCategory.setForeground(theme.getTextPrimary());
        cbCategory.setFont(new Font("SansSerif", Font.PLAIN, 14));
        mainPanel.add(createFieldPanel("Categoria", cbCategory));

        txtDescription = new JTextArea(3, 20);
        txtDescription.setBackground(theme.getBgInput());
        txtDescription.setForeground(theme.getTextPrimary());
        txtDescription.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtDescription.setCaretColor(theme.getTextPrimary());
        txtDescription.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(theme.getBorderColor()),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        JPanel descPanel = new JPanel(new BorderLayout(0, 5));
        descPanel.setBackground(theme.getBgCard());
        JLabel lblDesc = new JLabel("Descrição");
        lblDesc.setForeground(theme.getTextSecondary());
        lblDesc.setFont(new Font("SansSerif", Font.BOLD, 13));
        descPanel.add(lblDesc, BorderLayout.NORTH);
        descPanel.add(new JScrollPane(txtDescription), BorderLayout.CENTER);
        descPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        mainPanel.add(descPanel);

        mainPanel.add(Box.createVerticalStrut(25));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setBackground(theme.getBgCard());

        JButton btnCancel = createButton("Cancelar", theme.getBorderColor(), theme.getTextPrimary());
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = createButton("Salvar", theme.getAccentBlue(), Color.WHITE);
        btnSave.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        buttonsPanel.add(btnCancel);
        buttonsPanel.add(btnSave);
        mainPanel.add(buttonsPanel);

        add(mainPanel);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setBackground(theme.getBgInput());
        field.setForeground(theme.getTextPrimary());
        field.setCaretColor(theme.getTextPrimary());
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(theme.getBorderColor()),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(theme.getBgCard());
        JLabel label = new JLabel(labelText);
        label.setForeground(theme.getTextSecondary());
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        return panel;
    }

    private JButton createButton(String text, Color bgColor, Color fgColor) {
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
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(fgColor);
        btn.setPreferredSize(new Dimension(120, 38));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public boolean isConfirmed() { return confirmed; }
    public String getTitulo() { return txtTitle.getText().trim(); }
    public String getValor() { return txtValue.getText().trim(); }
    public String getData() { return txtDate.getText().trim(); }
    public String getCategoria() { return cbCategory.getSelectedItem().toString(); }
    public String getDescricao() { return txtDescription.getText().trim(); }
}