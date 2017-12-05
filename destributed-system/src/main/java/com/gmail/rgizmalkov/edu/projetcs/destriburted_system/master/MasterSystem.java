package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.master;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.rgizmalkov.edu.projects.vo.NodeResponse;
import com.gmail.rgizmalkov.edu.projects.vo.ServiceQueueEntity;
import com.gmail.rgizmalkov.edu.projetcs.destriburted_system.node.QuiteNode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.*;

@RequestMapping("/master")
public class MasterSystem {
    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper()
                .configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
//                .configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        String uid = UUID.randomUUID().toString();
        Object obj = "VALUE";
        ServiceQueueEntity stringServiceQueueEntity = new ServiceQueueEntity();
        stringServiceQueueEntity.setUid(uid);
        stringServiceQueueEntity.setJson(objectMapper.writeValueAsString(obj));
        System.out.println(objectMapper.writeValueAsString(stringServiceQueueEntity));
    }

    private Map<String, QuiteNode> quiteNodes;
    private final int replication;
    private final String nodeName;
    private final String masterURL;

    public MasterSystem(int replication, String nodeName, String masterURL) {
        this.replication = replication;
        this.nodeName = nodeName;
        this.masterURL = masterURL;
        this.quiteNodes = new HashMap<>();
    }

    public MasterSystem addNode(QuiteNode node){
        quiteNodes.put(node.getNodeUID() , node);
        return this;
    }

    @PostMapping(path = "/write")
    public void write(@RequestBody ServiceQueueEntity entity){
        QuiteNode quiteNode = quiteNodes.get("Traver");
        quiteNode.put(entity);
    }

    @GetMapping(
            path = "/get/{node}/{uid}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody NodeResponse<ServiceQueueEntity> get(@PathVariable String node, @PathVariable String uid){
        QuiteNode quiteNode = quiteNodes.get(node);
        return quiteNode.get(uid);
    }

    @PostMapping(path = "/disable/{node}")
    public void disableNode(@PathVariable String node){
        quiteNodes.get(node).changeServiceStatus(false);
    }


    @PostMapping(path = "/enable/{node}")
    public void enableNode(@PathVariable String node){
        quiteNodes.get(node).changeServiceStatus(true);
    }
}
