package org.tf2center.discordbot.utils;

import org.springframework.lang.NonNull;
import org.tf2center.discordbot.exceptions.TF2CUtilsException;

import java.util.List;

public class CollectionsUtils<T extends List> {

    public Object getLastFromList(@NonNull T collection) {
        if (collection.isEmpty()) {
            throw new TF2CUtilsException("Collection is empty.", new RuntimeException());
        }
        return collection.get(collection.size() - 1);
    }

}
