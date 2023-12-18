package com.tf2center.discordbot.dto.teams;

public class TF2CPlayerSlot {

    private String playerName;
    private String steamId;
    private String state;


    public TF2CPlayerSlot(String playerName, String steamId, String state) {
        this.playerName = playerName;
        this.steamId = steamId;
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }
}
