package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.master;

import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.cluster.Cluster;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.create.ClusterStatus;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ClusterManager {

    private List<Cluster> clusters;
    private final int cap;
    private AtomicInteger atomicInteger;


    @Autowired
    public ClusterManager(List<Cluster> clusters) {
        this.clusters = clusters;
        this.cap = clusters.size();
        this.atomicInteger = new AtomicInteger();
    }

    public Cluster getNext() {
        int andIncrement = atomicInteger.getAndIncrement();
        return clusters.get(andIncrement % cap);
    }

    public Optional<Cluster> getByNode(@NonNull String node) {
        for (Cluster cluster : clusters) {
            if (cluster.isNodeOnCluster(node)) {
                return Optional.of(cluster);
            }
        }
        return Optional.empty();
    }

    public List<ClusterStatus> clustersStatus(){
        return clusters.stream()
                .parallel()
                .map(Cluster::clusterInfo)
                .collect(Collectors.toList());
    }
}
