package com.tf2center.discordbot.dto;


import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CSlot;

public class TF2CLobby<T> {

    private int lobbyId;
    private long leaderSteamId;
    private String region;
    private boolean voiceCommunicationRequired;
    private String gameType;
    private boolean inReadyUpMode;
    private String map;
    private String thumbnailUrl;
    private int playersInLobby;
    private int playersForGame;
    private String restrictionsText;
    private boolean regionLock;
    private boolean balancedLobby;
    private boolean advanced;
    private TF2CSlot[] slots;


}
