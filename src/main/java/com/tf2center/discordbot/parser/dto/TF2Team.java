package com.tf2center.discordbot.parser.dto;

public enum TF2Team {

    RED, BLU;

    public static TF2Team fromString(String source) {
        if (source.equals("blue")) {
            return TF2Team.BLU;
        }
        return TF2Team.RED;
    }

}
