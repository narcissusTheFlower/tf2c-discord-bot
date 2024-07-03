package com.tf2center.discordbot.parser.dto;

import com.tf2center.discordbot.parser.dto.tf2classes.TF2Class;

import java.util.Objects;
import java.util.Optional;

public class SlotDTO {

    private Optional<String> playerName;
    private Optional<String> steamId;
    private boolean isEmpty;
    private Optional<TF2Class> tf2Class;
    private Optional<TF2Team> team;

    private SlotDTO(String playerName, String steamId, boolean isEmpty, TF2Team team) {
        this.playerName = Optional.of(playerName);
        this.steamId = Optional.of(steamId);
        this.isEmpty = isEmpty;
        this.tf2Class = Optional.empty();
        this.team = Optional.of(team);
    }

    private SlotDTO(boolean isEmpty, TF2Team team) {
        this.playerName = Optional.empty();
        this.steamId = Optional.empty();
        this.isEmpty = isEmpty;
        this.tf2Class = Optional.empty();
        this.team = Optional.of(team);
    }

    public static SlotDTO of(String playerName, String steamId, boolean isEmpty, TF2Team team) {
        return new SlotDTO(playerName, steamId, isEmpty, team);
    }

    public static SlotDTO of(boolean isEmpty, TF2Team team) {
        return new SlotDTO(isEmpty, team);
    }

    public Optional<String> getPlayerName() {
        return playerName;
    }

    public Optional<String> getSteamId() {
        return steamId;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public Optional<TF2Class> getTf2Class() {
        return tf2Class;
    }

    public Optional<TF2Team> getTeam() {
        return team;
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        SlotDTO slotDTO = (SlotDTO) object;
        return Objects.equals(playerName, slotDTO.playerName) && Objects.equals(steamId, slotDTO.steamId) && Objects.equals(isEmpty, slotDTO.isEmpty) && Objects.equals(tf2Class, slotDTO.tf2Class);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, steamId, isEmpty, tf2Class);
    }
}
