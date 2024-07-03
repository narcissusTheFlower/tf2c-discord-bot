package com.tf2center.discordbot.parser.discord;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
@Scope("singleton")
public class MessageActions {

    private final Mono<Channel> textChannel;

    @Autowired
    public MessageActions(GatewayDiscordClient client) {
        textChannel = client.getChannelById(
            Snowflake.of(Long.parseLong(System.getenv("TF2CLOBBY_CHANNEL")))
        );
    }

    public void publishEmbed(EmbedCreateSpec embed) {
        textChannel.ofType(GuildMessageChannel.class)
            .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
                .addEmbed(embed)
                .build()
            )).block();
    }

    public void deleteEmbed(Snowflake snowflake) {
        textChannel.ofType(GuildMessageChannel.class)
            .flatMap(channel -> channel.getMessageById(snowflake).block()
                .delete())
            .subscribe();
    }

    public void editEmbed(EmbedCreateSpec embed, Snowflake snowflake) {
        textChannel.ofType(GuildMessageChannel.class)
            .flatMap(channel -> channel.getMessageById(snowflake).block()
                .edit()
                .withEmbeds(embed))
            .subscribe();
    }


}
