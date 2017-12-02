package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.ymlpojos;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "distribution")
public class DistributedSystemProperties {

    YmlMaster master;

    Map<String, YmlNode> nodes;

}
