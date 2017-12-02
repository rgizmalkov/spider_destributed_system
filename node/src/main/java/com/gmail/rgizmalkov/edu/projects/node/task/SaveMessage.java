package com.gmail.rgizmalkov.edu.projects.node.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.rgizmalkov.edu.projects.node.Storage;
import com.gmail.rgizmalkov.edu.projects.vo.MessageInitialRs;
import com.gmail.rgizmalkov.edu.projects.vo.TaskResult;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class SaveMessage implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SaveMessage.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    Storage storage;
    MessageInitialRs messageInitialRs;

    public SaveMessage(Storage storage, MessageInitialRs messageInitialRs) {
        this.storage = storage;
        this.messageInitialRs = messageInitialRs;
    }

    @Override
    public void run(){
        String uid = messageInitialRs.getUid();
        try {
            storage.write(uid, getMessage(uid));
            sendAnswer(new TaskResult(0, "SUCCESS", uid));
        }catch (Exception ex){
            logger.warn("Error during attempt to save value with id = " + uid);
            sendAnswer(new TaskResult(12, "WRITE_ERROR", uid));
        }
    }

    @SneakyThrows
    private Serializable getMessage(String uid){
        HttpResponse<JsonNode> accept = Unirest.post("http://localhost:8080/spider/base/get_info")
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .body(mapper.writeValueAsString(uid))
                .asJson();
        return accept.getBody().toString();
    }

    @SneakyThrows
    private void sendAnswer(TaskResult taskResult){
        HttpResponse<JsonNode> accept = Unirest.post("")
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .body(mapper.writeValueAsString(taskResult))
                .asJson();
    }
}
