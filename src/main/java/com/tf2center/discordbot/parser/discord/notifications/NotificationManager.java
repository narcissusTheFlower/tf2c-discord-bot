package com.tf2center.discordbot.parser.discord.notifications;

import com.tf2center.discordbot.parser.discord.notifications.csv.CSVActions;
import com.tf2center.discordbot.parser.dto.LobbyDTO;
import com.tf2center.discordbot.parser.dto.MainPageObject;
import com.tf2center.discordbot.parser.dto.SlotDTO;
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
    private final NotifiedPlayersHolder notifiedPlayersHolder;

    @Autowired
    public NotificationManager(GatewayDiscordClient client, NotifiedPlayersHolder notifiedPlayersHolder) {
        this.client = client;
        this.notifiedPlayersHolder = notifiedPlayersHolder;
    }

    public void manage(Collection<MainPageObject> lobbies) {
        Set<Subscriber> subscribers = new LinkedHashSet<>();
        CSVActions.getInstance().readSubscribers().forEach((steamId, listDiscordChatId) -> {
            subscribers.add(
                Subscriber.of(
                    steamId, //steamId
                    listDiscordChatId.get(0), //discordId
                    listDiscordChatId.get(1) //discordChatId
                )
            );
        });

        List<SlotDTO> allPlayersInLobby = new ArrayList<>();
        //allPlayersInLobby.add(SlotDTO.of("narcissus", "/profile/76561198106563151", false, TF2Team.BLU, false));

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
            //If not ready
            .filter(slotDTO -> !slotDTO.getPersonIsReady())
            //If not in yet notified
            .filter(slotDTO -> notifiedPlayersHolder.getNotifiedPlayers().contains(NotifiedPlayerDestructor.of(slotDTO.getSteamId())))
            //Collecting for better debugging
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

                notifiedPlayersHolder.getNotifiedPlayers().add(
                    NotifiedPlayerDestructor.of(player.getSteamId())
                );
            } else {
                logger.info("Something is wrong!");
            }
            logger.info("Notified players collection size is {}", notifiedPlayersHolder.getNotifiedPlayers().size());
        });

    }

    public void manageWithLobbies(Collection<MainPageObject> lobbies) {
        Set<Subscriber> subscribers = new LinkedHashSet<>();
        CSVActions.getInstance().readSubscribers().forEach((steamId, listDiscordChatId) -> {
            subscribers.add(
                Subscriber.of(
                    steamId, //steamId
                    listDiscordChatId.get(0), //discordId
                    listDiscordChatId.get(1) //discordChatId
                )
            );
        });

        lobbies.stream()
            .map(e -> (LobbyDTO) e)
            .filter(LobbyDTO::isReady)
            .forEach(lobby -> {

                List<SlotDTO> allPlayersInLobby = new ArrayList<>();
                //allPlayersInLobby.add(SlotDTO.of("narcissus", "/profile/76561198106563151", false, TF2Team.BLU, false));
                lobby.getTeams().values().forEach(allPlayersInLobby::addAll);

                List<SlotDTO> filteredPlayers = allPlayersInLobby.stream()
                    .filter(slotDTO -> !slotDTO.getSteamId().isEmpty()) //will throw an exception without this
                    //If subscribed
                    .filter(slotDTO -> subscribers.contains(
                        Subscriber.of(slotDTO.getSteamId())
                    ))
                    //If not ready
                    .filter(slotDTO -> !slotDTO.getPersonIsReady())
                    //If not in yet notified
                    .filter(slotDTO -> notifiedPlayersHolder.getNotifiedPlayers().contains(
                        NotifiedPlayerDestructor.of(slotDTO.getSteamId()))
                    )
                    //Collecting for handier debugging
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
                            .flatMap(
                                channel -> channel.createMessage("Your [" + lobby.getGameType() + "|" + lobby.getMap() + "] is ready.")
                            )
                            .block();

                        notifiedPlayersHolder.getNotifiedPlayers().add(
                            NotifiedPlayerDestructor.of(player.getSteamId())
                        );
                    } else {
                        logger.info("Something is wrong!");
                    }
                    logger.info("Notified players collection size is {}", notifiedPlayersHolder.getNotifiedPlayers().size());
                });
            });
    }
}
