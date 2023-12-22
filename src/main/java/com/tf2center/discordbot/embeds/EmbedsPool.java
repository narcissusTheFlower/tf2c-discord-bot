package com.tf2center.discordbot.embeds;

import com.tf2center.discordbot.domain.TF2CWebSite;
import com.tf2center.discordbot.dto.TF2CLobby;
import com.tf2center.discordbot.exceptions.TF2CUpdateException;
import discord4j.core.spec.EmbedCreateSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class serves as a single point of reference for getting embeds that represent a lobby in any state.
 */
@Component("embedsPool")
@EnableScheduling
public class EmbedsPool {

    private static final Logger logger = LoggerFactory.getLogger(EmbedsPool.class);
    private static final Set<EmbedCreateSpec> LOBBIES = Collections.synchronizedSet(new LinkedHashSet<>());
    private static final Set<EmbedCreateSpec> SUBSTITUTION_SLOTS = Collections.synchronizedSet(new LinkedHashSet<>()); //Still thinking

    @Scheduled(fixedRate = 10_000, initialDelay = 2000)
    public static void updatePool() {
        try {
            Set<TF2CLobby> lobbies = TF2CWebSite.getLobbies();
            buildEmbedLobbies(lobbies);
            //buildSubstituteSlots(TF2CWebSite.getSubstitutionSpots());
        } catch (RuntimeException e) {
            throw new TF2CUpdateException("Failed to update embeds pool.", e);
        }

        logger.info("EMBEDS POOL updated with {} lobbies(-s) X substitution slots", LOBBIES.size());
    }

    private static void buildEmbedLobbies(Set<TF2CLobby> lobbies) {
        LOBBIES.clear();
        Set<EmbedCreateSpec> embeds = LobbyPreviewEmbedBuilder.of(lobbies).build();
        if (!embeds.isEmpty()) {
            LOBBIES.addAll(embeds);
        }
    }

    public static Set<EmbedCreateSpec> getLobbies() {
        return Set.copyOf(LOBBIES);
    }

    public static EmbedCreateSpec retrieveFirst() {
        return (EmbedCreateSpec) LOBBIES.toArray()[0];
    }

//
//    private static void buildSubstituteSlots(Set<TF2CSubstituteSlotDTO> substitutionSpots) {
//    }
//
//    public static Set<EmbedCreateSpec> getSubstitutionSlots() {
//        return null;
//    }

}
