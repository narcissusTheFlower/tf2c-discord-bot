package com.tf2center.discordbot.embeds;

import com.tf2center.discordbot.domain.TF2CWebSite;
import com.tf2center.discordbot.dto.json.tf2cpreview.TF2CLobbyPreview;
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

@Component("embedsPool")
@EnableScheduling
public class EmbedsPool {

    private static final Logger logger = LoggerFactory.getLogger(EmbedsPool.class);
    private static final Set<EmbedCreateSpec> PREVIEWS = Collections.synchronizedSet(new LinkedHashSet<>());
    private static final Set<EmbedCreateSpec> SUBSTITUTION_SLOTS = Collections.synchronizedSet(new LinkedHashSet<>()); //Still thinking

    @Scheduled(fixedRate = 10_000, initialDelay = 2000)
    public static void updatePool() {
        try {
            buildPreviews(TF2CWebSite.getPreviews());
            //buildSubstituteSlots(TF2CWebSite.getSubstitutionSpots());
        } catch (RuntimeException e) {
            throw new TF2CUpdateException("Failed to update embeds pool.", e);
        }
        logger.info("EMBEDS POOL updated with {} preview(-s) X substitution slots", PREVIEWS.size());
    }

    private static void buildPreviews(Set<TF2CLobbyPreview> previews) {
        PREVIEWS.clear();
        PREVIEWS.addAll(
                LobbyPreviewEmbedBuilder.of(previews).build()
        );
    }

//    public static Set<EmbedCreateSpec> getPreviews() {
//        return Set.copyOf(PREVIEWS);
//    }
//
//    private static void buildSubstituteSlots(Set<TF2CSubstituteSlot> substitutionSpots) {
//    }
//
//    public static Set<EmbedCreateSpec> getSubstitutionSlots() {
//        return null;
//    }

}
