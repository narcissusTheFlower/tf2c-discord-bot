package com.tf2center.discordbot.dto.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

//TODO redo this with simple field jackson parse
public class TF2CSubstituteSlotContainer {

    @JsonProperty("data")
    private Set<TF2CSubstituteSlotDTO> substitutionSlot;

    public Set<TF2CSubstituteSlotDTO> getSubstitutionSlot() {
        return substitutionSlot;
    }
}
