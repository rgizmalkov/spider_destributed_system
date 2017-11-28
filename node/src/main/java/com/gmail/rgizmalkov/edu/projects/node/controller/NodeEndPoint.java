package com.gmail.rgizmalkov.edu.projects.node.controller;

import com.gmail.rgizmalkov.edu.projects.node.Storage;
import com.gmail.rgizmalkov.edu.projects.node.task.SaveMessage;
import com.gmail.rgizmalkov.edu.projects.vo.MessageInitialRs;
import com.gmail.rgizmalkov.edu.projects.vo.Result;
import com.gmail.rgizmalkov.edu.projects.vo.TaskResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.gmail.rgizmalkov.edu.projects.vo.Result.run;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class NodeEndPoint {

    @Autowired
    Executor executorService;

    @Autowired
    Storage storage;

    @RequestMapping(
            method = POST,
            path = "/get_message",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void write(@RequestBody MessageInitialRs request) {
        executorService.execute(new SaveMessage(storage, request));
    }

}
