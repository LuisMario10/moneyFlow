package com.moneyFlow.security;

import com.password4j.Password;

public class PasswordHasher {
    public static String hash(String password) {
        return Password.hash(password).withBcrypt().getResult();
    }

    public static boolean check(String password, String hashed) {
        return Password.check(password, hashed).withBcrypt();
    }
}