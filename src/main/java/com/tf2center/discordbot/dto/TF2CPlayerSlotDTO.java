package com.tf2center.discordbot.dto;

public class TF2CPlayerSlotDTO {

    private String playerName;
    private String steamId;
    private String state;
    private String tf2Class;

    public TF2CPlayerSlotDTO(String playerName, String steamId, String state) {
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

    public String getTf2Class() {
        return tf2Class;
    }

    public void setTf2Class(String tf2Class) {
        this.tf2Class = tf2Class;
    }
}
