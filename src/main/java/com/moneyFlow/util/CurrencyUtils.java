package com.moneyFlow.util;

public class CurrencyUtils {
    public static double currencyConverter(int cents) {
        return (double) cents / 100;
    }
}
