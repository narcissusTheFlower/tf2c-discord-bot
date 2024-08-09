package com.tf2center.discordbot.parser.dto.tf2classes;

import java.util.Map;

public enum UltiduoClasses implements TF2Class {

    SOLDIER(Map.of("soldier", "\uD83D\uDE80Soldier")),
    MEDIC(Map.of("medic", "💊Medic"));

    private final Map<String, String> discordClassValue;

    UltiduoClasses(Map<String, String> discordClassValue) {
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
