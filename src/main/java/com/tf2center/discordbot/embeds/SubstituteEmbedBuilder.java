package com.tf2center.discordbot.embeds;

import com.tf2center.discordbot.dto.json.TF2CSubstituteSlotDTO;
import discord4j.core.spec.EmbedCreateSpec;

import java.util.Set;

public final class SubstituteEmbedBuilder {


    private SubstituteEmbedBuilder(Set<TF2CSubstituteSlotDTO> jsonParsedPreviews) {
        //this.jsonParsedPreviews = jsonParsedPreviews;
    }

    public static SubstituteEmbedBuilder of(Set<TF2CSubstituteSlotDTO> previews) {
        return new SubstituteEmbedBuilder(previews);
    }

    public Set<EmbedCreateSpec> build() {
        return null;
    }
}
