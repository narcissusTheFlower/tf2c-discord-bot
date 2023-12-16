package com.tf2center.discordbot.dto.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CSlotRestriction;

public class TF2CSubstituteSlot {

    @JsonProperty("lobbyNo")
    private int lobbyId;
    private String region;
    @JsonProperty("mumbleRequired")
    private boolean voiceCommunicationRequired;
    private String map;
    private String team;
    @JsonProperty("tf2Class")
    private String tf2cTeamRole;
    private String className;
    private String joinLink;
    private String gameType;
    private boolean regionLock;
    private boolean advanced;

    //This field is present on the website, but I've never seen a sub that had requirements
    @JsonIgnore
    private TF2CSlotRestriction restrictions;

    public int getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
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

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTf2cTeamRole() {
        return tf2cTeamRole;
    }

    public void setTf2cTeamRole(String tf2cTeamRole) {
        this.tf2cTeamRole = tf2cTeamRole;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getJoinLink() {
        return joinLink;
    }

    public void setJoinLink(String joinLink) {
        this.joinLink = joinLink;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public boolean isRegionLock() {
        return regionLock;
    }

    public void setRegionLock(boolean regionLock) {
        this.regionLock = regionLock;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean advanced) {
        this.advanced = advanced;
    }

    public TF2CSlotRestriction getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(TF2CSlotRestriction restrictions) {
        this.restrictions = restrictions;
    }
}
