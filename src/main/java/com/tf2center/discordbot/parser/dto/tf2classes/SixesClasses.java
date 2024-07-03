package com.tf2center.discordbot.parser.dto.tf2classes;

import java.util.Map;

public enum SixesClasses implements TF2Class {

    SCOUT1(Map.of("scout", "⚾Scout1")),
    SCOUT2(Map.of("scout", "⚾Scout1")),
    SOLDIER_ROAMER(Map.of("soldier_roamer", "\uD83D\uDE80Roamer")),
    SOLDIER_POCKET(Map.of("soldier_pocket", "\uD83D\uDE80Pocket")),
    DEMOMAN(Map.of("demoman", "🧨Demoman")),
    MEDIC(Map.of("medic", "💊Medic"));

    private final Map<String, String> discordClassValue;

    SixesClasses(Map<String, String> discordClassValue) {
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
