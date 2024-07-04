package com.tf2center.discordbot.parser.discord.notifications;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Component
@Scope("singleton")
public class AuthLogChannelParser {

    private final Mono<Channel> textChannel;

    @Autowired
    public AuthLogChannelParser(GatewayDiscordClient client) {
        textChannel = client.getChannelById(
            Snowflake.of(Long.parseLong(System.getenv("TF2CAUTH")))
        );
    }

    private static Object readCSV() {
        return null;
    }

    private static boolean writeCSV(){
        return false;
    }

    //    @Scheduled
    public void parseLogChannel() {
        //csv = readCSV();
        List<Message> messages = textChannel.ofType(GuildMessageChannel.class)
            .map(channel -> channel.getMessagesBefore(Snowflake.of(Instant.now()))
                .take(1)
                .collectList()
                .block()
            ).block();

        //if(csvCollection.contains(steamId) || csvCollection.contains(discordId)){
        // return;
        //}
        //writeCSV();
    }

}
