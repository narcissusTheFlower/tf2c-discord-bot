package com.tf2center.discordbot.parser.discord.notifications;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Wrapper class
 */
@Component
@Scope("singleton")
public class NotifiedPlayersHolder {

    private final Set<NotifiedPlayerDestructor> notifiedPlayers = Collections.synchronizedSet(new LinkedHashSet<>());

    public Set<NotifiedPlayerDestructor> getNotifiedPlayers() {
        return notifiedPlayers;
    }

}
