package com.gmail.rgizmalkov.edu.projects.spider_aggregation.controllers;


import com.gmail.rgizmalkov.edu.projects.spider_aggregation.bs.CommonBusinessService;
import com.gmail.rgizmalkov.edu.projects.spider_aggregation.tests.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("testing")
public class TestingController {

    @Autowired
    CommonBusinessService bsService;

    @RequestMapping(method = POST, path = "/cat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String cat(@RequestBody Cat cat) {
        System.out.println(cat);
        return "Hello kitty, " + cat.getName();
    }


    @RequestMapping(method = GET, path = "/srv")
    public @ResponseBody String srv(){
        bsService.goTo("https://en.wikipedia.org/wiki/Main_Page");
        return "Done";
    }
}
