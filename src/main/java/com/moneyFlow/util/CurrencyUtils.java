package com.moneyFlow.util;

public class CurrencyUtils {
    public static int parseValueToCents(String value) {
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
}
