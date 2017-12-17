package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.ymlpojos;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class YmlMaster {

    String master_host;
    Map<String, List<String>> clusters;

}
