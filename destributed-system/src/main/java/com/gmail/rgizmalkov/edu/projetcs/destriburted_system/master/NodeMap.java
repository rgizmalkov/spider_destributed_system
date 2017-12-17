package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.master;

import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.cluster.NodeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NodeMap {

    private final Map<String, NodeManager> quiteNodeMap;
    private int replication;
    private int interval;
    private List<String[]> replics;
    private Random randomizer;


    public NodeMap(Map<String, NodeManager> quiteNodeMap, int replication) {
        this.quiteNodeMap = quiteNodeMap;
        this.replication = replication;
        this.replics = new ArrayList<>();
        createReplics();
        randomizer = new Random();
    }

    private void createReplics(){
        String[] objects = new String[quiteNodeMap.keySet().size()];
        objects = quiteNodeMap.keySet().toArray(objects);
        int length = objects.length;
        if(length <= replication){
            this.interval = 1;
            replics.add(objects);
            this.replication = length;
        }else {
            this.interval = length / replication;
            if(length % replication == 0){
                List<String[]> rep = new ArrayList<>(replication);
                for (int i = 0; i < length;) {
                    for (int j = 0; j < interval; j++) {
                        if(i < interval){
                            String[] nodes = new String[replication];
                            rep.add(nodes);
                            nodes[0] = objects[i];
                            i++;
                        }else {
                            String[] nodes = rep.get(j);
                            nodes[i/interval] = objects[i];
                            i++;
                        }
                    }
                }
                this.replics = rep;
            }else {
                throw new RuntimeException("Not respected amount of nodes");
            }
        }

    }

    public NodeManager[] getNextNodes(){
        int nodeNum = randomizer.nextInt(interval);
        NodeManager[] nodeManagers = new NodeManager[replication];
        String[] nodesNames = replics.get(nodeNum);
        for (int i = 0; i < nodeManagers.length; i++) {
            nodeManagers[i] = quiteNodeMap.get(nodesNames[i]);
        }
        return nodeManagers;
    }

    public List<String[]> getReplics() {
        return replics;
    }

    public NodeManager get(String nodeName){
        return quiteNodeMap.get(nodeName);
    }
}
