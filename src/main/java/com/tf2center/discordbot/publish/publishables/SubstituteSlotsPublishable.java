package com.tf2center.discordbot.publish.publishables;

import com.tf2center.discordbot.embeds.EmbedActions;
import com.tf2center.discordbot.embeds.EmbedsPool;
import com.tf2center.discordbot.publish.Publishable;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Optional;

@Component("substituteSlotsPublishable")
@Scope("singleton")
public class SubstituteSlotsPublishable implements Publishable, EmbedActions {

    private final Mono<Channel> textChannel;
    private static Optional<Snowflake> subsEmbedId = Optional.empty();

    @Autowired
    public SubstituteSlotsPublishable(GatewayDiscordClient client) {
        textChannel = client.getChannelById(
                Snowflake.of(Long.parseLong(System.getenv("TF2CLOBBY_CHANNEL")))
        );
    }

    @Override
    public void publish() {
        EmbedCreateSpec freshSubstitues = EmbedsPool.getFreshSubstitues();
        if (subsEmbedId.isEmpty()) {
            Optional<Snowflake> snowflake = extractPostedSubstitutesEmbed(textChannel);
            if (snowflake.isEmpty()) {
                publishEmbed(freshSubstitues);
                subsEmbedId = extractPostedSubstitutesEmbed(textChannel);
            } else {
                subsEmbedId = snowflake;
            }
        } else {
            editEmbed(freshSubstitues, subsEmbedId.get());
        }

    }

    //    private Optional<Snowflake> extractPostedSubstitutesEmbed(Mono<Channel> textChannel) {
//        return Optional.ofNullable(
//                textChannel.ofType(GuildMessageChannel.class)
//                        .map(channel -> channel.getMessagesBefore(Snowflake.of(Instant.now()))
//                                .take(6)
//                                .filter(message -> message.getEmbeds().get(0).getTitle().get().toLowerCase().contains("substitute"))
//                                .blockFirst()
//                                .getId()
//                        )
//                        .block()
//        );
//    }
    private Optional<Snowflake> extractPostedSubstitutesEmbed(Mono<Channel> textChannel) {
        if (textChannel.ofType(GuildMessageChannel.class).block().getLastMessage() == null) {
            return Optional.empty();
        }

        try {
            Flux<Message> subsMessage = textChannel.ofType(GuildMessageChannel.class)
                    .map(channel -> channel.getMessagesBefore(Snowflake.of(Instant.now()))
                            .take(6)
                            .filter(message -> message.getEmbeds().get(0).getTitle().get().toLowerCase().contains("substitute"))
                    )
                    .block();

            return Optional.of(subsMessage.blockFirst().getId());
        } catch (NullPointerException e) {
            return Optional.empty();
        }
//    return Optional.ofNullable(
//                textChannel.ofType(GuildMessageChannel.class)
//                        .map(channel -> channel.getMessagesBefore(Snowflake.of(Instant.now()))
//                                .take(6)
//                                .filter(message -> message.getEmbeds().get(0).getTitle().get().toLowerCase().contains("substitute"))
//                                .blockFirst()
//                                .getId()
//                        )
//                        .block()
//        );
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
