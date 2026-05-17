package com.moneyFlow.view.dialogs;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NewTransactionDialog extends JDialog {

    private static final Color BG_DARK = new Color(30, 30, 42);
    private static final Color BG_INPUT = new Color(40, 40, 55);
    private static final Color ACCENT_COLOR = new Color(80, 140, 255);
    private static final Color TEXT_PRIMARY = new Color(235, 235, 245);
    private static final Color TEXT_SECONDARY = new Color(160, 160, 180);

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
        getContentPane().setBackground(BG_DARK);
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel lblHeader = new JLabel(isReceita ? "Adicionar Receita" : "Adicionar Despesa");
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblHeader.setForeground(isReceita ? new Color(72, 199, 142) : new Color(235, 87, 87));
        lblHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblHeader);
        mainPanel.add(Box.createVerticalStrut(25));

        txtTitle = createTextField();
        mainPanel.add(createFieldPanel("Título", txtTitle));

        txtValue = createTextField();
        mainPanel.add(createFieldPanel("Valor (R$)", txtValue));

        txtDate = createTextField();
        txtDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        mainPanel.add(createFieldPanel("Data (DD/MM/AAAA)", txtDate));

        String[] categories = isReceita ? new String[]{"Salário", "Investimento", "Outros"}
                                        : new String[]{"Alimentação", "Transporte", "Moradia", "Lazer", "Outros"};
        cbCategory = new JComboBox<>(categories);
        cbCategory.setBackground(BG_INPUT);
        cbCategory.setForeground(TEXT_PRIMARY);
        cbCategory.setFont(new Font("SansSerif", Font.PLAIN, 14));
        mainPanel.add(createFieldPanel("Categoria", cbCategory));

        txtDescription = new JTextArea(3, 20);
        txtDescription.setBackground(BG_INPUT);
        txtDescription.setForeground(TEXT_PRIMARY);
        txtDescription.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtDescription.setCaretColor(TEXT_PRIMARY);
        txtDescription.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(55, 55, 75)),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);

        JPanel descPanel = new JPanel(new BorderLayout(0, 5));
        descPanel.setBackground(BG_DARK);
        JLabel lblDesc = new JLabel("Descrição");
        lblDesc.setForeground(TEXT_SECONDARY);
        lblDesc.setFont(new Font("SansSerif", Font.BOLD, 13));
        descPanel.add(lblDesc, BorderLayout.NORTH);
        descPanel.add(new JScrollPane(txtDescription), BorderLayout.CENTER);
        descPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        mainPanel.add(descPanel);

        mainPanel.add(Box.createVerticalStrut(25));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsPanel.setBackground(BG_DARK);

        JButton btnCancel = createButton("Cancelar", new Color(60, 60, 75), TEXT_PRIMARY);
        btnCancel.addActionListener(e -> dispose());

        JButton btnSave = createButton("Salvar", ACCENT_COLOR, Color.WHITE);
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
        field.setBackground(BG_INPUT);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(55, 55, 75)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(BG_DARK);
        JLabel label = new JLabel(labelText);
        label.setForeground(TEXT_SECONDARY);
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
