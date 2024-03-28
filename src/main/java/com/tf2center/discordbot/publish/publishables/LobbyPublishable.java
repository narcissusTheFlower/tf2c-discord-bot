package com.tf2center.discordbot.publish.publishables;

import com.tf2center.discordbot.dto.TF2CLobbyIdDTO;
import com.tf2center.discordbot.embeds.EmbedActions;
import com.tf2center.discordbot.embeds.EmbedsPool;
import com.tf2center.discordbot.publish.Publishable;
import com.tf2center.discordbot.utils.TF2CStringUtils;
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
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component("lobbyPublishable")
@Scope("singleton")
public class LobbyPublishable implements Publishable, EmbedActions {

    private final Mono<Channel> textChannel;

    @Autowired
    public LobbyPublishable(GatewayDiscordClient client) {
        textChannel = client.getChannelById(
                Snowflake.of(Long.parseLong(System.getenv("TF2CLOBBY_CHANNEL")))
        );
    }

    @Override
    public void publish() {
        //Gathering info
        final Map<TF2CLobbyIdDTO, EmbedCreateSpec> freshEmbeds = EmbedsPool.getFreshLobbies();
        final List<TF2CLobbyIdDTO> freshEmbedsTF2CLobbySortedIdList = freshEmbeds.keySet().stream().sorted().toList();
        final Map<TF2CLobbyIdDTO, Snowflake> postedIdPlusSnowflake = extractPostedLobbiesEmbeds(textChannel);

        //Edit old lobbies and post new
        for (TF2CLobbyIdDTO freshTF2CLobbyId : freshEmbedsTF2CLobbySortedIdList) {
            if (!postedIdPlusSnowflake.isEmpty() && postedIdPlusSnowflake.containsKey(freshTF2CLobbyId)) {
                editEmbed(freshEmbeds.get(freshTF2CLobbyId), postedIdPlusSnowflake.get(freshTF2CLobbyId));
            }
            if (!postedIdPlusSnowflake.containsKey(freshTF2CLobbyId)) {
                publishEmbed(freshEmbeds.get(freshTF2CLobbyId));
            }
        }
        //Delete old lobbies
        if (!postedIdPlusSnowflake.keySet().isEmpty()) {
            for (TF2CLobbyIdDTO postedId : postedIdPlusSnowflake.keySet()) {
                if (!freshEmbeds.containsKey(postedId)) {
                    deleteEmbed(postedIdPlusSnowflake.get(postedId));
                }
            }
        }


    }

    private Map<TF2CLobbyIdDTO, Snowflake> extractPostedLobbiesEmbeds(Mono<Channel> textChannel) {
        final List<Message> postedEmbeds = textChannel.ofType(GuildMessageChannel.class)
                .map(channel -> channel.getMessagesBefore(Snowflake.of(Instant.now()))
                        .take(6)
                        .filter(message -> !message.getEmbeds().get(0).getTitle().get().toLowerCase().contains("substitute"))
                        .collectList()
                        .block())
                .block();

        if (postedEmbeds == null || postedEmbeds.isEmpty()) {
            return Collections.emptyMap();
        }

        final Map<TF2CLobbyIdDTO, Snowflake> result = new LinkedHashMap<>();
        postedEmbeds.forEach(message -> {
            int tf2cLobbyId = TF2CStringUtils.extractLobbyId(
                    message.getEmbeds().get(0).getTitle().get()
            );
            result.put(TF2CLobbyIdDTO.of(tf2cLobbyId), message.getId());
        });
        return result;
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
        try {
        textChannel.ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.getMessageById(snowflake).block()
                        .delete())
                .subscribe();
        } catch (Exception e) {
            System.out.println("caught while editing");
        }
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
