package com.tf2center.discordbot.parser.dto;

import java.util.Objects;
import java.util.Optional;

public class SlotDTO {

    private final Optional<String> playerName;
    private final Optional<String> steamId;
    private final boolean isEmpty;
    private final Optional<String> tf2Class;

    private SlotDTO(String playerName, String steamId, boolean isEmpty, String tf2Class) {
        this.playerName = Optional.of(playerName);
        this.steamId = Optional.of(steamId);
        this.isEmpty = isEmpty;
        this.tf2Class = Optional.of(tf2Class);
    }

    private SlotDTO(boolean isEmpty) {
        this.playerName = Optional.empty();
        this.steamId = Optional.empty();
        this.isEmpty = isEmpty;
        this.tf2Class = Optional.empty();
    }

    public static SlotDTO of(String playerName, String steamId, boolean isEmpty, String tf2Class) {
        return new SlotDTO(playerName, steamId, isEmpty, tf2Class);
    }

    public static SlotDTO of(boolean isEmpty) {
        return new SlotDTO(isEmpty);
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

    public Optional<String> getTf2Class() {
        return tf2Class;
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
