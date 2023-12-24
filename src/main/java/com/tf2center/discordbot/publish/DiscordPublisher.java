package com.tf2center.discordbot.publish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component()
@Scope("singleton")
@EnableScheduling
public class DiscordPublisher {

    private final static Set<Integer> activeLobbiesIDs = Collections.synchronizedSet(new LinkedHashSet<>());

    private final List<Publishable> publishables;

    @Autowired
    public DiscordPublisher(List<Publishable> publishables) {
        this.publishables = publishables;
    }

    @Scheduled(fixedRate = 10_000, initialDelay = 2300)
    private void executePublishers() {
            publishables.forEach(Publishable::publish);
    }
}
