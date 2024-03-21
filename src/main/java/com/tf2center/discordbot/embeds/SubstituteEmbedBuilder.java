package com.tf2center.discordbot.embeds;

import com.tf2center.discordbot.dto.json.TF2CSubstituteSlotDTO;
import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Set;

public final class SubstituteEmbedBuilder {

    private final Set<TF2CSubstituteSlotDTO> substituteSlots;

    private SubstituteEmbedBuilder(Set<TF2CSubstituteSlotDTO> substituteSlots) {
        this.substituteSlots = substituteSlots;
    }

    public static SubstituteEmbedBuilder of(Set<TF2CSubstituteSlotDTO> previews) {
        return new SubstituteEmbedBuilder(previews);
    }

    public EmbedCreateSpec build() {
        EmbedCreateSpec test = EmbedCreateSpec.builder()
                .color(Color.of(133, 63, 63)) //From tf2c colour pallet. Redish
                .title("Substitute slots to join.")
                .description(substituteSlots.isEmpty() ? "No substitute slots at this moment" : "")
                .addFields(buildFields())
                //TODO chacge source of the image
                .image("https://raw.githubusercontent.com/narcissusTheFlower/tf2-stats-browser-extension/c13a5552564c58fd06b1dcaa19357b419f4761b9/discord-bot-firestarter.png")
                .build();
        return test;
    }

    private EmbedCreateFields.Field[] buildFields() {
        EmbedCreateFields.Field[] subs = new EmbedCreateFields.Field[substituteSlots.size()];
        int counter = 0;
        for (TF2CSubstituteSlotDTO substituteSlot : substituteSlots) {
            subs[counter] = EmbedCreateFields.Field.of(
                    buildName(substituteSlot),
                    buildJoinLink(substituteSlot),
                    false);
            counter++;
        }
        return ArrayUtils.addAll(subs);
    }

    private String buildName(TF2CSubstituteSlotDTO slot) {
        return new StringBuilder()
                .append(slot.getClassName())
                .append(" | ")
                .append(slot.getRegion())
                .append(" | ")
                .append(slot.getGameType())
                .append(" | ")
                .append(slot.getMap())
                .toString();
    }

    private String buildJoinLink(TF2CSubstituteSlotDTO slot) {
        return new StringBuilder()
                .append("[Join](https://tf2center.com")
                .append(slot.getJoinLink())
                .append(")")
                .toString();
    }

}
