package com.tf2center.discordbot.publish.targets;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class LobbiesTextChannel {

    //If a lobby is present on the main page - it's alive

    private final static Set<?> aliveEmbeds = Collections.synchronizedSet(new LinkedHashSet<>());

    public static Set getAliveEmbeds() {
        return aliveEmbeds;
    }

}
