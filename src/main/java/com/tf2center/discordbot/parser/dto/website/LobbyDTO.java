package com.tf2center.discordbot.parser.dto.website;

import com.tf2center.discordbot.dto.TF2CPlayerSlotDTO;
import com.tf2center.discordbot.dto.json.tf2clobby.TF2CSlotDTO;
import com.tf2center.discordbot.parser.exceptions.TF2CDTOException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LobbyDTO implements MainPageObject {

    private final int id;
    private final long leaderSteamId;
    private final String region;
    private final boolean vcRequired;
    private final String gameType;
    private final boolean isReady;
    private final String map;
    private final String thumbnailURL;
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
        this.slots = builder.slots;
        this.playerSlotList = builder.playerSlotList;
        this.offclassingAllowed = builder.offclassingAllowed;
        this.config = builder.config;
        this.server = builder.server;
        this.leaderName = builder.leaderName;
        this.timeOpened = builder.timeOpened;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        LobbyDTO lobbyDTO = (LobbyDTO) object;
        return (id == lobbyDTO.id) &&
            (leaderSteamId == lobbyDTO.leaderSteamId) &&
            (vcRequired == lobbyDTO.vcRequired) &&
            (isReady == lobbyDTO.isReady) &&
            (playersInLobby == lobbyDTO.playersInLobby) &&
            (playersForGame == lobbyDTO.playersForGame) &&
            (regionLock == lobbyDTO.regionLock) &&
            (balancedLobby == lobbyDTO.balancedLobby) &&
            (advanced == lobbyDTO.advanced) &&
            (offclassingAllowed == lobbyDTO.offclassingAllowed) &&
            Objects.equals(region, lobbyDTO.region) &&
            Objects.equals(gameType, lobbyDTO.gameType) &&
            Objects.equals(map, lobbyDTO.map) &&
            Objects.equals(thumbnailURL, lobbyDTO.thumbnailURL) &&
            Objects.equals(restrictionsText, lobbyDTO.restrictionsText) &&
            Arrays.equals(slots, lobbyDTO.slots) &&
            Objects.equals(playerSlotList, lobbyDTO.playerSlotList) &&
            Objects.equals(config, lobbyDTO.config) &&
            Objects.equals(server, lobbyDTO.server) &&
            Objects.equals(leaderName, lobbyDTO.leaderName) &&
            Objects.equals(timeOpened, lobbyDTO.timeOpened);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id,
            leaderSteamId,
            region,
            vcRequired,
            gameType,
            isReady,
            map,
            thumbnailURL,
            playersInLobby,
            playersForGame,
            restrictionsText,
            regionLock,
            balancedLobby,
            advanced,
            playerSlotList,
            offclassingAllowed,
            config,
            server,
            leaderName,
            timeOpened);
        result = 31 * result + Arrays.hashCode(slots);
        return result;
    }

    @Override
    public String toString() {
        return "LobbyDTO{" +
            "id=" + id +
            ", leaderSteamId=" + leaderSteamId +
            ", region='" + region + '\'' +
            ", vcRequired=" + vcRequired +
            ", gameType='" + gameType + '\'' +
            ", isReady=" + isReady +
            ", map='" + map + '\'' +
            ", thumbnailURL='" + thumbnailURL + '\'' +
            ", playersInLobby=" + playersInLobby +
            ", playersForGame=" + playersForGame +
            ", restrictionsText='" + restrictionsText + '\'' +
            ", regionLock=" + regionLock +
            ", balancedLobby=" + balancedLobby +
            ", advanced=" + advanced +
            ", slots=" + Arrays.toString(slots) +
            ", playerSlotList=" + playerSlotList +
            ", offclassingAllowed=" + offclassingAllowed +
            ", config='" + config + '\'' +
            ", server='" + server + '\'' +
            ", leaderName='" + leaderName + '\'' +
            ", timeOpened='" + timeOpened + '\'' +
            '}';
    }

    public static class Builder {

        private int id;
        private long leaderSteamId;
        private String region;
        private boolean vcRequired;
        private String gameType;
        private boolean isReady;
        private String map;
        private String thumbnailURL;
        private int playersInLobby;
        private int playersForGame;
        private String restrictionsText;
        private boolean regionLock;
        private boolean balancedLobby;
        private boolean advanced;
        private TF2CSlotDTO[] slots;
        private List<TF2CPlayerSlotDTO> playerSlotList;
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

        public Builder gameType(String gameType) {
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

        public Builder slots(TF2CSlotDTO[] slots) {
            this.slots = Objects.requireNonNull(slots, "Array can't be null. Badly built array, bad HTML parsing.");
            return this;
        }

        public Builder playerSlotList(List<TF2CPlayerSlotDTO> playerSlotList) {
            if (playerSlotList.isEmpty() || playerSlotList == null) {
                throw new TF2CDTOException("Collection can't be empty or null.");
            }
            this.playerSlotList = playerSlotList;
            return this;
        }

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
