package com.moneyFlow.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;

/**
 * Gerenciador de moedas correntes para o moneyFlow.
 * Permite selecionar a moeda ativa, persiste a escolha via Preferences
 * e fornece utilitários de formatação de valores monetários.
 */
public class CurrencyManager {

    public enum ECurrency {
        REAL("Real (R$)", "R$", Locale.of("pt", "BR")),
        EURO("Euro (€)", "€", Locale.of("de", "DE")),
        LIBRA("Libra (£)", "£", Locale.UK),
        DOLAR("Dólar ($)", "$", Locale.US);

        private final String displayName;
        private final String symbol;
        private final Locale locale;

        ECurrency(String displayName, String symbol, Locale locale) {
            this.displayName = displayName;
            this.symbol = symbol;
            this.locale = locale;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getSymbol() {
            return symbol;
        }

        public Locale getLocale() {
            return locale;
        }

        public static ECurrency fromDisplayName(String displayName) {
            for (ECurrency c : values()) {
                if (c.getDisplayName().equalsIgnoreCase(displayName)) {
                    return c;
                }
            }
            return REAL; // Padrão
        }
    }

    private static final CurrencyManager INSTANCE = new CurrencyManager();
    private static final String PREF_KEY_CURRENCY = "moneyflow_currency";

    private ECurrency currentCurrency;
    private final List<Runnable> listeners = new ArrayList<>();

    private CurrencyManager() {
        Preferences prefs = Preferences.userNodeForPackage(CurrencyManager.class);
        String saved = prefs.get(PREF_KEY_CURRENCY, ECurrency.REAL.name());
        try {
            this.currentCurrency = ECurrency.valueOf(saved);
        } catch (IllegalArgumentException e) {
            this.currentCurrency = ECurrency.REAL;
        }
    }

    public static CurrencyManager getInstance() {
        return INSTANCE;
    }

    public ECurrency getCurrency() {
        return currentCurrency;
    }

    public void setCurrency(ECurrency currency) {
        if (currency == null || this.currentCurrency == currency) return;
        this.currentCurrency = currency;

        // Persiste a escolha
        Preferences prefs = Preferences.userNodeForPackage(CurrencyManager.class);
        prefs.put(PREF_KEY_CURRENCY, currency.name());

        // Notifica as views registradas
        notifyListeners();
    }

    public void addCurrencyChangeListener(Runnable listener) {
        listeners.add(listener);
    }

    public void removeCurrencyChangeListener(Runnable listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (Runnable listener : new ArrayList<>(listeners)) {
            listener.run();
        }
    }

    /**
     * Formata um valor em centavos de acordo com a moeda ativa.
     * @param cents Valor em centavos (ex: 125000 representa 1250.00)
     * @return String formatada (ex: "R$ 1.250,00" ou "€ 1.250,00")
     */
    public String format(int cents) {
        double valueInDouble = cents / 100.0;
        NumberFormat nf = NumberFormat.getCurrencyInstance(currentCurrency.getLocale());
        String formatted = nf.format(Math.abs(valueInDouble));
        if (cents < 0) {
            return "- " + formatted;
        }
        return formatted;
    }
}
