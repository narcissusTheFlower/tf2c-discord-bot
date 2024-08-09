package com.tf2center.discordbot.parser.dto.tf2classes;

import java.util.Map;

public enum BBallClasses implements TF2Class {

    SOLDIER1(Map.of("soldier_roamer", "\uD83D\uDE80Soldier")),
    SOLDIER2(Map.of("soldier_pocket", "\uD83D\uDE80Soldier"));

    private final Map<String, String> discordClassValue;

    BBallClasses(Map<String, String> discordClassValue) {
        this.discordClassValue = discordClassValue;
    }

    @Override
    public String getDiscordClassValue() {
        return discordClassValue.values().iterator().next().toString();
    }

    @Override
    public String getDiscordClassKey() {
        return discordClassValue.keySet().iterator().next().toString();
    }


}
