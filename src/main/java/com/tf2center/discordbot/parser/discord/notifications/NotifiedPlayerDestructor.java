package com.tf2center.discordbot.parser.discord.notifications;

import java.util.Objects;

public class NotifiedPlayerDestructor {
    private final String steamId;
    private long epochSeconds;

    private NotifiedPlayerDestructor(String steamId, long epochSeconds) {
        this.steamId = steamId;
        this.epochSeconds = epochSeconds;
    }

    private NotifiedPlayerDestructor(String steamId) {
        this.steamId = steamId;
    }

    public static NotifiedPlayerDestructor of(String steamId, long epochSeconds) {
        return new NotifiedPlayerDestructor(steamId, epochSeconds);
    }

    public static NotifiedPlayerDestructor of(String steamId) {
        return new NotifiedPlayerDestructor(steamId);
    }

    public String getSteamId() {
        return steamId;
    }

    public long getEpochSeconds() {
        return epochSeconds;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        NotifiedPlayerDestructor that = (NotifiedPlayerDestructor) object;
        return Objects.equals(getSteamId(), that.getSteamId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSteamId());
    }
}
