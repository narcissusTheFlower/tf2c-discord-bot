package com.tf2center.discordbot.utils;

import com.tf2center.discordbot.exceptions.TF2CUtilsException;
import org.springframework.lang.NonNull;

import java.util.List;

public class TF2CCollectionsUtils {

    public static <T extends List<?>> Object getLastFromList(@NonNull T collection) {
        if (collection.isEmpty()) {
            throw new TF2CUtilsException("Collection is empty.", new RuntimeException());
        }
        return collection.get(collection.size() - 1);
    }


}
