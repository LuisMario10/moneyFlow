package com.moneyFlow.util;

public class CurrencyUtils {
    public static double currencyConverter(int cents) {
        return (double) cents / 100;
    }

    public static int parseValueToCents(String value) {
        try {
            String cleanString = value.replaceAll("[^0-9,-]", "").replace(",", ".");
            if (cleanString.isEmpty()) return 0;
            double amount = Double.parseDouble(cleanString);
            return (int) Math.round(amount * 100);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
