package com.gmail.rgizmalkov.edu.projects.spider_aggregation.controllers;


import com.gmail.rgizmalkov.edu.projects.spider_aggregation.bs.CommonBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("testing")
public class TestingController {


    @Autowired
    CommonBusinessService bsService;

    @RequestMapping(method = GET, path="/google")
    public String finder(){
        return bsService.getWebPage("https://www.google.ru/");
    }
}
