package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.ymlpojos;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class YmlMaster {

    String url;
    String name;
    List<String> activeNodes;
    int replication;

}
