package com.tf2center.discordbot.parser.discord.notifications;

import java.util.Objects;

public class Subscriber {

    private String steamId;
    private String discordId;
    private String discordChatId;

    private Subscriber(String steamId, String discordId, String discordChatId) {
        this.steamId = steamId;
        this.discordId = discordId;
        this.discordChatId = discordChatId;
    }

    private Subscriber(String steamId) {
        this.steamId = steamId;
        this.discordId = "";
        this.discordChatId = "";
    }

    public static Subscriber of(String steamId, String discordId, String discordChatId) {
        return new Subscriber(steamId, discordId, discordChatId);
    }

    public static Subscriber of(String steamId) {
        return new Subscriber(steamId);
    }


    public String getSteamId() {
        return steamId;
    }

    public String getDiscordId() {
        return discordId;
    }

    public String getDiscordChatId() {
        return discordChatId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscriber that)) {
            return false;
        }

        return Objects.equals(steamId, that.steamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(steamId);
    }
}
