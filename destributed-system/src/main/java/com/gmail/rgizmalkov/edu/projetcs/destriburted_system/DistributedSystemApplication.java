package com.gmail.rgizmalkov.edu.projetcs.destriburted_system;

import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.master.MasterSystem;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.node.Node;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.node.QuiteNode;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.ymlpojos.DistributedSystemProperties;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.ymlpojos.YmlMaster;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.ymlpojos.YmlNode;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@SpringBootApplication
@Configuration
public class DistributedSystemApplication{

    public static void main(String[] args) {
        SpringApplication.run(DistributedSystemApplication.class, args);
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
                        ymlNode.getUrl(), ymlNode.getName(), activeNode
                );
                masterSystem.addNode(quiteNode);
            }
        }

        return masterSystem;
    }

}
