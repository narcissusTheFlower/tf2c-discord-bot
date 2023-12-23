package com.tf2center.discordbot.dto;

import com.tf2center.discordbot.dto.json.tf2clobby.TF2CSlotDTO;

import java.util.List;

public interface TF2CLobby {

    int getLobbyId();

    long getLeaderSteamId();

    String getRegion();

    boolean isVoiceCommunicationRequired();

    String getGameType();

    boolean isInReadyUpMode();

    String getMap();

    String getThumbnailUrl();

    int getPlayersInLobby();

    int getPlayersForGame();

    String getRestrictionsText();

    boolean isRegionLocked();

    boolean isBalancedLobby();

    boolean isAdvanced();

    TF2CSlotDTO[] getSlots();

    List<TF2CPlayerSlotDTO> getPlayerSlotList();

    boolean isOffclassingAllowed();

    String getConfig();

    String getServer();

    String getLeaderName();

    String getTimeOpened();
}
