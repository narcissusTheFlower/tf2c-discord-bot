package com.tf2center.discordbot.parser.dto;

public enum TF2Team {

    RED("red"),
    BLU("blu");

    private final String value;

    TF2Team(String value) {
        this.value = value;
    }

    public static TF2Team fromString(String source) {
        if (source.equals("blue")) {
            return TF2Team.BLU;
        }
        return TF2Team.RED;
    }

    public String getTeamString() {
        return value;
    }
}
