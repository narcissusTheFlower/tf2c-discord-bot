package com.tf2center.discordbot.parser.discord.notifications;

import com.tf2center.discordbot.parser.exceptions.TF2CCSVException;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.Embed;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("singleton")
//TODO: Move this into a separate program
public class AuthLogChannelParser {

    private final Mono<Channel> textChannel;

    private static final String CSV_FILE = "/home/user/IdeaProjects/new-discord-bot/test.csv";
    @Autowired
    public AuthLogChannelParser(GatewayDiscordClient client) {
        textChannel = client.getChannelById(
            Snowflake.of(Long.parseLong(System.getenv("TF2CAUTH")))
        );
    }

    private static Map<String, String> readCSV() {
        Map<String, String> result = new LinkedHashMap<>();
        return result;
    }

    //    @Scheduled(fixedRate = 30_000)
    public void parseLogChannel() {
        Map<String, String> existingSteamDiscordIds = readCSV();
        List<Message> messages = textChannel.ofType(GuildMessageChannel.class)
            .map(channel -> channel.getMessagesBefore(Snowflake.of(Instant.now()))
                .take(5)
                .collectList()
                .block()
            ).block();

        Map<String, String> steamDiscordIds = parseIds(messages);

        //if(csvCollection.contains(steamId) || csvCollection.contains(discordId)){
        // return;
        //}
        //writeCSV();
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

    //TODO: Make sure its appending
    private void writeToCSV(Map<String, String> steamDiscordIds) {
        try (
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FILE));

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader("SteamID", "DiscordId"));
        ) {
            steamDiscordIds.forEach((steamId, discordId) -> {
                try {
                    csvPrinter.printRecord(steamId, discordId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            csvPrinter.flush();
        } catch (IOException e) {
            throw new TF2CCSVException("Failed to write IDs to the .csv file.", e);
        }
    }




}
