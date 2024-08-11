package com.tf2center.discordbot.parser.discord.listeners.commands;

import com.tf2center.discordbot.parser.discord.notifications.csv.CSVActions;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class NotificationsOffCommand implements SlashCommand {

    private static final Logger logger = LoggerFactory.getLogger(NotificationsOffCommand.class);

    private GatewayDiscordClient gatewayDiscordClient;

    @Autowired
    public NotificationsOffCommand(GatewayDiscordClient gatewayDiscordClient) {
        this.gatewayDiscordClient = gatewayDiscordClient;
    }

    @Override
    public String getName() {
        return "notifications_off";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        logger.info("Person trying to UNsubscribe: " + event.getInteraction().getUser().getGlobalName().get());
        CSVActions.deleteFromSubscribers(event.getInteraction().getUser().getId());
        logger.info("Person successfully UNsubbed");
        return event.reply()
            .withContent("Notifications are OFF.")
                .withEphemeral(false);
    }
}
