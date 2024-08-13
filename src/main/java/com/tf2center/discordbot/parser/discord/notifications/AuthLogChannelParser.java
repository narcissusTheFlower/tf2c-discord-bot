package com.tf2center.discordbot.parser.discord.notifications;

import com.tf2center.discordbot.parser.discord.notifications.csv.CSVActions;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.Embed;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Scheduled(fixedRate = 10_000)
    public void parseLogChannel() {

        List<Message> messages = textChannel.ofType(GuildMessageChannel.class)
                .map(channel -> channel.getMessagesBefore(Snowflake.of(Instant.now()))
                    .take(5)
                    .collectList()
                    .block()
                ).block();

        Map<String, String> steamDiscordIds = parseIds(Objects.requireNonNull(messages));

        Map<String, String> steamDiscordIdsFromCSV = CSVActions.getInstance().readAll();

        steamDiscordIds.forEach((steamId, discordId) -> {
            if (steamDiscordIdsFromCSV.containsKey(steamId)) {
                CSVActions.getInstance().appendToAll(steamId, discordId);
            }
        });
    }

    private Map<String, String> parseIds(List<Message> messages) {
        Map<String, String> steamDiscordIds = new LinkedHashMap<>();
        messages.stream()
            .filter(message -> !message.getEmbeds().isEmpty())
            .filter(message -> message.getEmbeds().get(0).getFields().size() > 3) //Sort out not needed embeds
            .forEach(message -> {
                List<Embed.Field> fields = message.getEmbeds().get(0).getFields(); //Get fields with discord and steamid
                steamDiscordIds.put(
                    fields.get(1).getValue(),
                    fields.get(0).getValue()
                        .replace("<", "")
                        .replace(">", "")
                        .replace("@", ""));
            });
        return steamDiscordIds;
    }
}
