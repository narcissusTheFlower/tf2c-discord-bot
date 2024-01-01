package com.tf2center.discordbot.dto;

import org.jetbrains.annotations.NotNull;

public final class TF2CLobbyIdDTO implements Comparable<TF2CLobbyIdDTO> {

    private final int lobbyId;

    private TF2CLobbyIdDTO(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public static TF2CLobbyIdDTO of(int lobbyId) {
        return new TF2CLobbyIdDTO(lobbyId);
    }

    public int intValue() {
        return lobbyId;
    }

    @Override
    public int compareTo(@NotNull TF2CLobbyIdDTO anotherLobbyId) {
        return Integer.compare(this.lobbyId, anotherLobbyId.intValue());
    }

    @Override
    public int hashCode() {
        return this.lobbyId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TF2CLobbyIdDTO) {
            return this.lobbyId == ((TF2CLobbyIdDTO) obj).lobbyId;
        }
        return false;
    }
}
