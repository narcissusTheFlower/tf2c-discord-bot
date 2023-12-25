package com.tf2center.discordbot.listeners;

import com.tf2center.discordbot.publish.publishables.LobbyPublishable;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class LobbiesListener {

    @Autowired
    public LobbiesListener(GatewayDiscordClient client) {
        client.on(MessageCreateEvent.class).flatMap(this::recordNewLobbies).subscribe();
    }

    private Mono<Void> recordNewLobbies(MessageCreateEvent message) {
        return Mono.just(message.getMessage())
                .flatMap(lobbyEmbed -> LobbyPublishable.extractInformation(
                        lobbyEmbed.getEmbeds().get(0).getTitle().get(),
                        lobbyEmbed.getId()
                ));
    }
}
