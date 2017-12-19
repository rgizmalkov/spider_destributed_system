package com.gmail.rgizmalkov.edu.projects.spider_aggregation.bs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.rgizmalkov.edu.projects.vo.Header;
import com.gmail.rgizmalkov.edu.projects.vo.ServiceQueueEntity;
import com.gmail.rgizmalkov.edu.projects.vo.WebPage;
import com.gmail.rgizmalkov.edu.projects.vo.WebPageServiceRequest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.gmail.rgizmalkov.edu.projects.vo.WebPageParsingOption.*;

@Service("aggregationBsService")
public class CommonBusinessService {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Value("${service.webPageServiceUrl}")
    private String getWebPageService;

    @Value("${service.master}")
    private String master;

    @SneakyThrows
    public <T> T post(@NonNull WebPageServiceRequest request, Class<T> tClass) {
        HttpResponse<JsonNode> accept = Unirest.post(getWebPageService)
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .body(mapper.writeValueAsString(request))
                .asJson();

        return (T) mapper.readValue(accept.getBody().toString(), tClass);
    }

    public <T> void save(@NonNull WebPage webPage) throws JsonProcessingException, UnirestException {
        WebPage newWebPage = new WebPage();
        newWebPage.setTitle(webPage.getTitle());

        List<Header> headers = webPage.getHeaders();
        List<Header> newHeaders = new ArrayList<>();
        for (Header header : headers) {
            if ("h1".equals(header.getScale()) || "h2".equals(header.getScale())) {
                newHeaders.add(header);
            }
        }
        newWebPage.setHeaders(newHeaders);
        newWebPage.setUrl(webPage.getUrl());


        ServiceQueueEntity serviceQueueEntity = new ServiceQueueEntity();
        serviceQueueEntity.setUid(UUID.randomUUID().toString());
        serviceQueueEntity.setJson(mapper.writeValueAsString(newWebPage));

        Unirest.post(master)
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .body(mapper.writeValueAsString(serviceQueueEntity))
                .asJson();

    }
}
