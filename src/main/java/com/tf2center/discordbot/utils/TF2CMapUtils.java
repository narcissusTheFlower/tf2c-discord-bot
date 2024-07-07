package com.tf2center.discordbot.utils;

import java.util.Map;
import java.util.Objects;

public class TF2CMapUtils {

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        return map.entrySet()
            .stream()
            .filter(entry -> Objects.equals(entry.getValue(), value))
            .map(Map.Entry::getKey)
            .findAny().get();
    }

}
