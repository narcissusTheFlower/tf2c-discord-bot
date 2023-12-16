package com.tf2center.discordbot.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class TF2CSubstituteSlotContainer {
    @JsonProperty("data")
    private Set<TF2CSubstituteSlot> substitutionSlot;

    public Set<TF2CSubstituteSlot> getSubstitutionSlot() {
        return substitutionSlot;
    }
}
