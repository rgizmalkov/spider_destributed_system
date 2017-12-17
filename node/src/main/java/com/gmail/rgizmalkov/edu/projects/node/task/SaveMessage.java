package com.gmail.rgizmalkov.edu.projects.node.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.rgizmalkov.edu.projects.node.Storage;
import com.gmail.rgizmalkov.edu.projects.vo.NodeRequest;
import com.gmail.rgizmalkov.edu.projects.vo.TaskResult;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class SaveMessage implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SaveMessage.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    private Storage storage;
    private NodeRequest nodeRequest;
    private final String nodeName;

    public SaveMessage(Storage storage, NodeRequest nodeRequest, String nodeName) {
        this.storage = storage;
        this.nodeRequest = nodeRequest;
        this.nodeName = nodeName;
    }

    @Override
    public void run(){
        String uid = nodeRequest.getUid();
        String from = nodeRequest.getFrom();
        try {
            storage.write(uid, getMessage(from, uid));
            sendAnswer(new TaskResult(0, "OK", uid, nodeName));
        }catch (Exception ex){
            logger.warn("Error during attempt to save value with id = " + uid);
            sendAnswer(new TaskResult(12, "WRITE_ERROR", uid, nodeName));
        }
    }

    @SneakyThrows
    private String getMessage(String from, String uid){
        HttpResponse<JsonNode> accept = Unirest.get(from + "/" + nodeName + "/" + uid)
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .asJson();
        return  accept.getBody().toString().replace("\\", "").replace("\"", "");
    }

    @SneakyThrows
    private void sendAnswer(TaskResult taskResult){
        HttpResponse<JsonNode> accept = Unirest.post("http://localhost:8080/master/history/set")
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .body(mapper.writeValueAsString(taskResult))
                .asJson();
    }
}
