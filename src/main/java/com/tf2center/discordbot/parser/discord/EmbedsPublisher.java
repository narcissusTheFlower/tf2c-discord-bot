package com.tf2center.discordbot.parser.discord;

import com.tf2center.discordbot.utils.TF2CStringUtils;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;


@Component
@Scope("singleton")
public class EmbedsPublisher {

    private final Mono<Channel> textChannel;
    private MessageActions messageActions;

    @Autowired
    public EmbedsPublisher(GatewayDiscordClient client, MessageActions messageActions) {
        textChannel = client.getChannelById(
            Snowflake.of(Long.parseLong(System.getenv("TF2CLOBBY_CHANNEL")))
        );
        this.messageActions = messageActions;
    }

    public void run(Map<String, Set<EmbedCreateSpec>> newEmbeds) {
        manageLobbiesEmbeds(
            newEmbeds.get("EmbedLobbies")
        );
        manageSubsEmbed(
            newEmbeds.get("EmbedSubs").iterator().next()
        );
    }

    private void manageLobbiesEmbeds(Set<EmbedCreateSpec> newEmbeds) {
        Map<Integer, EmbedCreateSpec> newEmbedToLobbyId = new HashMap<>();
        newEmbeds.forEach(newEmbedSpecBuilder -> {
            newEmbedToLobbyId.put(TF2CStringUtils.extractLobbyId(newEmbedSpecBuilder.title().get()), newEmbedSpecBuilder);
        });

        Map<Integer, Snowflake> oldLobbyIdToSnowflake = extractPostedLobbiesEmbeds(textChannel);
        oldLobbyIdToSnowflake.forEach((oldLobbyId, discordMessageId) -> {
            if (!newEmbedToLobbyId.containsKey(oldLobbyId)) {
                messageActions.deleteEmbed(discordMessageId);
            } else {
                messageActions.editEmbed(newEmbedToLobbyId.get(oldLobbyId), discordMessageId);
            }
        });

        newEmbedToLobbyId.forEach((newLobbyId, newEmbed) -> {
            if (!oldLobbyIdToSnowflake.containsKey(newLobbyId)) {
                messageActions.publishEmbed(newEmbed);
            }
        });
    }

    private void manageSubsEmbed(EmbedCreateSpec newSubsEmbed) {
        Optional<Snowflake> oldEmbed = extractPostedSubEmbed(textChannel);
        if (oldEmbed.isEmpty()) {
            messageActions.publishEmbed(newSubsEmbed);
        } else {
            messageActions.editEmbed(newSubsEmbed, oldEmbed.get());
        }
    }

    private Map<Integer, Snowflake> extractPostedLobbiesEmbeds(Mono<Channel> textChannel) {
        if (textChannel.ofType(GuildMessageChannel.class).block().getLastMessage() == null) {
            return Collections.emptyMap();
        }
        List<Message> postedEmbeds = textChannel.ofType(GuildMessageChannel.class)
            .map(channel -> channel.getMessagesBefore(Snowflake.of(Instant.now()))
                .take(8)
                .filter(message -> !message.getEmbeds().isEmpty())
                .filter(message -> !message.getEmbeds().get(0).getTitle().get().toLowerCase().contains("substitute"))
                .collectList()
                .block())
            .block();

        if (postedEmbeds == null || postedEmbeds.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Integer, Snowflake> result = new LinkedHashMap<>();
        postedEmbeds.forEach(message -> {
            int lobbyId = TF2CStringUtils.extractLobbyId(
                message.getEmbeds().get(0).getTitle().get()
            );
            result.put(lobbyId, message.getId());
        });
        return result;
    }

    private Optional<Snowflake> extractPostedSubEmbed(Mono<Channel> textChannel) {
        if (textChannel.ofType(GuildMessageChannel.class).block().getLastMessage() == null) {
            return Optional.empty();
        }

        List<Message> postedEmbed = textChannel.ofType(GuildMessageChannel.class)
            .map(channel -> channel.getMessagesBefore(Snowflake.of(Instant.now()))
                .take(8)
                .filter(message -> !message.getEmbeds().isEmpty())
                .filter(message -> message.getEmbeds().get(0).getTitle().get().toLowerCase().contains("substitute"))
                .collectList()
                .block())
            .block();

        if (postedEmbed == null || postedEmbed.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(postedEmbed.get(0).getId());
    }


}
