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

                Thread.sleep(30_000);
//                Exception in thread "Thread-6" java.lang.IllegalMonitorStateException: current thread is not owner
//                at java.base/java.lang.Object.wait(Native Method)
//                at com.tf2center.discordbot.parser.discord.notifications.NotifiedPlayerDestructor.lambda$new$0(NotifiedPlayerDestructor.java:18)
//                at java.base/java.lang.Thread.run(Thread.java:840)
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