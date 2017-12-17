package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.master;

import com.gmail.rgizmalkov.edu.projects.vo.NodeResponse;
import com.gmail.rgizmalkov.edu.projects.vo.ServiceQueueEntity;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.cluster.Cluster;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.create.ClusterStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/master")
public class MasterSystem {

    private final ClusterManager clusterManager;

    @Autowired
    public MasterSystem(ClusterManager clusterManager) {
        this.clusterManager = clusterManager;
    }

    @PostMapping(path = "/write")
    public void write(@RequestBody ServiceQueueEntity entity) {
        Cluster cluster = clusterManager.getNext();
        cluster.sendMessageToCluster(entity);
    }

    @GetMapping(
            path = "/get/{node}/{uid}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody
    NodeResponse<ServiceQueueEntity> get(@PathVariable String node, @PathVariable String uid) {
        return clusterManager.getByNode(node).map((cluster) -> {
            return cluster.writeToOneNode(node, uid);
        }).orElseThrow(
                () -> {
                    return new RuntimeException("Error during attempt to get info from back-map!");
                }
        );
    }

    @GetMapping(
            path = "/nodes/health"
    )
    public @ResponseBody List<ClusterStatus> nodesHealth() {
        return clusterManager.clustersStatus();
    }


}
