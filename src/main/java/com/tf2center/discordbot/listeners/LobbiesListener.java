package com.tf2center.discordbot.listeners;

import com.tf2center.discordbot.publish.publishables.LobbyPublishable;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LobbiesListener {

    @Autowired
    public LobbiesListener(GatewayDiscordClient client) {
        client.on(ChatInputInteractionEvent.class, this::recordNewLobbies).subscribe();
    }

    private Mono<Void> recordNewLobbies(ChatInputInteractionEvent event) {
        return Mono.just(event.getInteraction().getMessage().get().getEmbeds().get(0).getTitle())
                .flatMap(title -> LobbyPublishable.takeNewLobby(title.get()));
    }

}
