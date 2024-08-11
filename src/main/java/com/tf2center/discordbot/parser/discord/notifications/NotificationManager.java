package com.tf2center.discordbot.parser.discord.notifications;

import com.tf2center.discordbot.parser.discord.notifications.csv.CSVActions;
import com.tf2center.discordbot.parser.dto.LobbyDTO;
import com.tf2center.discordbot.parser.dto.MainPageObject;
import com.tf2center.discordbot.parser.dto.SlotDTO;
import com.tf2center.discordbot.parser.dto.TF2Team;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope("singleton")
public class NotificationManager {

    private static final Logger logger = LoggerFactory.getLogger(NotificationManager.class);
    private final GatewayDiscordClient client;
    private static final Set<NotifiedPlayerDestructor> NOTIFIED_PLAYERS = Collections.synchronizedSet(new HashSet<>());

    @Autowired
    public NotificationManager(GatewayDiscordClient client) {
        this.client = client;
    }

    public void manage(Collection<MainPageObject> lobbies) {
        Set<Subscriber> subscribers = new LinkedHashSet<>();
        CSVActions.readSubscribers().forEach((steamId, listDiscordChatId) -> {
            subscribers.add(
                Subscriber.of(
                    steamId, //steamId
                    listDiscordChatId.get(0), //discordId
                    listDiscordChatId.get(1) //discordChatId
                )
            );
        });

        List<SlotDTO> allPlayersInLobby = new ArrayList<>();
        //Optional[/profile/76561198042755731]
        allPlayersInLobby.add(SlotDTO.of("narcissus", "/profile/76561198106563151", false, TF2Team.BLU, false));

        lobbies.stream()
            .map(e -> (LobbyDTO) e)
            .filter(LobbyDTO::isReady)
            .forEach(lobby -> lobby.getTeams().values().forEach(allPlayersInLobby::addAll));

        if (allPlayersInLobby.isEmpty()) return;

        List<SlotDTO> filteredPlayers = allPlayersInLobby.stream()
            .filter(slotDTO -> !slotDTO.getSteamId().isEmpty()) //will throw an exception without this
            //If subscribed
            .filter(slotDTO -> subscribers.contains(
                Subscriber.of(slotDTO.getSteamId())
            ))
            .filter(slotDTO -> !slotDTO.getPersonIsReady())
            .filter(slotDTO -> !NOTIFIED_PLAYERS.contains(new NotifiedPlayerDestructor(slotDTO.getSteamId())))
            .toList();

        filteredPlayers.forEach(player -> {

            //Find snowflake
            Optional<Subscriber> subscriber = subscribers
                .stream()
                .filter(sub -> sub.getSteamId().equals(player.getSteamId()))
                .findFirst();

            if (subscriber.isPresent()) {
                client.getChannelById(Snowflake.of(
                        subscriber.get().getDiscordChatId()
                    ))
                    .ofType(MessageChannel.class)
                    .flatMap(channel -> channel.createMessage("Your lobby is ready❗"))
                    .block();

                NOTIFIED_PLAYERS.add(
                    new NotifiedPlayerDestructor(player.getSteamId(), NOTIFIED_PLAYERS)
                );
            } else {
                logger.info("Something is wrong!");
            }
            logger.info("Notified players collection size is {}", NOTIFIED_PLAYERS.size());
        });

    }
}
