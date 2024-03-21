package com.tf2center.discordbot.publish.publishables;

import com.tf2center.discordbot.dto.TF2CLobbyIdDTO;
import com.tf2center.discordbot.embeds.EmbedActions;
import com.tf2center.discordbot.embeds.EmbedsPool;
import com.tf2center.discordbot.publish.Publishable;
import com.tf2center.discordbot.utils.TF2CStringUtils;
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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component("lobbyPublishable")
@Scope("singleton")
public class LobbyPublishable implements Publishable, EmbedActions {

    private static final Map<TF2CLobbyIdDTO, Snowflake> POSTED_IDS = new LinkedHashMap<>();
    private Map<TF2CLobbyIdDTO, EmbedCreateSpec> freshEmbeds;
    private final Mono<Channel> textChannel;

    @Autowired
    public LobbyPublishable(GatewayDiscordClient client) {
        textChannel = client.getChannelById(
                Snowflake.of(Long.parseLong(System.getenv("TF2CLOBBY_CHANNEL")))
        );
    }

    public static Mono<Void> extractInformation(String title, Snowflake snowflake) {
        int lobbyId = TF2CStringUtils.extractLobbyId(title);
        if (!POSTED_IDS.containsKey(TF2CLobbyIdDTO.of(lobbyId))) {
            POSTED_IDS.put(TF2CLobbyIdDTO.of(lobbyId), snowflake);
        }
        return Mono.empty();
    }

    @Override
    public void publish() {
        //Get fresh lobbies
        freshEmbeds = EmbedsPool.getFreshLobbies();
        List<TF2CLobbyIdDTO> freshEmbedsTF2CLobbySortedIdList = freshEmbeds.keySet().stream().sorted().toList();

        //Synchronizing this to avoid "java.util.ConcurrentModificationException: null" on "for (TF2CLobbyIdDTO postedId : POSTED_IDS.keySet()) {"
        //java.util.ConcurrentModificationException: null WILL BREAK THE LOBBY PUBLISHING MECHANISM
        synchronized (this) {
            //Delete old lobbies
            if (!POSTED_IDS.keySet().isEmpty()) {
                for (TF2CLobbyIdDTO postedId : POSTED_IDS.keySet()) {
                    if (!freshEmbeds.containsKey(postedId)) {
                        deleteEmbed(POSTED_IDS.get(postedId));
                        POSTED_IDS.remove(postedId);
                    }
                }
            }
        }
        //Edit old lobbies and post new
        for (TF2CLobbyIdDTO freshTF2CLobbyId : freshEmbedsTF2CLobbySortedIdList) {
            if (!POSTED_IDS.isEmpty() && POSTED_IDS.containsKey(freshTF2CLobbyId)) {
                editEmbed(freshEmbeds.get(freshTF2CLobbyId), POSTED_IDS.get(freshTF2CLobbyId));
            }
            if (!POSTED_IDS.containsKey(freshTF2CLobbyId)) {
                publishEmbed(freshEmbeds.get(freshTF2CLobbyId));
            }
        }
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
