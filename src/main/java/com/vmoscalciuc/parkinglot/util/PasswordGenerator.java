package com.vmoscalciuc.parkinglot.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class PasswordGenerator {
    private static final int MINPASSWORDLENGTH = 5;
    private static final int MAXPASSWORDLENGTH = 10;
    private static final char[] SYMBOLS = "@$!%*?&#^()_+-;.,".toCharArray();
    private static final char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] NUMBERS = "0123456789".toCharArray();
    private static final char[] ALL_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz@$!%*?&#^()_+-;.,0123456789".toCharArray();
    private static final Random rand = new SecureRandom();

    public String generateRandomPassword() {
        var len = rand.nextInt((MAXPASSWORDLENGTH - MINPASSWORDLENGTH) + 1) + MINPASSWORDLENGTH;

        var password = new char[len];

        password[0] = LOWERCASE[rand.nextInt(LOWERCASE.length)];
        password[1] = UPPERCASE[rand.nextInt(UPPERCASE.length)];
        password[2] = NUMBERS[rand.nextInt(NUMBERS.length)];
        password[3] = SYMBOLS[rand.nextInt(SYMBOLS.length)];

        for (var i = 4; i < len; i++) {
            password[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
        }

        for (int i = 0; i < password.length; i++) {
            var randomPosition = rand.nextInt(password.length);
            var temp = password[i];
            password[i] = password[randomPosition];
            password[randomPosition] = temp;
        }
        return new String(password);
    }
}
