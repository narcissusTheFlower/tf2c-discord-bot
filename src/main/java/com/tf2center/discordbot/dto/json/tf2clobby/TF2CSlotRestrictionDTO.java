package com.tf2center.discordbot.dto.json.tf2clobby;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TF2CSlotRestrictionDTO {

    private String games;

    public String getGames() {
        return games;
    }

    public void setGames(String games) {
        this.games = games;
    }
}
