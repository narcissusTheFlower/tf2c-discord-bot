package com.tf2center.discordbot.parser.discord.notifications;

import com.tf2center.discordbot.parser.dto.LobbyDTO;
import com.tf2center.discordbot.parser.dto.MainPageObject;
import com.tf2center.discordbot.parser.dto.SlotDTO;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@Scope("singleton")
public class NotificationManager {

    private final GatewayDiscordClient client;

    @Autowired
    public NotificationManager(GatewayDiscordClient client) {
        this.client = client;
    }

    public void manage(Collection<MainPageObject> lobbies) {
        //Variable for readability
        Map<String, List<String>> subscribersSteamDiscordChatIds = CSVActions.readSubscribers();

        List<SlotDTO> allPlayersInLobby = new ArrayList<>();
        lobbies.stream()
            .map(e -> (LobbyDTO) e)
            .filter(LobbyDTO::isReady)
            .forEach(lobby -> lobby.getTeams().values().forEach(allPlayersInLobby::addAll));

        if (allPlayersInLobby.isEmpty()) return;

        allPlayersInLobby.stream()
            .filter(slotDTO -> slotDTO.getSteamId().isPresent())
            .filter(slotDTO -> subscribersSteamDiscordChatIds.containsKey(slotDTO.getSteamId().get()))
            .forEach(player -> {
                if (NotifiedUsers.getConcurrentHashMapSteamDiscordId().containsKey(player.getSteamId().get())) {
                    return;
                }

                client.getChannelById(Snowflake.of(
                        subscribersSteamDiscordChatIds.get(player.getSteamId().get()).get(1)
                    ))
                    .ofType(MessageChannel.class)
                    .flatMap(channel -> channel.createMessage("Your lobby is ready❗"))
                    .block();

                NotifiedUsers.getConcurrentHashMapSteamDiscordId().put(
                    player.getSteamId().get(),
                    subscribersSteamDiscordChatIds.get(player.getSteamId().get()).get(0));
            });
    }

}
