package com.tf2center.discordbot.parser.discord.notifications;

import com.tf2center.discordbot.parser.exceptions.TF2CCSVException;
import discord4j.common.util.Snowflake;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CSVActions {

    private static final Path SUBSCRIBERS = Path.of(System.getenv("SUBSCRIBERS_CSV"));
    private static final Path ALL_PLAYERS = Path.of(System.getenv("ALL_PLAYERS_CSV"));

    private CSVActions() {
    }

    static synchronized Map<String, List<String>> readSubscribers() {
        Map<String, List<String>> steamDiscordIds = new HashMap<>();
        try (
                Reader reader = Files.newBufferedReader(SUBSCRIBERS);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        ) {
            csvParser.getRecords().stream().skip(1).forEach(e -> {
                steamDiscordIds.put(
                        e.get(0),
                        List.of(e.get(1), e.get(2))
                );
            });
        } catch (IOException e) {
            throw new TF2CCSVException("Failed to read SUBSCRIBERS csv file.", e);
        }
        return steamDiscordIds;
    }

    public static synchronized void appendToSubscribers(Snowflake discordUserId, Snowflake chatId) {
        try {
            Map<String, List<String>> SubscribersSteamDiscordIds = readSubscribers();
            Map<String, String> AllPlayersSteamDiscordIds = readAll();
            emptyCSV(SUBSCRIBERS);

            String steamID = getKeyByValue(AllPlayersSteamDiscordIds, discordUserId.asString());
            SubscribersSteamDiscordIds.put(
                    steamID,
                    List.of(discordUserId.asString(), chatId.asString())
            );

            BufferedWriter writer = Files.newBufferedWriter(SUBSCRIBERS);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("SteamID", "DiscordId", "ChatId"));

            SubscribersSteamDiscordIds.forEach((steamId, list) -> {
                try {
                    csvPrinter.printRecord(steamId, list.get(0), list.get(1));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            csvPrinter.flush();
            writer.close();
            csvPrinter.close();
        } catch (IOException e) {
            throw new TF2CCSVException("Failed to APPEND IDs to the SUBSCRIBERS .csv file.", e);
        }
    }

    public static synchronized void deleteFromSubscribers(Snowflake discordId) {
        try {
            Map<String, List<String>> steamDiscordIds = readSubscribers();
            emptyCSV(SUBSCRIBERS);
            steamDiscordIds.values().removeIf(value -> value.contains(discordId.asString()));

            BufferedWriter writer = Files.newBufferedWriter(SUBSCRIBERS);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("SteamID", "DiscordId", "ChatId"));

            steamDiscordIds.forEach((steamId, list) -> {
                try {
                    csvPrinter.printRecord(steamId, list.get(0), list.get(1));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            csvPrinter.flush();
            writer.close();
            csvPrinter.close();
        } catch (IOException e) {
            throw new TF2CCSVException("Failed to delete from SUBSCRIBERS csv file.", e);
        }
    }

    static synchronized Map<String, String> readAll() {
        Map<String, String> steamDiscordIds = new HashMap<>();
        try (
                Reader reader = Files.newBufferedReader(ALL_PLAYERS);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        ) {
            csvParser.getRecords().stream().skip(1).forEach(e -> {
                steamDiscordIds.put(e.get(0), e.get(1));
            });
        } catch (IOException e) {
            throw new TF2CCSVException("Failed to read ALL_PLAYERS csv file.", e);
        }
        return steamDiscordIds;
    }

    static synchronized void appendToAll(String steamId, String discordId) {
        try {
            Map<String, String> allSteamDiscordIds = readAll();
            emptyCSV(ALL_PLAYERS);
            allSteamDiscordIds.put(steamId, discordId);

            BufferedWriter writer = Files.newBufferedWriter(ALL_PLAYERS);
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("SteamID", "DiscordId"));

            allSteamDiscordIds.forEach((k, v) -> {
                try {
                    csvPrinter.printRecord(k, v);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            csvPrinter.flush();
            writer.close();
            csvPrinter.close();
        } catch (IOException e) {
            throw new TF2CCSVException("Failed to APPEND IDs to the SUBSCRIBERS .csv file.", e);
        }
    }

    static private void emptyCSV(Path csvFile) {
        File csv = new File(csvFile.toString());
        try (FileWriter fileWriter = new FileWriter(csv)) {
            String blank = "";
            fileWriter.write(blank);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .findAny().get();
    }

}
