package com.tf2center.discordbot.parser.discord.notifications;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ChannelMessageDownloader {

    private final Mono<Channel> textChannel;
    //also inject dataAccess bean

    @Autowired
    public ChannelMessageDownloader(GatewayDiscordClient client) {
        textChannel = client.getChannelById(
            Snowflake.of(Long.parseLong(System.getenv("TF2CAUTH")))
        );
    }

    //    @PostConstruct
    private void run() {
        List<Message> messages = textChannel.ofType(GuildMessageChannel.class)
            .map(channel -> channel.getMessagesBefore(Snowflake.of(Instant.now()))
                .take(2000)
                .collectList()
                .block()
            ).block();
        Map<String, String> stringStringMap = parseIds(messages);
//        writetoCSV()
    }

    private Map<String, String> parseIds(List<Message> messages) {
        Map<String, String> steamDiscordIds = new HashMap<>();
        messages.forEach(message -> {
            if (!message.getEmbeds().isEmpty()) {
//                String steamId = message.getEmbeds().get(0)
                //String discordId = message.getEmbeds().get(0)
                //steamDiscordIds.put(steamId, discordId);
            }
        });
        return steamDiscordIds;
    }

    private void writetoCSV(Map<String, String> ids) {
        Object dadaAccessBean = new Object();
        //3d partyLibrary
        //insert everything into a db
    }

}
