package com.tf2center.discordbot.dto.json.tf2cpreview;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class represents a slot in a team in a lobby.
 */
public class TF2CSlot {
    private String tf2Class;
    private TF2CAvailableSlot[] availableSlots;
    @JsonIgnore
    private Object[] availableReservedSlots;

    public String getTf2Class() {
        return tf2Class;
    }

    public void setTf2Class(String tf2Class) {
        this.tf2Class = tf2Class;
    }

    public TF2CAvailableSlot[] getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(TF2CAvailableSlot[] availableSlots) {
        this.availableSlots = availableSlots;
    }

    public Object[] getAvailableReservedSlots() {
        return availableReservedSlots;
    }

    public void setAvailableReservedSlots(Object[] availableReservedSlots) {
        this.availableReservedSlots = availableReservedSlots;
    }
}
