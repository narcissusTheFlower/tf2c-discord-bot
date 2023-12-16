package org.tf2center.discordbot.embeds;

import org.tf2center.discordbot.domain.TF2CWebSite;
import org.tf2center.discordbot.dto.json.TF2CSubstituteSlot;
import org.tf2center.discordbot.dto.json.tf2cpreview.TF2CLobbyPreview;
import org.tf2center.discordbot.dto.json.tf2lobby.TF2CLobby;
import org.tf2center.discordbot.exceptions.TF2CUpdateException;
import discord4j.core.spec.EmbedCreateSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component("embedsPool")
@EnableScheduling
public class EmbedsPool {

    private static final Logger logger = LoggerFactory.getLogger(EmbedsPool.class);

    /**
     * Ready to go embeds acquired by the slash commands
     */

    //There might be multiple lobby embeds
    private static final Set<EmbedCreateSpec> LOBBIES = new CopyOnWriteArraySet<>();

    //There can be only 1 preview embed. It's a collection for convenience and it will only contain 1 element
    private static final Set<EmbedCreateSpec> PREVIEWS = new CopyOnWriteArraySet<>();

    //Still thinking
    private static final Set<EmbedCreateSpec> SUBSTITUTION_SLOTS = new CopyOnWriteArraySet<>();


    //@Scheduled(fixedRate = 10_000, initialDelay = 2000)
    public static void updatePool() {
        try {
            buildPreview(TF2CWebSite.getPreviews());
            buildLobbies(TF2CWebSite.getLobbies());
            buildSubstituteSlots(TF2CWebSite.getSubstitutionSpots());
        } catch (RuntimeException e) {
            throw new TF2CUpdateException("Failed to update embeds pool.", e);
        }
        logger.info("EMBEDS POOL updated with {} preview, X lobbies, X substitution slots", PREVIEWS.size());

    }

    private static synchronized void buildPreview(Set<TF2CLobbyPreview> previews) {
        PREVIEWS.clear();
        PREVIEWS.addAll(LobbyPreviewEmbedBuilder.of(previews).build());
    }

    private static synchronized void buildLobbies(Set<TF2CLobby> previews) {

    }

    private static synchronized void buildSubstituteSlots(Set<TF2CSubstituteSlot> substitutionSpots) {

    }

    public static synchronized EmbedCreateSpec getPreview() {
        return (EmbedCreateSpec) PREVIEWS.toArray()[0];
        //return mainPreview.stream().findFirst().get();
    }

    public static synchronized EmbedCreateSpec getNextLobby() {
        //TODO make is so that ppl can go through different lobbies
        return null;
    }

    public static synchronized EmbedCreateSpec getSubstitutionSlots() {

        return null;
    }

    private static void assemblePreview(Set<TF2CLobbyPreview> previews) {
//        previews.forEach(
//                preview -> {
//                    preview.
//                }
//        );

    }


}
