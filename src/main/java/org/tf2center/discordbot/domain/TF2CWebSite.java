package org.tf2center.discordbot.domain;

import org.tf2center.discordbot.dto.TF2CPlayerCount;
import org.tf2center.discordbot.dto.json.TF2CSubstituteSlot;
import org.tf2center.discordbot.dto.json.tf2cpreview.TF2CLobbyPreview;
import org.tf2center.discordbot.dto.json.tf2lobby.TF2CLobby;
import org.tf2center.discordbot.exceptions.TF2CUpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@Component("tf2cWebSite")
public final class TF2CWebSite {
    private static final Logger logger = LoggerFactory.getLogger(TF2CWebSite.class);
    private static AtomicInteger playersCountTotal;
    private static AtomicInteger playersEU;
    private static AtomicInteger playersNA;
    private static AtomicInteger playersOther;
    private static Instant lastUpdate = Instant.now();
    private static final Set<TF2CLobby> lobbies = Collections.synchronizedSet(new LinkedHashSet<>());
    private static final Set<TF2CLobbyPreview> previews = Collections.synchronizedSet(new LinkedHashSet<>());
    private static final Set<TF2CSubstituteSlot> substitutionSpots = Collections.synchronizedSet(new LinkedHashSet<>());

    public static void update(@NonNull TF2CPlayerCount playerCount,
                              @NonNull Set<TF2CLobby> lobbies,
                              @NonNull Set<TF2CLobbyPreview> previews,
                              @NonNull Set<TF2CSubstituteSlot> substitutionSpots) {
        try {
            playersCountTotal = playerCount.getPlayersCountTotal();
            playersEU = playerCount.getPlayersEU();
            playersNA = playerCount.getPlayersNA();
            playersOther = playerCount.getPlayersOther();
            updateLobbies(lobbies);
            updatePreviews(previews);
            updateSubstitutionSlots(substitutionSpots);
            lastUpdate = Instant.now();
            logger.info("TF2CENTER OBJECT updated with %d opened lobbies, %d current players, %d substitution spots."
                    .formatted(previews.size(), playersCountTotal.get(), substitutionSpots.size()));
        } catch (RuntimeException exception) {
            throw new TF2CUpdateException("Failed to run update methods.", exception);
        }
    }

    private static void updateLobbies(Set<TF2CLobby> incomingLobbies) {
        if (!incomingLobbies.isEmpty()) {
            lobbies.clear();
            lobbies.addAll(incomingLobbies);
        }
    }

    private static void updatePreviews(Set<TF2CLobbyPreview> incomingPreviews) {
        if (!incomingPreviews.isEmpty()) {
            previews.clear();
            previews.addAll(incomingPreviews);
        }
    }

    private static void updateSubstitutionSlots(Set<TF2CSubstituteSlot> incomingSubstitutionSlots) {
        if (!incomingSubstitutionSlots.isEmpty()) {
            substitutionSpots.clear();
            substitutionSpots.addAll(incomingSubstitutionSlots);
        }
    }

    public static Set<TF2CLobby> getLobbies() {
        return Set.copyOf(lobbies);
    }

    public static Set<TF2CLobbyPreview> getPreviews() {
        return Set.copyOf(previews);
    }

    public static Set<TF2CSubstituteSlot> getSubstitutionSpots() {
        return Set.copyOf(substitutionSpots);
    }

    public static synchronized Instant getLastUpdate() {
        return lastUpdate;
    }

    public static AtomicInteger getPlayersCountTotal() {
        return playersCountTotal;
    }

    public static AtomicInteger getPlayersEU() {
        return playersEU;
    }

    public static AtomicInteger getPlayersNA() {
        return playersNA;
    }

    public static AtomicInteger getPlayersOther() {
        return playersOther;
    }
}
