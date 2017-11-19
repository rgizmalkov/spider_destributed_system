package com.gmail.rgizmalkov.edu.projects.webpageparser.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/base")
public class BaseController {

    @RequestMapping(method = GET, path = "/hc")
    public String health(){
        return "Service is working...";
    }
}
