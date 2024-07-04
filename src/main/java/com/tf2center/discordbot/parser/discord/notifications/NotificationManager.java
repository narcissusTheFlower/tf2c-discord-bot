package com.tf2center.discordbot.parser.discord.notifications;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Scope("singleton")
public class NotificationManager {

    private final Mono<Channel> textChannel;

    @Autowired
    public NotificationManager(GatewayDiscordClient client) {
        textChannel = client.getChannelById(
                Snowflake.of(Long.parseLong(System.getenv("TF2CAUTH")))
        );
    }

    public void manage(){
        //readCSV();
    }

}
