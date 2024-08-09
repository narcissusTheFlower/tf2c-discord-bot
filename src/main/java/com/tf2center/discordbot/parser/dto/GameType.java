package com.tf2center.discordbot.parser.dto;

public enum GameType {

    SIXES,
    HIGHLANDER,
    ULTIDUO,
    BBALL,
    FOURVSFOUR;

    public static GameType fromString(String jsonGameType) {
        return switch (jsonGameType) {
            case "Highlander" -> GameType.HIGHLANDER;
            case "6v6" -> GameType.SIXES;
            case "4v4" -> GameType.FOURVSFOUR;
            case "Ultiduo" -> GameType.ULTIDUO;
            case "BBall" -> GameType.BBALL;

            default -> throw new IllegalStateException("Unexpected value: " + jsonGameType);
        };
    }


}
