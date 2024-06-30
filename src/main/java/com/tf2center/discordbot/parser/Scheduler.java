package com.tf2center.discordbot.parser;

import com.tf2center.discordbot.parser.discord.Publisher;
import com.tf2center.discordbot.parser.discord.embeds.EmbedsBuilder;
import com.tf2center.discordbot.parser.dto.MainPageObject;
import com.tf2center.discordbot.parser.html.MainPageParser;
import discord4j.core.spec.EmbedCreateSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@EnableScheduling
@Component
public class Scheduler {

    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    @Scheduled(fixedRate = 6_000)
    private static void runCycle() {
        Map<String, Collection<MainPageObject>> mainPageObjects = MainPageParser.getInstance().parse();
        logger.info("Scheduler updated with {} lobbies, {} substitution slots",
            mainPageObjects.get("Lobbies").size(),
            mainPageObjects.get("Subs").size());
        Map<String, Set<EmbedCreateSpec>> embeds = EmbedsBuilder.getInstance().build(mainPageObjects);
        Publisher.publish(embeds);
//          NotificationManager.manage();
    }


}