package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.node;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.rgizmalkov.edu.projects.vo.NodeRequest;
import com.gmail.rgizmalkov.edu.projects.vo.NodeResponse;
import com.gmail.rgizmalkov.edu.projects.vo.ServiceQueueEntity;
import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;

public class QuiteNode {
    private static final Logger logger = LoggerFactory.getLogger(QuiteNode.class);
    private final static ObjectMapper mapper = new ObjectMapper();
    private final ConcurrentLinkedQueue<ServiceQueueEntity> queue;
    private final ConcurrentHashMap<String, ServiceQueueEntity> outMap;
    private AtomicBoolean isServiceAvailable;
    private String nodeURL;
    private String selfURL;
    private String nodeUID;

    public QuiteNode(
            ConcurrentLinkedQueue<ServiceQueueEntity> queue,
            ConcurrentHashMap<String, ServiceQueueEntity> outMap,
            String nodeURL, String selfURL, String nodeUID
    ) {
        this.queue = queue;
        this.outMap = outMap;
        this.nodeURL = nodeURL;
        this.selfURL = selfURL;
        this.nodeUID = nodeUID;
        isServiceAvailable = new AtomicBoolean(true);
        sendToNode();
    }

    public void put(ServiceQueueEntity entity) {
        queue.add(entity);
    }

    public NodeResponse<ServiceQueueEntity> get(String uid) {
        // После получения сообщения об успешной записи отправить в мастер информацию о записанном файле
        ServiceQueueEntity serializableServiceQueueEntity = outMap.get(uid);
        if (serializableServiceQueueEntity == null) {
            logger.warn(format("Cannot find entity in back map. [uid = %s]", uid));
            //send to info
            return new NodeResponse<>(10, "WARN", "Cannot find entity in back-map", selfURL + "/result" , null);
        } else {
            return new NodeResponse<>(0, "OK", "Success get entity from back-map", selfURL + "/result", serializableServiceQueueEntity);
        }
    }


    private void sendToNode() {
        Thread thread = new Thread(() -> {
            while (isServiceAvailable.get()) {
                ServiceQueueEntity entity = null;
                try {
                    entity = queue.poll();
                    if (entity != null) {
                        send(entity);
                    } else {
                        Thread.sleep(1000);
                    }
                } catch (Exception ex) {
                    logger.warn(
                            format(
                                    "Error during attempt to send message to node.\nNode = %s\nEntity = %s",
                                    nodeURL,
                                    entity
                            )
                    );
                }
            }
        });
        thread.start();
    }

    private void send(ServiceQueueEntity entity) {
        if (validate(entity)) {
            try {
                NodeRequest message = new NodeRequest();
                message.setFrom(selfURL+"/master/get");
                message.setUid(entity.getUid());
                outMap.put(entity.getUid(), entity);
                logger.info(format("Send request from [%s] to [%s] message with uid = %s", selfURL, nodeURL, entity.getUid()));
                Unirest.post(nodeURL + "/get_message")
                        .header("accept", "application/json")
                        .header("content-type", "application/json")
                        .body(mapper.writeValueAsString(message))
                        .asJson();
            } catch (Exception ex) {
                logger.warn(format("Fail sending message with uid = %s\nError during process of send message!", entity.getUid()));
                outMap.remove(entity.getUid(), entity);
            }
        }
    }

    private boolean validate(ServiceQueueEntity entity) {
        return entity != null &&
                entity.getJson() != null &&
                entity.getUid() != null;
    }

    public void changeServiceStatus(boolean value) {
        if(value && !isServiceAvailable.get()){
            sendToNode();
        }
        isServiceAvailable.set(value);
    }

    public String getNodeUID() {
        return nodeUID;
    }
}
