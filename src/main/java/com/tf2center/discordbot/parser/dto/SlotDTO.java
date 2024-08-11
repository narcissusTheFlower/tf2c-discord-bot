package com.tf2center.discordbot.parser.dto;

import com.tf2center.discordbot.parser.dto.tf2classes.TF2Class;
import com.tf2center.discordbot.parser.exceptions.TF2CDTOException;

import java.util.Objects;
import java.util.Optional;

public class SlotDTO {

    private Optional<String> playerName;
    private Optional<String> steamId;
    private boolean isEmpty;
    private Optional<TF2Class> tf2Class;
    private Optional<TF2Team> team;
    private Optional<Boolean> personIsReady;

    private SlotDTO(String playerName, String steamId, boolean isEmpty, TF2Team team, boolean personIsReady) {
        this.playerName = Optional.of(playerName);
        this.steamId = Optional.of(steamId);
        this.isEmpty = isEmpty;
        this.tf2Class = Optional.empty();
        this.team = Optional.of(team);
        this.personIsReady = Optional.of(personIsReady);
    }

    private SlotDTO(boolean isEmpty, TF2Team team) {
        this.playerName = Optional.empty();
        this.steamId = Optional.empty();
        this.isEmpty = isEmpty;
        this.tf2Class = Optional.empty();
        this.team = Optional.of(team);
    }

    public static SlotDTO of(String playerName, String steamId, boolean isEmpty, TF2Team team, boolean personIsReady) {
        return new SlotDTO(playerName, steamId, isEmpty, team, personIsReady);
    }

    public static SlotDTO of(boolean isEmpty, TF2Team team) {
        return new SlotDTO(isEmpty, team);
    }

    public String getPlayerName() {
        return playerName.orElse("");
    }

    public String getSteamId() {
        if (steamId.isPresent()) {
            return steamId.get().substring(9);
        } else {
            throw new TF2CDTOException("Missing steamId.");
        }
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public TF2Class getTf2Class() {
        return tf2Class.get();
    }

    public TF2Team getTeam() {
        return team.get();
    }

    public boolean getPersonIsReady() {
        //Somehow optional is null idk, not fixing it rn, too tired
        if (personIsReady == null) {
            return !isEmpty();
        } else {
            return personIsReady.get();
        }
    }

    public void setTf2Class(Optional<TF2Class> tf2Class) {
        this.tf2Class = tf2Class;
    }

    public void setPlayerName(Optional<String> playerName) {
        this.playerName = playerName;
    }

    public void setSteamId(Optional<String> steamId) {
        this.steamId = steamId;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public void setTeam(Optional<TF2Team> team) {
        this.team = team;
    }

    public void setPersonIsReady(Optional<Boolean> personIsReady) {
        this.personIsReady = personIsReady;
    }

    @Override
    public String toString() {
        return "SlotDTO{" +
            "playerName=" + playerName +
            ", steamId=" + steamId +
            ", isEmpty=" + isEmpty +
            ", tf2Class=" + tf2Class +
            ", team=" + team +
            ", personIsReady=" + personIsReady +
            '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        SlotDTO slotDTO = (SlotDTO) object;
        return Objects.equals(playerName, slotDTO.playerName) && Objects.equals(steamId, slotDTO.steamId) && Objects.equals(isEmpty, slotDTO.isEmpty) && Objects.equals(tf2Class, slotDTO.tf2Class) && Objects.equals(personIsReady, slotDTO.personIsReady);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, steamId, isEmpty, tf2Class, personIsReady);
    }
}
