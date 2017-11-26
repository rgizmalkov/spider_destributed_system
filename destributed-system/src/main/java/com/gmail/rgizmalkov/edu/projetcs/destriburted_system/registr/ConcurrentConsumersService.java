package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.registr;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class ConcurrentConsumersService implements ConsumersService {

    private final ConcurrentCircleArrayList<Node> nodes;



    @Override
    public ImmutableList<Node> getNextNodes(int replication) {
        return null;
    }

    @Override
    public void sendMessageTo(Message message, Node... consumers) {

    }
}
