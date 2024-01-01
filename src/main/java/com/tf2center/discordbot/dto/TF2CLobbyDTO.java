package com.tf2center.discordbot.dto;

import com.tf2center.discordbot.dto.json.tf2clobby.TF2CLobbyPreviewDTO;
import com.tf2center.discordbot.dto.json.tf2clobby.TF2CSlotDTO;

import java.util.List;

public class TF2CLobbyDTO implements TF2CLobby {

    private final int lobbyId;
    private final long leaderSteamId;
    private final String region;
    private final boolean voiceCommunicationRequired;
    private final String gameType;
    private final boolean inReadyUpMode;
    private final String map;
    private final String thumbnailUrl;
    private final int playersInLobby;
    private final int playersForGame;
    private final String restrictionsText;
    private final boolean regionLock;
    private final boolean balancedLobby;
    private final boolean advanced;
    private final TF2CSlotDTO[] slots;
    private final List<TF2CPlayerSlotDTO> playerSlotList;
    private final boolean offclassingAllowed;
    private final String config;
    private final String server;
    private final String leaderName;
    private final String timeOpened;

    public TF2CLobbyDTO(TF2CLobbyPreviewDTO preview) {
        this.lobbyId = preview.getLobbyId();
        this.leaderSteamId = preview.getLeaderSteamId();
        this.region = preview.getRegion();
        this.voiceCommunicationRequired = preview.isVoiceCommunicationRequired();
        this.gameType = preview.getGameType();
        this.inReadyUpMode = preview.isInReadyUpMode();
        this.map = preview.getMap();
        this.thumbnailUrl = preview.getThumbnailUrl();
        this.playersInLobby = preview.getPlayersInLobby();
        this.playersForGame = preview.getPlayersForGame();
        this.restrictionsText = preview.getRestrictionsText();
        this.regionLock = preview.isRegionLocked();
        this.balancedLobby = preview.isBalancedLobby();
        this.advanced = preview.isAdvanced();
        this.slots = preview.getSlots();
        this.playerSlotList = preview.getPlayerSlotList();
        this.offclassingAllowed = preview.isOffclassingAllowed();
        this.config = preview.getConfig();
        this.server = preview.getServer();
        this.leaderName = preview.getLeaderName();
        this.timeOpened = preview.getTimeOpened();
    }

    @Override
    public int getLobbyId() {
        return lobbyId;
    }

    @Override
    public long getLeaderSteamId() {
        return leaderSteamId;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public boolean isVoiceCommunicationRequired() {
        return voiceCommunicationRequired;
    }

    @Override
    public String getGameType() {
        return gameType;
    }

    @Override
    public boolean isInReadyUpMode() {
        return inReadyUpMode;
    }

    @Override
    public String getMap() {
        return map;
    }

    @Override
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public int getPlayersInLobby() {
        return playersInLobby;
    }

    @Override
    public int getPlayersForGame() {
        return playersForGame;
    }

    @Override
    public String getRestrictionsText() {
        return restrictionsText;
    }

    @Override
    public boolean isRegionLocked() {
        return regionLock;
    }

    @Override
    public boolean isBalancedLobby() {
        return balancedLobby;
    }

    @Override
    public boolean isAdvanced() {
        return advanced;
    }

    @Override
    public TF2CSlotDTO[] getSlots() {
        return slots;
    }

    @Override
    public List<TF2CPlayerSlotDTO> getPlayerSlotList() {
        return playerSlotList;
    }

    @Override
    public boolean isOffclassingAllowed() {
        return offclassingAllowed;
    }

    @Override
    public String getConfig() {
        return config;
    }

    @Override
    public String getServer() {
        return server;
    }

    @Override
    public String getLeaderName() {
        return leaderName;
    }

    @Override
    public String getTimeOpened() {
        return timeOpened;
    }

    @Override
    public int hashCode() {
        return this.lobbyId + (int) System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        TF2CLobbyDTO lobbyDTO = (TF2CLobbyDTO) obj;
        return this.getLobbyId() == lobbyDTO.getLobbyId() &&
                this.getPlayerSlotList().equals(lobbyDTO.getPlayerSlotList()) &&
                this.isInReadyUpMode() == lobbyDTO.isInReadyUpMode();
    }
}
