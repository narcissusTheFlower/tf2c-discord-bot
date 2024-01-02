package com.tf2center.discordbot.publish.publishables;

import com.tf2center.discordbot.dto.TF2CLobbyIdDTO;
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

import java.util.*;

@Component("lobbyPublishable")
@Scope("singleton")
public class LobbyPublishable implements Publishable {

    private static final Map<TF2CLobbyIdDTO, Snowflake> POSTED_IDS = new LinkedHashMap<>();
    static List<Integer> test = new ArrayList<>();
    private final static Snowflake TEXT_CHANNEL_ID = Snowflake.of(Long.parseLong(System.getenv("TF2CLOBBY_CHANNEL")));
    private Map<TF2CLobbyIdDTO, EmbedCreateSpec> freshEmbeds;
    private final Mono<Channel> textChannel;
    private List<TF2CLobbyIdDTO> freshEmbedsTF2CLobbySortedIdList;

    @Autowired
    public LobbyPublishable(GatewayDiscordClient client) {
        textChannel = client.getChannelById(TEXT_CHANNEL_ID);
    }

    public static Mono<Void> extractInformation(String title, Snowflake snowflake) {
        int lobbyId = TF2CStringUtils.extractLobbyId(title);
        test.add(lobbyId);
        if (!POSTED_IDS.containsKey(TF2CLobbyIdDTO.of(lobbyId))) {
            POSTED_IDS.put(TF2CLobbyIdDTO.of(lobbyId), snowflake);
        }
        return Mono.empty();
    }

    @Override
    public void publish() {
        //Publish subs
        //publishEmbed(EmbedsPool.getFreshSubs());

        //Get fresh lobbies
        freshEmbeds = EmbedsPool.getFreshLobbies();
        freshEmbedsTF2CLobbySortedIdList = freshEmbeds.keySet().stream().sorted().toList();

        //Delete old lobbies
        if (!POSTED_IDS.isEmpty()) {
            for (TF2CLobbyIdDTO postedId : POSTED_IDS.keySet()) { //java.util.ConcurrentModificationException: null //TODO FIX ME
                if (!freshEmbeds.containsKey(postedId) && POSTED_IDS.containsKey(postedId)) {
                    deleteEmbed(POSTED_IDS.get(postedId));
                    POSTED_IDS.remove(postedId);
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

    private void publishEmbed(EmbedCreateSpec embed) {
        textChannel.ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
                        .addEmbed(embed)
                        .build()
                )).block();
    }

    private void publishAllEmbeds(Set<EmbedCreateSpec> embeds) {
        for (EmbedCreateSpec embed : embeds) {
            publishEmbed(embed);
        }
    }

    private void deleteEmbed(Snowflake snowflake) {
        textChannel.ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.getMessageById(snowflake).block()
                        .delete())
                .subscribe();
    }

    private void editEmbed(EmbedCreateSpec embed, Snowflake snowflake) {
        textChannel.ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.getMessageById(snowflake).block()
                        .edit()
                        .withEmbeds(embed))
                .subscribe();
    }
}
