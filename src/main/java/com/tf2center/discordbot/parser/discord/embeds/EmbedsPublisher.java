package com.tf2center.discordbot.parser.discord.embeds;

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

    private static int extractLobbyId(String src) {
        return Integer.parseInt(
            String.valueOf(extractDigits(src)).substring(0, 7)
        );
    }

    private void manageSubsEmbed(EmbedCreateSpec newSubsEmbed) {
        Optional<Snowflake> oldEmbed = extractPostedSubEmbed(textChannel);
        if (oldEmbed.isEmpty()) {
            messageActions.publishEmbed(newSubsEmbed);
        } else {
            messageActions.editEmbed(newSubsEmbed, oldEmbed.get());
        }
    }

    private static int extractDigits(String src) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            char c = src.charAt(i);
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }
        return Integer.parseInt(builder.toString());
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

    private void manageLobbiesEmbeds(Set<EmbedCreateSpec> newEmbeds) {
        Map<Integer, EmbedCreateSpec> newEmbedToLobbyId = new HashMap<>();
        newEmbeds.forEach(newEmbedSpecBuilder -> {
            newEmbedToLobbyId.put(extractLobbyId(newEmbedSpecBuilder.title().get()), newEmbedSpecBuilder);
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
            int lobbyId = extractLobbyId(
                message.getEmbeds().get(0).getTitle().get()
            );
            result.put(lobbyId, message.getId());
        });
        return result;
    }


}
