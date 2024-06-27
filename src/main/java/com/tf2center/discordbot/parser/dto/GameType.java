package com.tf2center.discordbot.parser.dto;

public enum GameType {

    Sixes("6s"),
    Highlander("hl"),
    Ultiduo("Ultiduo"),
    BBall("BBall"),
    FourVSFour(""),
    ThreeVSThree("");

    private final String string;

    GameType(String gameType) {
        this.string = gameType;
    }

}
