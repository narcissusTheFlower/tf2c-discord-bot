package com.tf2center.discordbot.parser.discord.notifications;

import java.util.Objects;
import java.util.Set;

public class NotifiedPlayerDestructor{
    private String steamId;
    public String getSteamId() {
        return steamId;
    }

    NotifiedPlayerDestructor(String steamId, Set<NotifiedPlayerDestructor> ownList){
        this.steamId = steamId;

        Thread sleep = new Thread(() -> {
            try {
                //Defines for how long a person will not be getting another notification
                Thread.sleep(15_000);
                ownList.remove(this);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        sleep.start();
    }

    NotifiedPlayerDestructor(String steamId) {
        this.steamId = steamId;
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
