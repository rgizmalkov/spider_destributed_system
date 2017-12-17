package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.master;


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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@RequestMapping("/master/history")
public class LocalHistoryController {
    private final static Logger logger = LoggerFactory.getLogger(LocalHistoryController.class);

    private final ThreadPoolTaskExecutor executor;
    private final Map<String, List<TaskResult>> history;

    public LocalHistoryController(ThreadPoolTaskExecutor executor, Map<String, List<TaskResult>> history) {
        this.executor = executor;
        this.history = history;
    }

    @GetMapping(
            path = "/info/{node}"
    )
    public @ResponseBody Map<String, List<TaskResult>> info(@PathVariable String node){
        if (node == null || "all".equals(node)) {
            return ImmutableMap.copyOf(history);
        }else {
            List<TaskResult> taskResults = history.get(node);
            return ImmutableMap.of(node, new ArrayList<>(taskResults));
        }
    }

    @PostMapping(
            path = "/set"
    )
    public void set(@RequestBody TaskResult taskResult){
         executor.execute(() -> {
                if(taskResult != null){
                    String node = taskResult.getNode();
                    List<TaskResult> taskResults = history.get(node);
                    if(taskResults == null){
                        logger.warn("Can not find node with name = " + node);
                    }else {
                        taskResults.add(taskResult);
                    }
                }
         });
    }

    public Pair<List<TaskResult>, List<TaskResult>> reset(String from, String to){
        return new ImmutablePair<>(history.get(from), history.get(to));
    }

}
