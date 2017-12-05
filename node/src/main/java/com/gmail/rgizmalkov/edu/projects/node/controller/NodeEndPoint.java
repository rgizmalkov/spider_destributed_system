package com.gmail.rgizmalkov.edu.projects.node.controller;

import com.gmail.rgizmalkov.edu.projects.node.Storage;
import com.gmail.rgizmalkov.edu.projects.node.task.SaveMessage;
import com.gmail.rgizmalkov.edu.projects.vo.NodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController()
@RequestMapping("/node")
public class NodeEndPoint {

    @Autowired
    ThreadPoolTaskExecutor executorService;

    @Autowired
    Storage storage;

    @Value("${node.name}")
    String name;

    @RequestMapping(
            method = POST,
            path = "/get_message",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void bell(@RequestBody NodeRequest request) {
        executorService.execute(new SaveMessage(storage, request, name));
    }

    @GetMapping(
            path = "/data",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody Map<String, String> data(){
        return storage.getStorage();
    }

    @GetMapping(
            path = "/data/{uid}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody String get(@PathVariable String uid){
        return storage.get(uid);
    }

    @RequestMapping(
            method = GET,
            path = "/health"
    )
    public String serverStatus(){
        //logic
        return name + " is alive!";
    }

}
