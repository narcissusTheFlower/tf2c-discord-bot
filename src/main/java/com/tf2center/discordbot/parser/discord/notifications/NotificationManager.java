package com.tf2center.discordbot.parser.discord.notifications;

import com.tf2center.discordbot.parser.discord.listeners.commands.CSVController;
import com.tf2center.discordbot.parser.dto.LobbyDTO;
import com.tf2center.discordbot.parser.dto.MainPageObject;
import com.tf2center.discordbot.parser.dto.SlotDTO;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.spec.MessageCreateSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    public void manage(Collection<MainPageObject> lobbies) {
        //Map<String, String> subscribers = readSubscribersCSV();
        List<LobbyDTO> readyLobbies = lobbies.stream()
            .map(e -> (LobbyDTO) e)
            .filter(LobbyDTO::isReady)
            .toList();

        if (readyLobbies.isEmpty()) {
            return;
        }

        readyLobbies.forEach(lobby -> {
            List<SlotDTO> allPlayersInLobby = new ArrayList<>();
            lobby.getTeams().values().forEach(allPlayersInLobby::addAll);

            //Create 2 csv files. One with all ids, and one with only subscribed people
            Map<String, String> steamDiscordIds = CSVController.readSubscribers();
            allPlayersInLobby.stream()
                .filter(subscribers::contains)
                .forEach(subscriber -> sendNotification(steamDiscordIds.get(subscriber.getSteamId().get()), "Your lobby is ready!"));

            stringStringMap.forEach(subscriber -> sendNotification());

        });
    }

    private void sendNotification(String DiscordId, String notificationText) {
        //TODO not guild channel!!!!
        textChannel.ofType(GuildMessageChannel.class)
            .flatMap(channel -> channel.createMessage(MessageCreateSpec.builder()
                .content(notificationText)
                .build()
            )).block();
    }

}
