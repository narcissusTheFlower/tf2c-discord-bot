package com.tf2center.discordbot.publish.publishables;

import com.tf2center.discordbot.embeds.EmbedActions;
import com.tf2center.discordbot.publish.Publishable;
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

@Component("substituteSlotsPublishable")
@Scope("singleton")
public class SubstituteSlotsPublishable implements Publishable, EmbedActions {

    private final Mono<Channel> textChannel;
    private boolean thisIsFirstPost = true;

    @Autowired
    public SubstituteSlotsPublishable(GatewayDiscordClient client) {
        textChannel = client.getChannelById(
                Snowflake.of(Long.parseLong(System.getenv("TF2CLOBBY_CHANNEL")))
        );
    }

    @Override
    public void publish() {
//        EmbedCreateSpec freshSubstitues = EmbedsPool.getFreshSubstitues();
//        if (thisIsFirstPost){
//            //publishEmbed();
//        }


    }


    public Mono<Void> extractInformation(String title, Snowflake snowflake) {
        return Mono.empty();
    }

    @Override
    public void publishEmbed(EmbedCreateSpec embed) {
        textChannel.ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
                        .addEmbed(embed)
                        .build()
                )).block();
    }

    @Override
    public void deleteEmbed(Snowflake snowflake) {
        textChannel.ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.getMessageById(snowflake).block()
                        .delete())
                .subscribe();
    }

    @Override
    public void editEmbed(EmbedCreateSpec embed, Snowflake snowflake) {
        textChannel.ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.getMessageById(snowflake).block()
                        .edit()
                        .withEmbeds(embed))
                .subscribe();
    }
}
