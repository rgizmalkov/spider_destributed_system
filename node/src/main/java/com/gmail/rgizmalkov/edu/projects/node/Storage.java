package com.gmail.rgizmalkov.edu.projects.node;

import lombok.NonNull;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {

    private ConcurrentHashMap<String, Serializable> storage = new ConcurrentHashMap<>(2^5);

    public String write(String uid, Serializable obj) {
        storage.putIfAbsent(
                uid, obj
        );
        return uid;
    }

    public @NonNull Serializable read(String uid){
        return storage.get(uid);
    }
}
