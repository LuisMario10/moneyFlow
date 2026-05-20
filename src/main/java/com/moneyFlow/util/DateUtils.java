package com.moneyFlow.util;

import java.time.LocalDateTime;

public class DateUtils {
    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.toString();
    }
}
