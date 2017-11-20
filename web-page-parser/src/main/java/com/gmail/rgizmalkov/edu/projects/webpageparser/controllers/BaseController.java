package com.gmail.rgizmalkov.edu.projects.webpageparser.controllers;


import com.gmail.rgizmalkov.edu.projects.vo.WebPage;
import com.gmail.rgizmalkov.edu.projects.vo.WebPageServiceRequest;
import com.gmail.rgizmalkov.edu.projects.webpageparser.bs.CommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/base")
public class BaseController {

    @Autowired
    private CommonBusinessService commonBusinessService;

    @RequestMapping(method = GET, path = "/hc")
    public String health(){
        return "Service is working...";
    }

    @RequestMapping(
            method = POST,
            path = "/get_info",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public @ResponseBody WebPage webPageInfo(@RequestBody WebPageServiceRequest request){
        return commonBusinessService.getWebPage(request);
    }
}
