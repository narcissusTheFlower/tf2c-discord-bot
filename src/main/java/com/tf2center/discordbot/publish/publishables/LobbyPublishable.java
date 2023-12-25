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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Component("lobbyPublishable")
public class LobbyPublishable implements Publishable {

    private final static Snowflake TEXT_CHANNEL_ID = Snowflake.of(Long.parseLong(System.getenv("TF2CLOBBY_CHANNEL")));
    private static final Map<TF2CLobbyId, Snowflake> postedMessagesIds = new LinkedHashMap<>();
    private static Iterator<TF2CLobbyId> postedMessagesIdsIterator;
    private final Mono<Channel> textChannel;
    private Map<TF2CLobbyId, EmbedCreateSpec> freshEmbeds;
    private static TF2CLobbyId latestTF2CLobbyId = TF2CLobbyId.of(0);
    private Iterator<TF2CLobbyId> freshTF2cIds;

    @Autowired
    public LobbyPublishable(GatewayDiscordClient client) {
        textChannel = client.getChannelById(TEXT_CHANNEL_ID);
    }

    //TF2C id is for detecting if a lobby needs to be edited or posted as new message
    //Snowflake is for finding messages that need to be edited/deleted
    public static Mono<Void> extractInformation(String title, Snowflake snowflake) {
        int lobbyId = TF2CStringUtils.extractDigits(title);
        if (!postedMessagesIds.containsKey(TF2CLobbyId.of(lobbyId))) {
            postedMessagesIds.put(TF2CLobbyId.of(lobbyId), snowflake);
            postedMessagesIdsIterator = postedMessagesIds.keySet().iterator();
            latestTF2CLobbyId = TF2CLobbyId.of(lobbyId);
        }
        return Mono.empty();
    }

    @Override
    public void publish() {
        getNewLobbiesFromPool();
        for (TF2CLobbyId newTF2CLobbyId : freshEmbeds.keySet()) {
            //Edit
            if (postedMessagesIds.containsKey(newTF2CLobbyId)) {
                editLobby(
                        freshEmbeds.get(newTF2CLobbyId),
                        postedMessagesIds.get(newTF2CLobbyId)
                );
                //Delete
                //} else if (!postedMessagesIds.containsKey(newTF2CLobbyId) && latestTF2CLobbyId.intValue() > newTF2CLobbyId.intValue()) {
            } else if (postedMessagesIdsIterator != null) {
                if (postedMessagesIdsIterator.next().intValue() != newTF2CLobbyId.intValue() &&
                        latestTF2CLobbyId.intValue() > newTF2CLobbyId.intValue()) {
                    //deleteLobby();
                }
                //Post new
            } else if (!postedMessagesIds.containsKey(newTF2CLobbyId) && latestTF2CLobbyId.intValue() < newTF2CLobbyId.intValue()) {
                publishNewLobby(freshEmbeds.get(newTF2CLobbyId));
            }
        }
    }

    public void getNewLobbiesFromPool() {
        freshEmbeds = EmbedsPool.getFreshLobbies();
        freshTF2cIds = freshEmbeds.keySet().iterator();
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
