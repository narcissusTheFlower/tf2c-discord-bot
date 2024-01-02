package com.tf2center.discordbot.embeds;

import com.tf2center.discordbot.domain.TF2CWebSite;
import com.tf2center.discordbot.dto.TF2CLobbyIdDTO;
import com.tf2center.discordbot.exceptions.TF2CUpdateException;
import discord4j.core.spec.EmbedCreateSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * This class serves as a single point of reference for getting embeds that represent a lobby in any state.
 */
@Component("embedsPool")
@Scope("singleton")
@EnableScheduling
public class EmbedsPool {

    private static final Logger logger = LoggerFactory.getLogger(EmbedsPool.class);
    private static final Map<TF2CLobbyIdDTO, EmbedCreateSpec> EMBED_LOBBIES_POOL = Collections.synchronizedMap(new HashMap<>(10));
    private static final Set<EmbedCreateSpec> EMBED_SUBSTITUTION_POOL = Collections.synchronizedSet(new LinkedHashSet<>(10));

    @Scheduled(fixedRate = 10_000, initialDelay = 2000)// Order of scheduled: 2
    public static void updatePool() {
        try {
            EMBED_LOBBIES_POOL.clear();
            EMBED_SUBSTITUTION_POOL.clear();

            Map<TF2CLobbyIdDTO, EmbedCreateSpec> lobbyEmbeds = LobbyEmbedBuilder.of(TF2CWebSite.getLobbies()).build();
            EMBED_LOBBIES_POOL.putAll(lobbyEmbeds);

            //Set<EmbedCreateSpec> substituteEmbeds = SubstituteEmbedBuilder.of(TF2CWebSite.getSubstituteSlots()).build();
            //EMBED_SUBSTITUTION_POOL.addAll(substituteEmbeds);
        } catch (RuntimeException e) {
            throw new TF2CUpdateException("Failed to update embeds pool.", e);
        }

        logger.info("EMBEDS POOL updated with {} lobbies(-s) X substitution slots", EMBED_LOBBIES_POOL.size());
    }

    public static Map<TF2CLobbyIdDTO, EmbedCreateSpec> getFreshLobbies() {
        return Map.copyOf(EMBED_LOBBIES_POOL);
    }

    public static Set<EmbedCreateSpec> getFreshSubs() {
        return Set.copyOf(EMBED_SUBSTITUTION_POOL);
    }

}
