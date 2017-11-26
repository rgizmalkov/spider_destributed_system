package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.registr;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Destributor {

    private final ConsumersService consumersService;

    private final int replicFactor;

    private final InfoWrapper wrapper;

    private final ConcurrentLinkedQueue<Serializable> queue;






}
