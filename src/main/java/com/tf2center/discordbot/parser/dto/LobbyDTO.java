package com.tf2center.discordbot.parser.dto;


import com.tf2center.discordbot.parser.exceptions.TF2CDTOException;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public class LobbyDTO implements MainPageObject {

    private final int id;
    private final long leaderSteamId;
    private final String region;
    private final boolean vcRequired;
    private final GameType gameType;
    private final boolean isReady;
    private final String map;
    private final String thumbnailURL;
    private final int playersInLobby;
    private final int playersForGame;
    private final String restrictionsText;
    private final boolean regionLock;
    private final boolean balancedLobby;
    private final boolean advanced;
    private final Map<String, Collection<SlotDTO>> teams;
    //    private final List<TF2CPlayerSlotDTO> playerSlotList;
    private final boolean offclassingAllowed;
    private final String config;
    private final String server;
    private final String leaderName;
    private final String timeOpened;

    private LobbyDTO(Builder builder) {
        this.id = builder.id;
        this.leaderSteamId = builder.leaderSteamId;
        this.region = builder.region;
        this.vcRequired = builder.vcRequired;
        this.gameType = builder.gameType;
        this.isReady = builder.isReady;
        this.map = builder.map;
        this.thumbnailURL = builder.thumbnailURL;
        this.playersInLobby = builder.playersInLobby;
        this.playersForGame = builder.playersForGame;
        this.restrictionsText = builder.restrictionsText;
        this.regionLock = builder.regionLock;
        this.balancedLobby = builder.balancedLobby;
        this.advanced = builder.advanced;
        this.teams = builder.slots;
//        this.playerSlotList = builder.playerSlotList;
        this.offclassingAllowed = builder.offclassingAllowed;
        this.config = builder.config;
        this.server = builder.server;
        this.leaderName = builder.leaderName;
        this.timeOpened = builder.timeOpened;
    }

    public int getId() {
        return id;
    }

    public long getLeaderSteamId() {
        return leaderSteamId;
    }

    public String getRegion() {
        return region;
    }

    public boolean isVcRequired() {
        return vcRequired;
    }

    public GameType getGameType() {
        return gameType;
    }

    public boolean isReady() {
        return isReady;
    }

    public String getMap() {
        return map;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public int getPlayersInLobby() {
        return playersInLobby;
    }

    public int getPlayersForGame() {
        return playersForGame;
    }

    public String getRestrictionsText() {
        return restrictionsText;
    }

    public boolean isRegionLocked() {
        return regionLock;
    }

    public boolean isBalancedLobby() {
        return balancedLobby;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public Map<String, Collection<SlotDTO>> getTeams() {
        return teams;
    }

//    public List<TF2CPlayerSlotDTO> getPlayerSlotList() {
//        return playerSlotList;
//    }

    public boolean isOffclassingAllowed() {
        return offclassingAllowed;
    }

    public String getConfig() {
        return config;
    }

    public String getServer() {
        return server;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public String getTimeOpened() {
        return timeOpened;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LobbyDTO lobbyDTO)) {
            return false;
        }
        return (id == lobbyDTO.id) && (leaderSteamId == lobbyDTO.leaderSteamId) && (vcRequired == lobbyDTO.vcRequired) && (isReady == lobbyDTO.isReady) && (playersInLobby == lobbyDTO.playersInLobby) && (playersForGame == lobbyDTO.playersForGame) && (regionLock == lobbyDTO.regionLock) && (balancedLobby == lobbyDTO.balancedLobby) && (advanced == lobbyDTO.advanced) && (offclassingAllowed == lobbyDTO.offclassingAllowed) && Objects.equals(region, lobbyDTO.region) && (gameType == lobbyDTO.gameType) && Objects.equals(map, lobbyDTO.map) && Objects.equals(thumbnailURL, lobbyDTO.thumbnailURL) && Objects.equals(restrictionsText, lobbyDTO.restrictionsText) && Objects.equals(teams, lobbyDTO.teams) && Objects.equals(config, lobbyDTO.config) && Objects.equals(server, lobbyDTO.server) && Objects.equals(leaderName, lobbyDTO.leaderName) && Objects.equals(timeOpened, lobbyDTO.timeOpened);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, leaderSteamId, region, vcRequired, gameType, isReady, map, thumbnailURL, playersInLobby, playersForGame, restrictionsText, regionLock, balancedLobby, advanced, teams, offclassingAllowed, config, server, leaderName, timeOpened);
    }

    @Override
    public String toString() {
        return "LobbyDTO{" + '\n' +
            "id=" + id + '\n' +
            ", leaderSteamId=" + leaderSteamId + '\n' +
            ", region='" + region + '\'' + '\n' +
            ", vcRequired=" + vcRequired + '\n' +
            ", gameType=" + gameType + '\n' +
            ", isReady=" + isReady + '\n' +
            ", map='" + map + '\'' + '\n' +
            ", thumbnailURL='" + thumbnailURL + '\'' + '\n' +
            ", playersInLobby=" + playersInLobby + '\n' +
            ", playersForGame=" + playersForGame + '\n' +
            ", restrictionsText='" + restrictionsText + '\'' + '\n' +
            ", regionLock=" + regionLock + '\n' +
            ", balancedLobby=" + balancedLobby + '\n' +
            ", advanced=" + advanced + '\n' +
            ", teams=" + teams + '\n' +
//            ", playerSlotList=" + playerSlotList + '\n'+
            ", offclassingAllowed=" + offclassingAllowed + '\n' +
            ", config='" + config + '\'' + '\n' +
            ", server='" + server + '\'' + '\n' +
            ", leaderName='" + leaderName + '\'' + '\n' +
            ", timeOpened='" + timeOpened + '\'' + '\n' +
            '}';
    }

    public static class Builder {

        private int id;
        private long leaderSteamId;
        private String region;
        private boolean vcRequired;
        private GameType gameType;
        private boolean isReady;
        private String map;
        private String thumbnailURL;
        private int playersInLobby;
        private int playersForGame;
        private String restrictionsText;
        private boolean regionLock;
        private boolean balancedLobby;
        private boolean advanced;
        private Map<String, Collection<SlotDTO>> slots;
        //        private List<TF2CPlayerSlotDTO> playerSlotList;
        private boolean offclassingAllowed;
        private String config;
        private String server;
        private String leaderName;
        private String timeOpened;

        public Builder() {

        }

        public Builder id(int id) {
            if (id == 0) {
                throw new TF2CDTOException("Lobby id can't be 0.");
            }
            this.id = id;
            return this;
        }

        public Builder leaderSteamId(long leaderSteamId) {
            if (leaderSteamId == 0) {
                throw new TF2CDTOException("Lobby leader steamId can't be 0.");
            }
            this.leaderSteamId = leaderSteamId;
            return this;
        }

        public Builder vcRequired(boolean vcRequired) {
            this.vcRequired = vcRequired;
            return this;
        }

        public Builder region(String region) {
            this.region = Objects.requireNonNull(region, "Region can't be null. Bad HTML parsing");
            return this;
        }

        public Builder gameType(GameType gameType) {
            this.gameType = Objects.requireNonNull(gameType, "Game type can't be null. Bad HTML parsing");
            return this;
        }

        public Builder isReady(boolean isReady) {
            this.isReady = isReady;
            return this;
        }

        public Builder map(String map) {
            this.map = Objects.requireNonNull(map, "Map can't be null. Bad HTML parsing");
            return this;
        }

        public Builder thumbnailURL(String thumbnailURL) {
            this.thumbnailURL = Objects.requireNonNull(thumbnailURL, "Thumbnail URL can't be null. Bad HTML parsing");
            return this;
        }

        public Builder playersInLobby(int playersInLobby) {
            this.playersInLobby = playersInLobby;
            return this;
        }

        public Builder playersForGame(int playersForGame) {
            if (playersForGame == 0) {
                throw new TF2CDTOException("Players for a lobby can't be 0. Should at least be 4 for Ultiduo. Bad HTML parsing.");
            }
            this.playersForGame = playersForGame;
            return this;
        }

        public Builder restrictionsText(String restrictionsText) {
            this.restrictionsText = Objects.requireNonNull(restrictionsText, "Restrictions text can't be null. Bad HTML parsing");
            return this;
        }

        public Builder regionLock(boolean regionLock) {
            this.regionLock = regionLock;
            return this;
        }

        public Builder balancedLobby(boolean balancedLobby) {
            this.balancedLobby = balancedLobby;
            return this;
        }

        public Builder advanced(boolean advanced) {
            this.advanced = advanced;
            return this;
        }

        public Builder teams(Map<String, Collection<SlotDTO>> teams) {
            this.slots = Objects.requireNonNull(teams, "Map can't be null. Bad HTML parsing.");
            return this;
        }

//        public Builder playerSlotList(List<TF2CPlayerSlotDTO> playerSlotList) {
//            if (playerSlotList.isEmpty() || playerSlotList == null) {
//                throw new TF2CDTOException("Collection can't be empty or null.");
//            }
//            this.playerSlotList = playerSlotList;
//            return this;
//        }

        public Builder offclassingAllowed(boolean offclassingAllowed) {
            this.offclassingAllowed = offclassingAllowed;
            return this;
        }

        public Builder config(String config) {
            this.config = Objects.requireNonNull(config, "Config can't be null. Bad HTML parsing.");
            return this;
        }

        public Builder server(String server) {
            this.server = Objects.requireNonNull(server, "Server can't be null. Bad HTML parsing.");
            return this;
        }

        public Builder leaderName(String leaderName) {
            this.leaderName = Objects.requireNonNull(leaderName, "Leader name can't be null. Bad HTML parsing.");
            return this;
        }

        public Builder timeOpened(String timeOpened) {
            this.timeOpened = Objects.requireNonNull(timeOpened, "Time of the opening can't be null. Bad HTML parsing.");
            return this;
        }

        public LobbyDTO build() {
            return new LobbyDTO(this);
        }

    }
}
