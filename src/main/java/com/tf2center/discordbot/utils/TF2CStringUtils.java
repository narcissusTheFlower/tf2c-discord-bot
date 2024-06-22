package com.tf2center.discordbot.utils;

public class TF2CStringUtils {

    /**
     * @param src Lobby #1270546
     * @return 1270546
     */
    public static int extractLobbyId(String src) {
        return Integer.parseInt(
            String.valueOf(extractDigits(src)).substring(0, 7)
        );
    }
    public static int extractDigits(String src) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            char c = src.charAt(i);
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }
        return Integer.parseInt(builder.toString());
    }
}
