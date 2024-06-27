package com.tf2center.discordbot.parser.dto;

import com.tf2center.discordbot.parser.exceptions.TF2CDTOException;

import java.util.Objects;

public class SubsDTO implements MainPageObject {

    private final int lobbyId;
    private final String region;
    private final boolean vcRequired;
    private final String map;
    private final String team;
    private final String tf2cTeamRole;
    private final String className;
    private final String joinLink;
    private final String gameType;
    private final boolean regionLock;
    private final boolean advanced;

    private SubsDTO(Builder builder) {
        this.lobbyId = builder.lobbyId;
        this.region = builder.region;
        this.vcRequired = builder.vcRequired;
        this.map = builder.map;
        this.team = builder.team;
        this.tf2cTeamRole = builder.roleInTeam;
        this.className = builder.className;
        this.joinLink = builder.joinLink;
        this.gameType = builder.gameType;
        this.regionLock = builder.regionLock;
        this.advanced = builder.advanced;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        SubsDTO subsDTO = (SubsDTO) object;
        return (lobbyId == subsDTO.lobbyId) &&
            (vcRequired == subsDTO.vcRequired) &&
            (regionLock == subsDTO.regionLock) &&
            (advanced == subsDTO.advanced) &&
            Objects.equals(region, subsDTO.region) &&
            Objects.equals(map, subsDTO.map) &&
            Objects.equals(team, subsDTO.team) &&
            Objects.equals(tf2cTeamRole, subsDTO.tf2cTeamRole) &&
            Objects.equals(className, subsDTO.className) &&
            Objects.equals(joinLink, subsDTO.joinLink) &&
            Objects.equals(gameType, subsDTO.gameType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lobbyId,
            region,
            vcRequired,
            map,
            team,
            tf2cTeamRole,
            className,
            joinLink,
            gameType,
            regionLock,
            advanced);
    }

    public static class Builder {
        private int lobbyId;
        private String region;
        private boolean vcRequired;
        private String map;
        private String team;
        private String roleInTeam;
        private String className;
        private String joinLink;
        private String gameType;
        private boolean regionLock;
        private boolean advanced;

        public Builder() {

        }

        public Builder lobbyId(int lobbyId) {
            if (lobbyId == 0) {
                throw new TF2CDTOException("Lobby id can't be 0.");
            }
            this.lobbyId = lobbyId;
            return this;
        }

        public Builder region(String region) {
            this.region = Objects.requireNonNull(region, "Region can't be null. Bad HTML parsing");
            return this;
        }

        public Builder vcRequired(boolean vcRequired) {
            this.vcRequired = vcRequired;
            return this;
        }

        public Builder map(String map) {
            this.map = Objects.requireNonNull(map, "Map can't be null. Bad HTML parsing");
            return this;
        }

        public Builder team(String team) {
            this.team = Objects.requireNonNull(team, "Team can't be null. Bad HTML parsing");
            return this;
        }

        public Builder roleInTeam(String roleInTeam) {
            this.roleInTeam = Objects.requireNonNull(roleInTeam, "Role in a team can't be null. Bad HTML parsing");
            return this;
        }

        public Builder className(String className) {
            this.className = Objects.requireNonNull(className, "Name of the tf2 class can't be null. Bad HTML parsing");
            return this;
        }

        public Builder joinLink(String joinLink) {
            this.joinLink = Objects.requireNonNull(joinLink, "Join link can't be null. Either bad HTML parsing or error wile building.");
            return this;
        }

        public Builder gameType(String gameType) {
            this.gameType = Objects.requireNonNull(gameType, "Game type (6s, highlander etc.) can't be null. Bad HTML parsing.");
            return this;
        }

        public Builder regionLock(boolean regionLock) {
            this.regionLock = regionLock;
            return this;
        }

        public Builder advanced(boolean advanced) {
            this.advanced = advanced;
            return this;
        }

        public SubsDTO build() {
            return new SubsDTO(this);
        }
    }
}
