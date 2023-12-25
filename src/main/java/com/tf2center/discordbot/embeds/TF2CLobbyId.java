package com.tf2center.discordbot.embeds;

import org.jetbrains.annotations.NotNull;

public final class TF2CLobbyId extends Number implements Comparable<Integer> {

    private final int lobbyId;

    private TF2CLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public static TF2CLobbyId of(int lobbyId) {
        return new TF2CLobbyId(lobbyId);
    }

    @Override
    public int intValue() {
        return lobbyId;
    }

    @Override
    public long longValue() {
        return (long) lobbyId;
    }

    @Override
    public float floatValue() {
        return (float) lobbyId;
    }

    @Override
    public double doubleValue() {
        return (double) lobbyId;
    }

    @Override
    public int compareTo(@NotNull Integer anotherInteger) {
        return Integer.compare(this.lobbyId, anotherInteger);
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
