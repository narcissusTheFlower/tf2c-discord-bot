package com.tf2center.discordbot.publish.publishables;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

/**
 * This class is responsible for clearing the text channel to where the embeds are posted as not to delete them
 * manually every time the bot is restarted.
 */
@Component("prePublishActions")
@Scope("singleton")
public class PrePublishActions {

    private final Mono<Channel> textChannel;

    @Autowired
    public PrePublishActions(GatewayDiscordClient client) {
        textChannel = client.getChannelById(
                Snowflake.of(Long.parseLong(System.getenv("TF2CLOBBY_CHANNEL")))
        );
    }

    //@PostConstruct
    private void cleanMessageChannel() {
        List<Snowflake> snowflakes = textChannel.ofType(GuildMessageChannel.class)
                .map(channel -> channel.getMessagesBefore(Snowflake.of(Instant.now()))
                        .take(6)
                        .filter(message -> !message.getEmbeds().isEmpty())
                        .map(Message::getId)
                        .collectList()
                        .block()
                ).block();

        for (Snowflake snowflake : snowflakes) {
            textChannel.ofType(GuildMessageChannel.class)
                    .flatMap(channel -> channel.getMessageById(snowflake).block()
                            .delete())
                    .block();
        }


    }
}
