package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.registr;

import com.google.common.collect.ImmutableList;

import java.util.List;

public interface ConsumersService {

    ImmutableList<Node> getNextNodes(int replication);

    void sendMessageTo(Message message, Node ... consumers);

}
