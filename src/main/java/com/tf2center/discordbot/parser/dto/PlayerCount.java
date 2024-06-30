package com.tf2center.discordbot.parser.dto;

public class PlayerCount implements MainPageObject {

    final int playerCount;

    private PlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public static PlayerCount of(int playerCount) {
        return new PlayerCount(playerCount);
    }

    public int getPlayerCount() {
        return playerCount;
    }
}
