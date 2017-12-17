package com.gmail.rgizmalkov.edu.projetcs.destriburted_system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.rgizmalkov.edu.projects.vo.TaskResult;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.cluster.Cluster;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.cluster.SpiderCluster;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.master.LocalHistoryController;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.master.MasterSystem;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.master.NodeMap;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.cluster.NodeManager;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.ymlpojos.DistributedSystemProperties;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.ymlpojos.YmlMaster;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.ymlpojos.YmlNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.*;
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

    @Bean("historySystem")
    public LocalHistoryController localHistoryController(DistributedSystemProperties properties){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize( 5);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("history-");
        executor.initialize();

        Map<String, List<TaskResult>> map = new HashMap<>();

        YmlMaster master = properties.getMaster();
        Map<String, List<String>> clusters = master.getClusters();
        List<String> activeNodes = new ArrayList<>();
        for (String s : clusters.keySet()) {
            activeNodes.addAll(clusters.get(s));
        }
        for (String activeNode : activeNodes) {
            map.put(activeNode, Collections.synchronizedList(new ArrayList<>()));
        }
        return new LocalHistoryController(executor, map);
    }

    @Bean
    public List<Cluster> clusters(LocalHistoryController localHistoryController, DistributedSystemProperties properties) {
        List<Cluster> clusters = new ArrayList<>();
        YmlMaster master = properties.getMaster();
        String master_host = master.getMaster_host();
        Map<String, List<String>> clustersMap = master.getClusters();
        for (List<String> nodesOnCluster : clustersMap.values()) {
            Map<String, NodeManager> map = new HashMap<>();
            for (String node : nodesOnCluster) {
                YmlNode ymlNode = properties.getNodes().get(node);
                map.put(
                        node,  new NodeManager(
                                new ConcurrentLinkedQueue<>(),
                                new ConcurrentHashMap<>(2 ^ 6),
                                ymlNode.getUrl(), master_host, node)
                );
            }
            clusters.add(new SpiderCluster(localHistoryController, map));
        }
        return clusters;
    }


}
