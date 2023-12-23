package com.tf2center.discordbot.publish.publishables;

import com.tf2center.discordbot.embeds.EmbedsPool;
import com.tf2center.discordbot.publish.Publishable;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("lobbyPublishable")
public class LobbyPublishable implements Publishable {

    private final static Snowflake TEXT_CHANNEL_ID = Snowflake.of(Long.parseLong(System.getenv("TF2CLOBBY_CHANNEL")));
    private final GatewayDiscordClient client;
    private Collection<EmbedCreateSpec> embeds;

    //     event.getClient().getChannelById(snowflake).ofType(GuildMessageChannel.class)
//                .flatMap(channel -> channel.getMessageById(Snowflake.of(1187373319884910623L)).block().edit().withContent("another change").withEmbeds(sixes)).subscribe();

    @Autowired
    public LobbyPublishable(GatewayDiscordClient client) {
        this.client = client;
    }

    @Override
    public void refresh() {
        embeds = EmbedsPool.getLobbies().values();
    }

    @Override
    public void publish() {
        refresh();
        addLobbies();
    }


    private void editLobbies() {

    }

    private void addLobbies() {
        client.getChannelById(TEXT_CHANNEL_ID)
                .ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
                        .addAllEmbeds(embeds)
                        .build()
                )).block();
    }
}
