package com.tf2center.discordbot.parser.dto.tf2classes;

import java.util.Map;

public enum HighlanderClasses implements TF2Class {

    SCOUT(Map.of("scout", "⚾Scout1")),
    SOLDIER(Map.of("solly", "\uD83D\uDE80Soldier")),
    PYRO(Map.of("pyro", "🔥Pyro")),
    DEMOMAN(Map.of("demoman", "🧨Demoman")),
    HEAVY(Map.of("heavy", "🐤Heavy")),
    ENGINEER(Map.of("engie", "\uD83D\uDD27Engineer")),
    MEDIC(Map.of("medic", "💊Medic")),
    SNIPER(Map.of("sniper", "🎯Sniper")),
    SPY(Map.of("spy", "\uD83D\uDD2ASpy"));


    private final Map<String, String> discordClassValue;

    HighlanderClasses(Map<String, String> discordClassValue) {
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
