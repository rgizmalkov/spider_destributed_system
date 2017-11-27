package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.registr;

import com.google.common.collect.ImmutableList;

public class ConcurrentIterableConsumersService implements ConsumersService {

    private final ConcurrentCircleLinkedList<Node> nodes;

    @Override
    public ImmutableList<Node> getNextNodes(int replication) {
    }

    @Override
    public void sendMessageTo(Message message, Node... consumers) {

    }
}
