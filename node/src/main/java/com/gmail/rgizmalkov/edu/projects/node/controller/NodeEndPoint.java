package com.gmail.rgizmalkov.edu.projects.node.controller;

import com.gmail.rgizmalkov.edu.projects.node.Storage;
import com.gmail.rgizmalkov.edu.projects.node.task.SaveMessage;
import com.gmail.rgizmalkov.edu.projects.vo.NodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executor;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
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
    public void bell(@RequestBody NodeRequest request) {
        executorService.execute(new SaveMessage(storage, request));
    }

    @RequestMapping(
            method = GET,
            path = "/health"
    )
    public String serverStatus(){
        //logic
        return null;
    }

}
