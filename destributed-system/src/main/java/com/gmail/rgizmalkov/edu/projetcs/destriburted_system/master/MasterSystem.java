package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.master;

import com.gmail.rgizmalkov.edu.projects.vo.NodeResponse;
import com.gmail.rgizmalkov.edu.projects.vo.ServiceQueueEntity;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.cluster.Cluster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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

//    @GetMapping(
//            path = "/nodes/health"
//    )
//    public @ResponseBody
//    Map<String, List<String>> nodesHealth() {
//        Map<String, List<String>> map = new HashMap<>();
//        final String cluster = "Cluster [%s]";
//        List<String[]> replics = quiteNodes.getReplics();
//        for (int i = 0; i < replics.size(); i++) {
//            List<String> hch = new ArrayList<>();
//            String[] nodesInCluster = replics.get(i);
//            for (String node : nodesInCluster) {
//                hch.add(quiteNodes.get(node).nodeHealth());
//            }
//            map.put(String.format(cluster, i), hch);
//        }
//        return map;
//    }


}
