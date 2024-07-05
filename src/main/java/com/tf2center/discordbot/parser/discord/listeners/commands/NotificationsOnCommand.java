package com.tf2center.discordbot.parser.discord.listeners.commands;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class NotificationsOnCommand implements SlashCommand {

    private static final Logger logger = LoggerFactory.getLogger(NotificationsOnCommand.class);

    private GatewayDiscordClient gatewayDiscordClient;

    @Autowired
    public NotificationsOnCommand(GatewayDiscordClient gatewayDiscordClient) {
        this.gatewayDiscordClient = gatewayDiscordClient;
    }

    @Override
    public String getName() {
        return "notificationsOn";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        //Person asks for notifs
        //I take their discord id look it up in the main csv and see steamid
        //I take that pair of steamid and discordid and put in the subscribers csv
        String discordId = "";
        CSVController.appendToSubscribers(discordId);
        return event.reply()
                .withContent("Notifications are ON.")
                .withEphemeral(false);

    }
}
