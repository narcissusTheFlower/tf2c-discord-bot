package com.tf2center.discordbot.dto.json.tf2clobby;

public class TF2CAvailableSlotDTO {

    private String team;
    private boolean reserved;
    private TF2CSlotRestrictionDTO restrictions;

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public TF2CSlotRestrictionDTO getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(TF2CSlotRestrictionDTO restrictions) {
        this.restrictions = restrictions;
    }
}
