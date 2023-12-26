package com.tf2center.discordbot.embeds;

import org.jetbrains.annotations.NotNull;

public final class TF2CLobbyId implements Comparable<TF2CLobbyId> {

    private final int lobbyId;

    private TF2CLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public static TF2CLobbyId of(int lobbyId) {
        return new TF2CLobbyId(lobbyId);
    }

    public int intValue() {
        return lobbyId;
    }

    @Override
    public int compareTo(@NotNull TF2CLobbyId anotherLobbyId) {
        return Integer.compare(this.lobbyId, anotherLobbyId.intValue());
    }

    @Override
    public int hashCode() {
        return this.lobbyId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TF2CLobbyId) {
            return this.lobbyId == ((TF2CLobbyId) obj).lobbyId;
        }
        return false;
    }
}
