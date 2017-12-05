package com.gmail.rgizmalkov.edu.projetcs.destriburted_system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.master.MasterSystem;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.node.QuiteNode;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.ymlpojos.DistributedSystemProperties;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.ymlpojos.YmlMaster;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.ymlpojos.YmlNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@SpringBootApplication
@Configuration
public class DistributedSystemApplication{

    public static void main(String[] args) {
        SpringApplication.run(DistributedSystemApplication.class, args);
    }

    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.build();
//        objectMapper.configure()
        return objectMapper;
    }

    @Bean("masterSystem")
    public MasterSystem createMasterSystem(DistributedSystemProperties properties) {
        System.out.println(properties);

        YmlMaster master = properties.getMaster();
        String name = master.getName();
        int replication = master.getReplication();
        String url = master.getUrl();

        MasterSystem masterSystem = new MasterSystem(replication, name, url);

        List<String> activeNodes = master.getActiveNodes();
        for (String activeNode : activeNodes) {
            YmlNode ymlNode = properties.getNodes().get(activeNode);
            if (ymlNode != null) {
                QuiteNode quiteNode = new QuiteNode(
                        new ConcurrentLinkedQueue<>(),
                        new ConcurrentHashMap<>(2 ^ 6),
                        ymlNode.getUrl(), master.getUrl(), activeNode
                );
                masterSystem.addNode(quiteNode);
            }
        }

        return masterSystem;
    }

}
