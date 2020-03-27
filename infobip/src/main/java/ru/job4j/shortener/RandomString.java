package ru.job4j.shortener;

import java.security.SecureRandom;

public class RandomString {
    public static String genString(int len) {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();
        StringBuilder result = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            result.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return result.toString();
    }
}
