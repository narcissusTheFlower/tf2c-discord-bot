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
    private static Map<Snowflake, TF2CLobbyId> postedMessagesIds = new LinkedHashMap<>();
    private final Mono<Channel> textChannel;
    private Map<TF2CLobbyId, EmbedCreateSpec> freshEmbeds;
    private Iterator<TF2CLobbyId> freshTf2cIds;

    @Autowired
    public LobbyPublishable(GatewayDiscordClient client) {
        textChannel = client.getChannelById(TEXT_CHANNEL_ID);

    }

    //TF2C id is for detecting if a lobby needs to be edited or posted as new message
    //Snowflake is for finding messages that need to be edited

    public static Mono<Void> extractInformation(String title, Snowflake snowflake) {
        int lobbyId = TF2CStringUtils.extractDigits(title);
        postedMessagesIds.put(snowflake, new TF2CLobbyId(lobbyId));
        return Mono.empty();
    }

    @Override
    public void publish() {
        refresh();
        for (EmbedCreateSpec embed : freshEmbeds.values()) {
            if (freshTf2cIds.hasNext()) {
                if (postedMessagesIds.containsValue(freshTf2cIds.next())) {
                    editLobby(embed, );
                }
                publishNewLobby(embed);
            }
        }
    }

    @Override
    public void refresh() {
        freshEmbeds = EmbedsPool.getFreshLobbies();
        freshTf2cIds = freshEmbeds.keySet().iterator();
    }

    private void editLobby(EmbedCreateSpec embed, Snowflake snowflake) {
        textChannel.ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.getMessageById(snowflake).block()
                        .edit()
                        .withEmbeds(embed))
                .subscribe();
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
}
