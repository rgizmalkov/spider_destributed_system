package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.cluster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.rgizmalkov.edu.projects.vo.NodeResponse;
import com.gmail.rgizmalkov.edu.projects.vo.ServiceQueueEntity;
import com.gmail.rgizmalkov.edu.projects.vo.TaskResult;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.create.ClusterStatus;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.master.LocalHistoryController;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SpiderCluster implements Cluster {
    private static final Logger logger = LoggerFactory.getLogger(SpiderCluster.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private final LocalHistoryController localHistoryController;
    private final Map<String, NodeManager> nodesOnCluster;
    private final String name;

    public SpiderCluster(LocalHistoryController localHistoryController, Map<String, NodeManager> nodesOnCluster, String name) {
        this.localHistoryController = localHistoryController;
        this.nodesOnCluster = nodesOnCluster;
        this.name = name;
        hardReset();
    }

    @Override
    public <R extends ServiceQueueEntity> void sendMessageToCluster(R obj) {
        nodesOnCluster.values().stream()
                .parallel()
                .forEach((nodeManager) -> {
                    nodeManager.put(obj);
                });
    }

    @Override
    public <R extends ServiceQueueEntity> void sendMessageToOneNode(String uid, R obj) {
        nodesOnCluster.computeIfPresent(uid, (map, node) -> {
            node.put(obj);
            return node;
        });
    }

    @Override
    public <R extends ServiceQueueEntity> NodeResponse<R> writeToOneNode(String node, String uid) {
        NodeManager nodeManager = nodesOnCluster.get(node);
        return (NodeResponse<R>) nodeManager.get(uid);
    }


    @Override
    public <R extends ServiceQueueEntity> R read(String uid) {
        Collection<NodeManager> values = nodesOnCluster.values();
        for (NodeManager node : values) {
            if (node.getIsServiceAvailable() && node.status == Status.OK) {
                return (R) node.getFromNode(uid);
            }
        }
        return null;
    }

    @Override
    public <R extends ServiceQueueEntity> R readFromNode(String node, String uid) {
        NodeManager nodeManager = nodesOnCluster.get(node);
        return (R) nodeManager.getFromNode(uid);
    }

    @Override
    public boolean isNodeOnCluster(String node) {
        return nodesOnCluster.containsKey(node);
    }

    @Override
    public ClusterStatus clusterInfo() {
        ClusterStatus clusterStatus = new ClusterStatus().setCluster(name);
        for (NodeManager nodeManager : nodesOnCluster.values()) {
            clusterStatus.addNode(
                    new ClusterStatus.NodeStatus(
                            nodeManager.getNodeUID(),
                            nodeManager.nodeHealth(),
                            String.valueOf(nodeManager.status),
                            nodeManager.getIsServiceAvailable()
                    ));
        }
        return clusterStatus;
    }

    @Override
    public String showNodeDifference() {
        return null;
    }

    @Override
    public boolean isNodeStillWorking(String uid) {
        NodeManager nodeManager = nodesOnCluster.get(uid);
        if (nodeManager != null) {
            String nodeHealth = nodeManager.nodeHealth();
            return nodeHealth != null && !nodeHealth.isEmpty();
        } else {
            return false;
        }
    }


    public void hardReset() {
        new Thread(() -> {
            while(true) {
                try {
                    nodesOnCluster.values().stream()
                            .parallel()
                            .forEach((nodeManager) -> {
                                        if (nodeManager.isNeedToHardReset.get() && nodeManager.getIsServiceAvailable()) {
                                            String nodeUID = nodeManager.getNodeUID();
                                            for (String node : nodesOnCluster.keySet()) {
                                                NodeManager anotherOneNodeManager = nodesOnCluster.get(node);
                                                if (!nodeUID.equals(node) && !anotherOneNodeManager.isNeedToHardReset.get() && anotherOneNodeManager.getIsServiceAvailable()) {
                                                    String anotherNodeUID = anotherOneNodeManager.getNodeUID();
                                                    Pair<Set<TaskResult>, Set<TaskResult>> reset = localHistoryController.reset(nodeUID, anotherNodeUID);
                                                    Status status = nodeManager.status;
                                                    Set<TaskResult> left = reset.getLeft();
                                                    switch (status) {
                                                        case RESET_AFTER_DROPDOWN:
                                                            for (TaskResult taskResult : left) {
                                                                nodeManager.put(anotherOneNodeManager.getFromNode(taskResult.getUid()));
                                                            }
                                                            nodeManager.isNeedToHardReset.set(false);
                                                            nodeManager.status = Status.OK;
                                                            break;
                                                        case RESET_AFTER_STOP:
                                                            Set<TaskResult> right = reset.getRight();
                                                            HashSet<TaskResult> taskResults = new HashSet<>(right);
                                                            for (TaskResult taskResult : left) {
                                                                if (!taskResults.contains(taskResult)) {
                                                                    nodeManager.put(anotherOneNodeManager.getFromNode(taskResult.getUid()));
                                                                }
                                                            }
                                                            nodeManager.isNeedToHardReset.set(false);
                                                            nodeManager.status = Status.OK;
                                                            break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                            );
                }catch (Exception ex){
                    /*NuN*/
                }
            }
        }).start();
    }
}
