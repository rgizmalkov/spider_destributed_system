package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.cluster;

import com.gmail.rgizmalkov.edu.projects.vo.AppId;
import com.gmail.rgizmalkov.edu.projects.vo.NodeResponse;
import com.gmail.rgizmalkov.edu.projects.vo.ServiceQueueEntity;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.create.ClusterStatus;

import java.util.Set;
import java.util.function.Supplier;

public interface Cluster {

    <R extends ServiceQueueEntity> void sendMessageToCluster(R obj);
    <R extends ServiceQueueEntity> void sendMessageToOneNode(String uid, R obj);

    <R extends ServiceQueueEntity> NodeResponse<R> writeToOneNode(String node, String uid);

    <R extends ServiceQueueEntity> R read(String uid);
    <R extends ServiceQueueEntity> R readFromNode(String node, String uid);

    boolean isNodeOnCluster(String node);

    ClusterStatus clusterInfo();
    String showNodeDifference();

    void hardReset();

    boolean isNodeStillWorking(String uid);

}
