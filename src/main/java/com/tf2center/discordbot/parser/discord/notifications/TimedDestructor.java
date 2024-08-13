package com.tf2center.discordbot.parser.discord.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

/**
 * Class responsible for managing notification pauses so that a person is not getting spammed. <br>
 * If current epoch seconds are later/higher than those in the DTO instance, the instance is removed from the "notified players" collection
 */
@Component
@EnableScheduling
@Scope("singleton")
public class TimedDestructor {

    private NotifiedPlayersHolder holder;

    @Autowired
    public TimedDestructor(NotifiedPlayersHolder holder) {
        this.holder = holder;
    }

    @Scheduled(fixedRate = 1_000)
    private void destructElements() {
        Set<NotifiedPlayerDestructor> notifiedPlayers = holder.getNotifiedPlayers();
        if (notifiedPlayers.isEmpty()) {
            return;
        }

        //Remove outdated players
        notifiedPlayers
            .stream()
            .filter(
                player -> player.getEpochSeconds() > Instant.now().getEpochSecond()
            )
            .forEach(notifiedPlayers::remove);
    }

}
