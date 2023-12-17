package com.tf2center.discordbot.dto.json.tf2cpreview;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tf2center.discordbot.dto.teams.TF2CTeam;


public final class TF2CLobbyPreview {

    @JsonProperty("no")
    private int lobbyId;
    private long leaderSteamId;
    private String region;
    @JsonProperty("mumbleRequired")
    private boolean voiceCommunicationRequired;
    private String gameType;
    private boolean inReadyUpMode;
    private String map;
    private String thumbnailUrl;
    private int playersInLobby;
    private int playersForGame;
    private String restrictionsText;
    private boolean regionLock;
    @JsonProperty("useBalancer")
    private boolean balancedLobby;
    private boolean advanced;
    private TF2CSlot[] slots;
    @JsonIgnore
    private TF2CTeam teams = new TF2CTeam();

    public TF2CTeam getTeams() {
        return teams;
    }

    public void setTeams(TF2CTeam teams) {
        this.teams = teams;
    }

    public int getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public long getLeaderSteamId() {
        return leaderSteamId;
    }

    public void setLeaderSteamId(long leaderSteamId) {
        this.leaderSteamId = leaderSteamId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isVoiceCommunicationRequired() {
        return voiceCommunicationRequired;
    }

    public void setVoiceCommunicationRequired(boolean voiceCommunicationRequired) {
        this.voiceCommunicationRequired = voiceCommunicationRequired;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public boolean isInReadyUpMode() {
        return inReadyUpMode;
    }

    public void setInReadyUpMode(boolean inReadyUpMode) {
        this.inReadyUpMode = inReadyUpMode;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getPlayersInLobby() {
        return playersInLobby;
    }

    public void setPlayersInLobby(int playersInLobby) {
        this.playersInLobby = playersInLobby;
    }

    public int getPlayersForGame() {
        return playersForGame;
    }

    public void setPlayersForGame(int playersForGame) {
        this.playersForGame = playersForGame;
    }

    public String getRestrictionsText() {
        return restrictionsText;
    }

    public void setRestrictionsText(String restrictionsText) {
        this.restrictionsText = restrictionsText;
    }

    public boolean isRegionLocked() {
        return regionLock;
    }

    public void setRegionLock(boolean regionLock) {
        this.regionLock = regionLock;
    }

    public boolean isBalancedLobby() {
        return balancedLobby;
    }

    public void setBalancedLobby(boolean balancedLobby) {
        this.balancedLobby = balancedLobby;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }

    public TF2CSlot[] getSlots() {
        return slots;
    }

    public void setSlots(TF2CSlot[] slots) {
        this.slots = slots;
    }

}
