package com.tf2center.discordbot.publish.publishables;

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
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component("lobbyPublishable")
public class LobbyPublishable implements Publishable {

    private final static Snowflake TEXT_CHANNEL_ID = Snowflake.of(Long.parseLong(System.getenv("TF2CLOBBY_CHANNEL")));
    private final static List<Long> IDS = new LinkedList<>();
    private final Mono<Channel> textChannel;
    private Map<Integer, EmbedCreateSpec> embeds;

    @Autowired
    public LobbyPublishable(GatewayDiscordClient client) {
        textChannel = client.getChannelById(TEXT_CHANNEL_ID);
    }

    public static Mono<Void> takeNewLobby(String title) {
        long lobbyId = TF2CStringUtils.extractDigits(title);
        IDS.add(lobbyId);
        return Mono.empty();
    }

    @Override
    public void refresh() {
        embeds = EmbedsPool.getFreshLobbies();
    }

    @Override
    public void publish() {
        //First do a refresh
        refresh();

        if (newLobbiesDetected()) {
            editLobbies();
        } else {
            publishNewLobbies();
        }
    }

    private void editLobbies() {
        //     event.getClient().getChannelById(snowflake).ofType(GuildMessageChannel.class)
//                .flatMap(channel -> channel.getMessageById(Snowflake.of(1187373319884910623L)).block().edit().withContent("another change")
//                .withEmbeds(sixes)).subscribe();
    }

    private void publishNewLobbies() {
        for (EmbedCreateSpec embed : embeds.values()) {
            textChannel.ofType(GuildMessageChannel.class)
                    .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
                            .addEmbed(embed)
                            .build()
                    )).block();
        }
    }

    private boolean newLobbiesDetected() {

        refresh();
        return false;
    }
}
