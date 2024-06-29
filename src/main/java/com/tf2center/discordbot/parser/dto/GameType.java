package com.tf2center.discordbot.parser.dto;

public enum GameType {

    SIXES("6v6"),
    HIGHLANDER("Highlander"),
    ULTIDUO("Ultiduo"),
    BBALL("BBall"),
    FOURVSFOUR("4v4");

    private final String value;

    GameType(String gameType) {
        this.value = gameType;
    }

}
