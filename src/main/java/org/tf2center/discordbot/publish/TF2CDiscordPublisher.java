package org.tf2center.discordbot.publish;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@EnableScheduling
public class TF2CDiscordPublisher {

    private static final List<Publishable> publishables = Collections.synchronizedList(new ArrayList<Publishable>());

    //@Scheduled(fixedRate = 100_0, initialDelay = 300)
    private void executePublishers() {

        publishables.clear();
        publishables.addAll(PublishersRefresher.refresh());

        if (!publishables.isEmpty()) {
            publishables.forEach(Publishable::publish);
        }
    }
}
