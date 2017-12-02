package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.node;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.rgizmalkov.edu.projects.vo.MessageInitialRs;
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

@RestController
@RequestMapping("/node")
public class QuiteNode {
    private static final Logger logger = LoggerFactory.getLogger(QuiteNode.class);
    private final static ObjectMapper mapper = new ObjectMapper();

    @Value("node.requestUrl")
    private String nodeURL;
    @Value("node.selfUrl")
    private String selfURL;
    private AtomicBoolean isServiceAvailable;

    private final ConcurrentLinkedQueue<ServiceQueueEntity<Serializable>> queue;
    private final ConcurrentHashMap<String, ServiceQueueEntity<Serializable>> outMap;

    @Autowired
    public QuiteNode(
            @Qualifier("initialQueue") ConcurrentLinkedQueue<ServiceQueueEntity<Serializable>> queue,
            @Qualifier("responseMap") ConcurrentHashMap<String, ServiceQueueEntity<Serializable>> outMap,
    ) {
        this.queue = queue;
        this.outMap = outMap;
    }

    @PostConstruct
    public void init() {
        isServiceAvailable = new AtomicBoolean(true);
        sendToNode();
    }

    @PostMapping(
            path = "/put"
    )
    public void put(@RequestBody ServiceQueueEntity<Serializable> entity) {
        queue.add(entity);
    }

    @GetMapping(
            path = "/get/{uid}"
    )
    public void get(@PathVariable String uid) {
        // После получения сообщения об успешной записи отправить в мастер информацию о записанном файле


        return outMap.remove(uid);
    }


    private void sendToNode() {
        while (isServiceAvailable.get()) {
            ServiceQueueEntity<Serializable> entity = null;
            try {
                entity = queue.poll();
                if (entity != null) {
                    send(entity);
                }
            } catch (Exception ex) {
                logger.warn(
                        format(
                                "Error during attempt to send entity to node.\nNode = %s\nEntity = %s",
                                nodeURL,
                                entity
                        )
                );
            }
        }
    }

    private void send(ServiceQueueEntity<Serializable> entity) {
        if (validate(entity)) {
            try {
                MessageInitialRs message = new MessageInitialRs();
                message.setFrom(selfURL + "/get");
                message.setUid(entity.getUid());
                outMap.put(entity.getUid(), entity);
                logger.info(format("Send request from [%s] to [%s] message with uid = %s", selfURL, nodeURL, entity.getUid()));
                Unirest.post(nodeURL + "/send_message")
                        .header("accept", "application/json")
                        .header("content-type", "application/json")
                        .body(mapper.writeValueAsString(message))
                        .asJson();
            }catch (Exception ex){
                logger.warn(format("Fail sending message with uid = %s\nError during process of send message!", entity.getUid()));
                outMap.remove(entity.getUid(), entity);
            }
        }
    }

    private boolean validate(ServiceQueueEntity<Serializable> entity) {
        return entity != null &&
                entity.getVal() != null &&
                entity.getUid() != null &&
                entity.getHash() != null &&
                entity.getValueClass() != null;
    }

    @PostMapping(path = "/changeStatus/{value}")
    public void changeServiceStatus(@PathVariable Boolean value) {
        isServiceAvailable.set(value);
    }
}
