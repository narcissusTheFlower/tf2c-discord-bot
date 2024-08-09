package com.tf2center.discordbot.parser.dto.tf2classes;

import java.util.Map;

public enum FoursClasses implements TF2Class {

    SCOUT(Map.of("scout", "⚾Scout1")),
    SOLDIER(Map.of("soldier", "\uD83D\uDE80Roamer")),
    DEMOMAN(Map.of("demoman", "🧨Demoman")),
    MEDIC(Map.of("medic", "💊Medic"));

    private final Map<String, String> discordClassValue;

    FoursClasses(Map<String, String> discordClassValue) {
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
