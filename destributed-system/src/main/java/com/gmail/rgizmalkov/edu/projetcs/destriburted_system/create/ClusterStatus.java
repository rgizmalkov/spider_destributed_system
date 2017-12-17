package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class ClusterStatus {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NodeStatus{
        private String node;
        private String health;
        private String status;
        private boolean isAvailable;
    }

    private String cluster;
    private List<NodeStatus> nodes = new ArrayList<>();

    public String getCluster() {
        return cluster;
    }

    public ClusterStatus setCluster(String cluster) {
        this.cluster = cluster;
        return this;
    }

    public List<NodeStatus> getNodes() {
        return nodes;
    }

    public ClusterStatus addNode(NodeStatus node) {
        this.nodes.add(node);
        return this;
    }
}
