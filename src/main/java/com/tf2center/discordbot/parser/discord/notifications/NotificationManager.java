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

import java.util.*;

@Component
@Scope("singleton")
@EnableScheduling
public class NotificationManager {

    private final GatewayDiscordClient client;

    private static final Set<NotifiedPlayerDestructor> NOTIFIED_PLAYERS = Collections.synchronizedSet(new HashSet<>());

    @Autowired
    public NotificationManager(GatewayDiscordClient client) {
        this.client = client;
    }

    @Scheduled(fixedRate = 30_000)
    private void emptyReadyPlayers() {
        NOTIFIED_PLAYERS.clear();
    }

    public void manage(Collection<MainPageObject> lobbies) {
        //Variable for readability, name describes data:
        //Key = steamId
        //List.get(0) = discordId
        //List.get(1) = discordChatId
        Map<String, List<String>> subscribersSteamDiscordChatIds = CSVActions.readSubscribers();

        List<SlotDTO> allPlayersInLobby = new ArrayList<>();
//        allPlayersInLobby.add(SlotDTO.of("narcissus","76561198106563151",false, TF2Team.BLU));

        lobbies.stream()
            .map(e -> (LobbyDTO) e)
            .filter(LobbyDTO::isReady)
            .forEach(lobby -> lobby.getTeams().values().forEach(allPlayersInLobby::addAll));

        if (allPlayersInLobby.isEmpty()) return;

        allPlayersInLobby.stream()
            .filter(slotDTO -> slotDTO.getSteamId().isPresent()) //will throw an exception without this. WORKS
            //If subscribed
            .filter(slotDTO -> subscribersSteamDiscordChatIds.containsKey(
                    slotDTO.getSteamId().get().substring(9)
                )
            )//WORKS
            //If an individual has not pressed Ready! yet
            .filter(slotDTO -> !slotDTO.getPersonIsReady().get())
            //If not notified via Discord yet
            .filter(slotDTO -> !NOTIFIED_PLAYERS.contains
                (new NotifiedPlayerDestructor(slotDTO.getSteamId().get()))
            )
            .forEach(player -> {
                client.getChannelById(Snowflake.of(
                        subscribersSteamDiscordChatIds.get(player.getSteamId().get()).get(1)
                    ))
                    .ofType(MessageChannel.class)
                    .flatMap(channel -> channel.createMessage("Your lobby is ready❗"))
                    .block();

                NOTIFIED_PLAYERS.add(
                    new NotifiedPlayerDestructor(player.getSteamId().get(), NOTIFIED_PLAYERS)
                );

            });
    }
}
