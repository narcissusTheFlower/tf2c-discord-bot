package com.tf2center.discordbot.utils;

import org.springframework.lang.NonNull;

public class TF2CStringUtils {
    public static String capitaliseFirstLetter(@NonNull String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
