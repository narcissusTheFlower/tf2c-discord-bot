package com.tf2center.discordbot.utils;

public class TF2CStringUtils {

    public static int extractDigits(String src) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }

        return Integer.parseInt(builder.toString());
    }
}
