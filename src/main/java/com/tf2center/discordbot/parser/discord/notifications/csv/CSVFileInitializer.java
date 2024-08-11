package com.tf2center.discordbot.parser.discord.notifications.csv;

import com.tf2center.discordbot.parser.exceptions.TF2CCSVException;

import java.nio.file.Files;
import java.nio.file.Path;

public class CSVFileInitializer {


    static Path getSubscribersCSV() {
        Path subs;
        try {
            subs = Path.of(System.getenv("SUBS"));
        } catch (NullPointerException e) {
            throw new TF2CCSVException("Failed to find subscribers.csv file!");
        }
        if (subs != null && Files.isWritable(subs) && Files.isReadable(subs) && Files.exists(subs)) {
            return subs;
        } else {
            throw new TF2CCSVException("subscribers.csv file is unreachable.");
        }
    }

    static Path getAllPlayersCSV() {
        Path allPlayers;
        try {
            allPlayers = Path.of(System.getenv("ALL_PLAYERS"));
        } catch (NullPointerException e) {
            throw new TF2CCSVException("Failed to find all_players.csv file!");
        }
        if (allPlayers != null && Files.isWritable(allPlayers) && Files.isReadable(allPlayers) && Files.exists(allPlayers)) {
            return allPlayers;
        } else {
            throw new TF2CCSVException("all_players.csv file is unreachable.");
        }
    }

}
