package com.tf2center.discordbot.parser.discord.notifications.csv;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

@Component
@EnableScheduling
public class CSVBackup {

    private static final Path subscribersCSV = Path.of(System.getenv("SUBS"));
    private static final Path target = Path.of(System.getenv("SUBSCRIBERS_ARCHIVE") + Instant.now().toString());

    @Scheduled(fixedRate = 1_800_000) //Every 30 minutes
    private void backup() {
        try {
            Files.copy(subscribersCSV, target);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create a copy of subscribers.csv", e);
        }
    }


}
