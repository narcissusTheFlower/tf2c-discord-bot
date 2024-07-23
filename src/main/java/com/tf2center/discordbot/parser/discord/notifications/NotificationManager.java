package com.tf2center.discordbot.parser.discord.notifications;

import com.tf2center.discordbot.parser.dto.LobbyDTO;
import com.tf2center.discordbot.parser.dto.MainPageObject;
import com.tf2center.discordbot.parser.dto.SlotDTO;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("singleton")
@EnableScheduling
public class NotificationManager {

    private final GatewayDiscordClient client;

    private static final Map<String, String> notifiedPlayers = new ConcurrentHashMap<>();

    @Autowired
    public NotificationManager(GatewayDiscordClient client) {
        this.client = client;
    }

    @Scheduled(fixedRate = 35_000)
    private void emptyReadyPlayers() {
        notifiedPlayers.clear();
    }

    public void manage(Collection<MainPageObject> lobbies) {
        //Variable for readability
        Map<String, List<String>> subscribersSteamDiscordChatIds = CSVActions.readSubscribers();

        List<SlotDTO> allPlayersInLobby = new ArrayList<>();
//        allPlayersInLobby.add(SlotDTO.of("narcissus","76561198106563151",false, TF2Team.BLU));

        lobbies.stream()
            .map(e -> (LobbyDTO) e)
//            .filter(LobbyDTO::isReady)
            .forEach(lobby -> lobby.getTeams().values().forEach(allPlayersInLobby::addAll));

        if (allPlayersInLobby.isEmpty()) return;

        allPlayersInLobby.stream()
            .filter(slotDTO -> slotDTO.getSteamId().isPresent()) //will throw exception without this
            .filter(slotDTO -> subscribersSteamDiscordChatIds.containsKey(slotDTO.getSteamId().get()))//empty?
            .forEach(player -> {
                if (notifiedPlayers.containsKey(player.getSteamId().get())) {
                    return;
                }

                client.getChannelById(Snowflake.of(
                        subscribersSteamDiscordChatIds.get(player.getSteamId().get()).get(1)
                    ))
                    .ofType(MessageChannel.class)
                    .flatMap(channel -> channel.createMessage("Your lobby is ready❗"))
                    .block();

                notifiedPlayers.put(
                    player.getSteamId().get(),
                    subscribersSteamDiscordChatIds.get(player.getSteamId().get()).get(0));
            });
    }
}
