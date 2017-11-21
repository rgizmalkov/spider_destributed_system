package com.gmail.rgizmalkov.edu.projects.spider_aggregation.controllers;


import com.gmail.rgizmalkov.edu.projects.spider_aggregation.cg.PlatformBridge;
import com.gmail.rgizmalkov.edu.projects.vo.SpiderServiceRequest;
import com.gmail.rgizmalkov.edu.projects.vo.WebPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
        List<WebPage> webInfo = bridge.getWebInfo(request);
        return "Done";
    }
}
