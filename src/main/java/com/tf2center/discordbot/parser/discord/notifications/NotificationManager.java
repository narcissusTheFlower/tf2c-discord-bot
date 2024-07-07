package com.tf2center.discordbot.parser.discord.notifications;

import com.tf2center.discordbot.parser.dto.LobbyDTO;
import com.tf2center.discordbot.parser.dto.MainPageObject;
import com.tf2center.discordbot.parser.dto.SlotDTO;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.Channel;
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
        //use reflection to edit objects
        List<LobbyDTO> readyLobbies = lobbies.stream()
            .map(e -> (LobbyDTO) e)
//            .filter(LobbyDTO::isReady)  undo when done with testing
            .toList();

//        if (readyLobbies.isEmpty()) {
//            return;
//        }

        Map<String, List<String>> subscribersSteamDiscordIds = CSVController.readSubscribers();
        Map<String, String> allPlayersSteamDiscordIds = CSVController.readAllPlayers();
        List<SlotDTO> allPlayersInLobby = new ArrayList<>();
        readyLobbies.forEach(lobby -> {
            //add steamid not empty filter
            lobby.getTeams().values().forEach(allPlayersInLobby::addAll);
        });
        List<SlotDTO> list = allPlayersInLobby.stream().filter(slotDTO -> slotDTO.getSteamId().isPresent()).toList();
        System.out.println();


//            allPlayersInLobby.stream()
//                .filter(subscribers::contains)
//                .forEach(subscriber -> sendNotification(steamDiscordIds.get(subscriber.getSteamId().get()), "Your lobby is ready!"));
//
//            stringStringMap.forEach(subscriber -> sendNotification());
    }

//    private void sendNotification(Set<String> discordIDs, String notificationText) {
//        discordIDs.forEach(discordId ->{
//
//
//            client.getChannelById(event.getMessage().getChannelId())
//                .ofType(MessageChannel.class)
//                .flatMap(channel -> channel.createMessage("You are unsubscribed from notifications ❗"))
//                .subscribe();
//            }
//        );
//
//    }

}
