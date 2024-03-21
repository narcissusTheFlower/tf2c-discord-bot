package com.tf2center.discordbot.listeners;

import com.tf2center.discordbot.exceptions.TF2CUpdateException;
import com.tf2center.discordbot.publish.publishables.LobbyPublishable;
import com.tf2center.discordbot.publish.publishables.SubstituteSlotsPublishable;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.Embed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class EmbedsListener {

    @Autowired
    public EmbedsListener(GatewayDiscordClient client) {
        client.on(MessageCreateEvent.class).flatMap(this::recordNewEmbeds).subscribe();
    }

    private Mono<Void> recordNewEmbeds(MessageCreateEvent message) {
        String title;
        List<Embed> embeds = message.getMessage().getEmbeds();
        if (!embeds.isEmpty()) {
            title = embeds.get(0).getTitle().get();
            Snowflake id = message.getMessage().getId();

            if (!title.toLowerCase().contains("lobby")) {
                return Mono.just(message.getMessage())
                        .flatMap(lobbyEmbed -> SubstituteSlotsPublishable.extractInformation(id));
            }

            return Mono.just(message.getMessage())
                    .flatMap(lobbyEmbed -> LobbyPublishable.extractInformation(title, id));

            // i am not sure if this exception happens everything goes to hell
        } else {
            throw new TF2CUpdateException("Failed to detect embeds in new messages, which should be impossible. " +
                    "something has gone wrong.");
        }




    }
}
