package com.gmail.rgizmalkov.edu.projects.node;

import lombok.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {

    private ConcurrentHashMap<String, String> storage = new ConcurrentHashMap<>(2^5);

    public String write(String uid, String obj) {
        storage.putIfAbsent(
                uid, obj
        );
        return uid;
    }

    public HashMap<String, String> getStorage() {
        return new HashMap<String, String>(storage);
    }

    public String get(String uid){
        return storage.get(uid);
    }

    public @NonNull Serializable read(String uid){
        return storage.get(uid);
    }
}
