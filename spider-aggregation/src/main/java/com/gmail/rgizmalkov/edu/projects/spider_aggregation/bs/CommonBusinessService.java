package com.gmail.rgizmalkov.edu.projects.spider_aggregation.bs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.rgizmalkov.edu.projects.vo.WebPage;
import com.gmail.rgizmalkov.edu.projects.vo.WebPageServiceRequest;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.gmail.rgizmalkov.edu.projects.vo.WebPageParsingOption.*;

@Service("aggregationBsService")
public class CommonBusinessService {

    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public <T> T post(@NonNull WebPageServiceRequest request, Class<T> tClass){
        HttpResponse<JsonNode> accept = Unirest.post("http://localhost:8080/spider/base/get_info")
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .body(mapper.writeValueAsString(request))
                .asJson();

        return (T) mapper.readValue(accept.getBody().toString(), tClass);
    }
}
