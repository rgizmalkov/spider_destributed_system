package com.gmail.rgizmalkov.edu.projects.spider_aggregation.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.rgizmalkov.edu.projects.spider_aggregation.cg.PlatformBridge;
import com.gmail.rgizmalkov.edu.projects.vo.SpiderServiceRequest;
import com.gmail.rgizmalkov.edu.projects.vo.WebPage;
import com.gmail.rgizmalkov.edu.projects.vo.WebPageParsingOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("testing")
public class SpiderController {

    @Autowired
    private PlatformBridge bridge;

    @RequestMapping(
            method = POST, path = "/srv",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody String srv(@RequestBody SpiderServiceRequest request){
        try {
            bridge.getWebInfo(request);
            return "OK";
        } catch (Exception ex) {
            return "FAIL";
        }
    }

    public static void main(String[] args) throws JsonProcessingException {
        SpiderServiceRequest spiderServiceRequest = new SpiderServiceRequest();
        spiderServiceRequest.setDeep(1);
        spiderServiceRequest.setOptions(Arrays.asList(
                WebPageParsingOption.HEADERS,
                WebPageParsingOption.TITLE,
                WebPageParsingOption.URLS,
                WebPageParsingOption.HEAD
        ));
        spiderServiceRequest.setUrl("https://en.wikipedia.org/wiki/Space_debris");

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writeValueAsString(spiderServiceRequest));
    }
}
