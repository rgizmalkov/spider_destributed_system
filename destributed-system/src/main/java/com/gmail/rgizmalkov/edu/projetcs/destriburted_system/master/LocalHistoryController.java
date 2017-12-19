package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.master;


import com.gmail.rgizmalkov.edu.projects.vo.NodeDifference;
import com.gmail.rgizmalkov.edu.projects.vo.TaskResult;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

@RequestMapping("/master/history")
public class LocalHistoryController {
    private final static Logger logger = LoggerFactory.getLogger(LocalHistoryController.class);

    private final ThreadPoolTaskExecutor executor;
    private final Map<String, Set<TaskResult>> history;

    public LocalHistoryController(ThreadPoolTaskExecutor executor, Map<String, Set<TaskResult>> history) {
        this.executor = executor;
        this.history = history;
    }

    @GetMapping(
            path = "/info/{node}"
    )
    public @ResponseBody
    Map<String, Set<TaskResult>> info(@PathVariable String node) {
        if (node == null || "all".equals(node)) {
            return ImmutableMap.copyOf(history);
        } else {
            Set<TaskResult> taskResults = history.get(node);
            return ImmutableMap.of(node, new HashSet<>(taskResults));
        }
    }
    @GetMapping(
            path = "/size/{node}"
    )
    public @ResponseBody
    Map<String, String> size(@PathVariable String node) {
        if (node == null || "all".equals(node)) {
            Map<String, String> res = new HashMap<>();
            for (String s : history.keySet()) {
                res.put(s, String.format("Size of storage on the node by info from history back-map = %s", history.get(s).size()));
            }
            return res;
        } else {
            Set<TaskResult> taskResults = history.get(node);
            return ImmutableMap.of(node, String.format("Size of storage on the node by info from history back-map = %s", taskResults.size()));
        }
    }


    @PostMapping(
            path = "/set"
    )
    public void set(@RequestBody TaskResult taskResult) {
        executor.execute(() -> {
            if (taskResult != null) {
                String node = taskResult.getNode();
                Set<TaskResult> taskResults = history.get(node);
                if (taskResults == null) {
                    logger.warn("Can not find node with name = " + node);
                } else {
                     if (!taskResults.contains(taskResult)) {
                        taskResults.add(taskResult);
                    }
                }
            }
        });
    }

    @GetMapping(
            path = "/analyze/diff/{node1}/{node2}"
    )
    public @ResponseBody
    NodeDifference analize(@PathVariable String node1, @PathVariable String node2) {
        Set<TaskResult> firstNodeTasks = history.get(node1);
        Set<TaskResult> secondNodeTasks = history.get(node2);

        return new NodeDifference(node1, node2, firstNodeTasks, secondNodeTasks);
    }

    public Pair<Set<TaskResult>, Set<TaskResult>> reset(String from, String to) {
        return new ImmutablePair<>(history.get(from), history.get(to));
    }

}
