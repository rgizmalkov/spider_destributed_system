package com.gmail.rgizmalkov.edu.projects.spider_aggregation.bs;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public void goTo(@NonNull String url){
        HttpResponse<JsonNode> accept = Unirest.post("http://localhost:8080/spider/base/get_info")
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .body(mapper.writeValueAsString(new WebPageServiceRequest(
                        url, Arrays.asList(TITLE, HEADERS, URLS)
                )))
                .asJson();

        System.out.println(accept);
    }
}
