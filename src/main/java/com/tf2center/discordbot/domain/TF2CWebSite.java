package com.tf2center.discordbot.domain;

import com.tf2center.discordbot.dto.TF2CLobby;
import com.tf2center.discordbot.dto.TF2CLobbyDTO;
import com.tf2center.discordbot.dto.TF2CPlayerCountDTO;
import com.tf2center.discordbot.dto.json.TF2CSubstituteSlotDTO;
import com.tf2center.discordbot.exceptions.TF2CUpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class represent the tf2center website. That includes lobbies, substitution slots and player count
 */
@Component("tf2cWebSite")
public final class TF2CWebSite {
    private static final Logger logger = LoggerFactory.getLogger(TF2CWebSite.class);
    private static AtomicInteger playersCountTotal;
    private static AtomicInteger playersEU;
    private static AtomicInteger playersNA;
    private static AtomicInteger playersOther;
    private static final Set<TF2CLobby> LOBBIES = Collections.synchronizedSet(new LinkedHashSet<>(6));
    private static final Set<TF2CSubstituteSlotDTO> SUBSTITUTE_SLOTS = Collections.synchronizedSet(new LinkedHashSet<>(6));

    public static void update(@NonNull TF2CPlayerCountDTO playerCount,
                              @NonNull Set<TF2CLobbyDTO> lobbies,
                              @NonNull Set<TF2CSubstituteSlotDTO> substitutionSpots) {
        try {
            updateLobbies(lobbies);
            updateSubstitutionSlots(substitutionSpots);

            playersCountTotal = playerCount.getPlayersCountTotal();
            playersEU = playerCount.getPlayersEU();
            playersNA = playerCount.getPlayersNA();
            playersOther = playerCount.getPlayersOther();

            logger.info("TF2CENTER OBJECT updated with %d opened lobbies, %d current players, %d substitution spots."
                    .formatted(lobbies.size(), playersCountTotal.get(), substitutionSpots.size()));
        } catch (RuntimeException exception) {
            throw new TF2CUpdateException("Failed to run update methods.", exception);
        }
    }

    private static void updateLobbies(Set<TF2CLobbyDTO> incomingLobbies) {
            LOBBIES.clear();
        if (!incomingLobbies.isEmpty())
            LOBBIES.addAll(incomingLobbies);

    }

    private static void updateSubstitutionSlots(Set<TF2CSubstituteSlotDTO> incomingSubstitutionSlots) {
        SUBSTITUTE_SLOTS.clear();
        if (!incomingSubstitutionSlots.isEmpty())
            SUBSTITUTE_SLOTS.addAll(incomingSubstitutionSlots);
    }

    public static Set<TF2CLobby> getLobbies() {
        return Set.copyOf(LOBBIES);
    }

    public static Set<TF2CSubstituteSlotDTO> getSubstituteSlots() {
        return Set.copyOf(SUBSTITUTE_SLOTS);
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
