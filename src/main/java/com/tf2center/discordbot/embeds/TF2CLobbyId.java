package com.tf2center.discordbot.embeds;

import org.jetbrains.annotations.NotNull;

public final class TF2CLobbyId extends Number implements Comparable<Integer> {

    private final int lobbyId;

    public TF2CLobbyId(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public int getLobbyId() {
        return lobbyId;
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
}
