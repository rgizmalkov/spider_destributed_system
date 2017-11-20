package com.gmail.rgizmalkov.edu.projects.spider_aggregation.bs;

import com.gmail.rgizmalkov.edu.projects.spider_aggregation.srv.GetWebPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonBusinessService {

    @Autowired
    private GetWebPageService webPageService;

    public String getWebPage(String url){
        return webPageService.getByUrl(url);
    }
}
