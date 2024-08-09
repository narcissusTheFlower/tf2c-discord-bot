package com.tf2center.discordbot.parser.discord.listeners;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.component.LayoutComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class ButtonListener {

    private GatewayDiscordClient client;

    @Autowired
    public ButtonListener(GatewayDiscordClient client) {
        client.on(ButtonInteractionEvent.class, this::handle).subscribe();
    }

    public Mono<Void> handle(ButtonInteractionEvent event) {

        List<LayoutComponent> components = event.getMessage().get().getComponents();

        return Mono.empty();
    }

}
