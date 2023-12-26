package com.tf2center.discordbot.publish.publishables;

import com.tf2center.discordbot.embeds.EmbedsPool;
import com.tf2center.discordbot.embeds.TF2CLobbyId;
import com.tf2center.discordbot.publish.Publishable;
import com.tf2center.discordbot.utils.TF2CStringUtils;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component("lobbyPublishable")
public class LobbyPublishable implements Publishable {

    private final static Snowflake TEXT_CHANNEL_ID = Snowflake.of(Long.parseLong(System.getenv("TF2CLOBBY_CHANNEL")));
    private static final Map<TF2CLobbyId, Snowflake> postedMessagesIds = new LinkedHashMap<>();
    private final Mono<Channel> textChannel;
    private Map<TF2CLobbyId, EmbedCreateSpec> freshEmbeds;
    private List<TF2CLobbyId> freshEmbedsTF2CLobbySortedIdList;

    @Autowired
    public LobbyPublishable(GatewayDiscordClient client) {
        textChannel = client.getChannelById(TEXT_CHANNEL_ID);
    }

    //TF2C id is for detecting if a lobby needs to be edited or posted as new message
    //Snowflake is for finding messages that need to be edited/deleted
    public static Mono<Void> extractInformation(String title, Snowflake snowflake) {
        int lobbyId = TF2CStringUtils.extractLobbyId(title);
        if (!postedMessagesIds.containsKey(TF2CLobbyId.of(lobbyId))) {
            postedMessagesIds.put(TF2CLobbyId.of(lobbyId), snowflake);
            //postedMessagesIdsIterator = postedMessagesIds.keySet().iterator();
        }
        return Mono.empty();
    }

    @Override
    public void publish() {
        getNewLobbiesFromPool();
        //Delete
        if (!postedMessagesIds.isEmpty()) {
            for (TF2CLobbyId postedId : postedMessagesIds.keySet()) {
                if (!freshEmbeds.containsKey(postedId) && postedMessagesIds.containsKey(postedId)) {
                    deleteLobby(postedMessagesIds.get(postedId));
                    postedMessagesIds.remove(postedId);
                }
            }
        }

        for (TF2CLobbyId newTF2CLobbyId : freshEmbedsTF2CLobbySortedIdList) {
            //Edit
            if (!postedMessagesIds.isEmpty() && postedMessagesIds.containsKey(newTF2CLobbyId)) {
                editLobby(
                        freshEmbeds.get(newTF2CLobbyId),
                        postedMessagesIds.get(newTF2CLobbyId)
                );
            }
            //Post new
            if (!postedMessagesIds.containsKey(newTF2CLobbyId)) {
                publishNewLobby(freshEmbeds.get(newTF2CLobbyId));
            }
        }
    }

    public void getNewLobbiesFromPool() {
        freshEmbeds = EmbedsPool.getFreshLobbies();
        freshEmbedsTF2CLobbySortedIdList = freshEmbeds.keySet().stream().sorted().toList();
    }

    private void publishNewLobby(EmbedCreateSpec embed) {
        textChannel.ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
                        .addEmbed(embed)
                        .build()
                )).block();
    }

    private void deleteLobby(Snowflake snowflake) {
        textChannel.ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.getMessageById(snowflake).block()
                        .delete())
                .subscribe();
    }

    private void editLobby(EmbedCreateSpec embed, Snowflake snowflake) {

        textChannel.ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.getMessageById(snowflake).block()
                        .edit()
                        .withEmbeds(embed))
                .subscribe();
    }
}