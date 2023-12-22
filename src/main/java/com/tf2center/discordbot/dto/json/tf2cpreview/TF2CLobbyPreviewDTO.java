package com.tf2center.discordbot.dto.json.tf2cpreview;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tf2center.discordbot.dto.TF2CPlayerSlotDTO;

import java.util.List;


public final class TF2CLobbyPreviewDTO {

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
    private TF2CSlotDTO[] slots;
    @JsonIgnore
    private List<TF2CPlayerSlotDTO> playerSlotList;
    @JsonIgnore
    private boolean offclassingAllowed;
    @JsonIgnore
    private String config;
    @JsonIgnore
    private String server;
    @JsonIgnore
    private String leaderName;

    @JsonIgnore
    private String timeOpened;

    public String getTimeOpened() {
        return timeOpened;
    }

    public void setTimeOpened(String timeOpened) {
        this.timeOpened = timeOpened;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public boolean isOffclassingAllowed() {
        return offclassingAllowed;
    }

    public void setOffclassingAllowed(boolean offclassingAllowed) {
        this.offclassingAllowed = offclassingAllowed;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public List<TF2CPlayerSlotDTO> getPlayerSlotList() {
        return playerSlotList;
    }

    public void setPlayerSlotList(List<TF2CPlayerSlotDTO> playerSlotList) {

        this.playerSlotList = playerSlotList;
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

    public TF2CSlotDTO[] getSlots() {
        return slots;
    }

    public void setSlots(TF2CSlotDTO[] slots) {
        this.slots = slots;
    }

}
