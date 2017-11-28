package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.registr2;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

public class Node {

    private final Map<String, Serializable> inmemory;

    private final AtomicBoolean isActive;


    public void getMessage(){

    }
}
