package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.cluster;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.rgizmalkov.edu.projects.vo.NodeRequest;
import com.gmail.rgizmalkov.edu.projects.vo.NodeResponse;
import com.gmail.rgizmalkov.edu.projects.vo.ServiceQueueEntity;
import com.google.common.base.Objects;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;

public class NodeManager {
    private final String nodeUID;

    private static final Logger logger = LoggerFactory.getLogger(NodeManager.class);
    private final static ObjectMapper mapper = new ObjectMapper();

    private final ConcurrentLinkedQueue<ServiceQueueEntity> queue;
    private final ConcurrentHashMap<String, ServiceQueueEntity> outMap;

    private AtomicBoolean isServiceAvailable;
    AtomicBoolean isNeedToHardReset;

    private final String nodeURL;
    private final String masterUrl;

    private Thread thread;
    volatile Status status;

    public NodeManager(
            ConcurrentLinkedQueue<ServiceQueueEntity> queue,
            ConcurrentHashMap<String, ServiceQueueEntity> outMap,
            String nodeURL, String masterUrl, String nodeUID
    ) {
        this.queue = queue;
        this.outMap = outMap;
        this.nodeURL = nodeURL;
        this.masterUrl = masterUrl;
        this.nodeUID = nodeUID;
        this.status = Status.OK;
        isServiceAvailable = new AtomicBoolean(true);
        isNeedToHardReset = new AtomicBoolean(false);
        sendToNode();
    }

    public void put(ServiceQueueEntity entity) {
        if(!isServiceAvailable.get()){

        }
        queue.add(entity);

    }

    public NodeResponse<ServiceQueueEntity> get(String uid) {
        // После получения сообщения об успешной записи отправить в мастер информацию о записанном файле
        ServiceQueueEntity serializableServiceQueueEntity = outMap.remove(uid);
        if (serializableServiceQueueEntity == null) {
            logger.warn(format("Cannot find entity in back map. [uid = %s]", uid));
            //send to info
            return new NodeResponse<>(10, "WARN", "Cannot find entity in back-map", null);
        } else {
            return new NodeResponse<>(0, "OK", "Success get entity from back-map", serializableServiceQueueEntity);
        }
    }

    public ServiceQueueEntity getFromNode(String uid){
        try {
            HttpResponse<String> jsonNodeHttpResponse = Unirest.get(nodeURL + "/date/" + uid).asString();
            return mapper.readValue(jsonNodeHttpResponse.getBody(), ServiceQueueEntity.class);
        } catch (Exception e) {
            return null;
        }
    }


    private void sendToNode() {
        if(this.thread != null){
            thread.interrupt();
        }
        this.thread = new Thread(() -> {
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
                    queue.add(entity);
                }
            }
        });
        thread.start();
    }

    private void send(ServiceQueueEntity entity) {
        if (validate(entity)) {
            try {
                NodeRequest message = new NodeRequest();
                message.setFrom(masterUrl + "/get");
                message.setUid(entity.getAppId());
                outMap.put(entity.getAppId(), entity);
                logger.info(format("Send request from [%s] to [%s] message with uid = %s", masterUrl, nodeURL, entity.getAppId()));
                Unirest.post(nodeURL + "/get_message")
                        .header("accept", "application/json")
                        .header("content-type", "application/json")
                        .body(mapper.writeValueAsString(message))
                        .asJson();
            } catch (Exception ex) {
                logger.warn(format("Fail sending message with uid = %s\nError during process of send message!", entity.getAppId()));
                queue.add(entity);
                outMap.remove(entity.getAppId(), entity);
            }
        }
    }

    public String nodeHealth() {
        try {
            HttpResponse<String> stringHttpResponse = Unirest.get(nodeURL + "/health").asString();
            return stringHttpResponse.getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    public int nodeSize() {
        try {
            HttpResponse<String> stringHttpResponse = Unirest.get(nodeURL + "/storage/size").asString();
            return Integer.valueOf(stringHttpResponse.getBody());
        } catch (Exception ex) {
            return 0;
        }
    }


    private boolean validate(ServiceQueueEntity entity) {
        return entity != null &&
                entity.getJson() != null &&
                entity.getUid() != null;
    }

    public void changeServiceStatus(boolean value) {
        if (value && !isServiceAvailable.get()) {
            sendToNode();
        }
        isServiceAvailable.set(value);
    }

    public String getNodeUID() {
        return nodeUID;
    }



    public void changeServiceStatusByHealthCheck() {
        new Thread(() -> {
            String nodeHealth = nodeHealth();
            if (nodeHealth != null && !nodeHealth.isEmpty()) {
                boolean sign = isServiceAvailable.get();
                if(!sign){
                    int size = nodeSize();
                    if(size == 0){
                        status = Status.RESET_AFTER_DROPDOWN;
                    }else {
                        status = Status.RESET_AFTER_STOP;
                    }
                    isServiceAvailable.set(true);
                    sendToNode();
                    isNeedToHardReset.set(true);
                }
            }else {
                isServiceAvailable.set(false);
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                /*NuN*/
            }
        }).start();
    }

    public boolean getIsServiceAvailable() {
        return isServiceAvailable.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeManager that = (NodeManager) o;
        return Objects.equal(nodeUID, that.nodeUID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nodeUID);
    }
}
