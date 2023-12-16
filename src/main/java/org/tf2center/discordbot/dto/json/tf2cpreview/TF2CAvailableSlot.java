package org.tf2center.discordbot.dto.json.tf2cpreview;

public class TF2CAvailableSlot {

    private String team;
    private boolean reserved;
    private TF2CSlotRestriction restrictions;

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

    public TF2CSlotRestriction getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(TF2CSlotRestriction restrictions) {
        this.restrictions = restrictions;
    }
}
