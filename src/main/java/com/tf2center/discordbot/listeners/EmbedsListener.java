package com.tf2center.discordbot.listeners;

import com.tf2center.discordbot.publish.publishables.LobbyPublishable;
import com.tf2center.discordbot.publish.publishables.SubstituteSlotsPublishable;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class EmbedsListener {

    @Autowired
    public EmbedsListener(GatewayDiscordClient client) {
        client.on(MessageCreateEvent.class).flatMap(this::recordNewEmbeds).subscribe();
    }

    private Mono<Void> recordNewEmbeds(MessageCreateEvent message) {
        String title = message.getMessage().getEmbeds().get(0).getTitle().get();
        Snowflake id = message.getMessage().getId();

        if (!title.toLowerCase().contains("lobby")) {
            return Mono.just(message.getMessage())
                    .flatMap(lobbyEmbed -> SubstituteSlotsPublishable.extractInformation(id));
        }
        return Mono.just(message.getMessage())
                .flatMap(lobbyEmbed -> LobbyPublishable.extractInformation(title, id));
    }
}
