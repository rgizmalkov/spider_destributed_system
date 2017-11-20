package com.gmail.rgizmalkov.edu.projects.webpageparser.bs;

import com.gmail.rgizmalkov.edu.projects.vo.WebPage;
import com.gmail.rgizmalkov.edu.projects.vo.WebPageParsingOption;
import com.gmail.rgizmalkov.edu.projects.vo.WebPageServiceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gmail.rgizmalkov.edu.projects.webpageparser.srv.GetWebPageService;

import java.util.Arrays;
import java.util.List;

import static com.gmail.rgizmalkov.edu.projects.vo.WebPageParsingOption.HEADERS;
import static com.gmail.rgizmalkov.edu.projects.vo.WebPageParsingOption.TITLE;

@Service
public class CommonBusinessService {

    private static final Logger logger = LoggerFactory.getLogger(CommonBusinessService.class);

    @Autowired
    private GetWebPageService webPageService;

    public WebPage getWebPage(WebPageServiceRequest request){
        if(request == null || request.getUrl() == null){
            logger.warn("Request got empty body/url!");
            return new WebPage();
        }

        List<WebPageParsingOption> options = request.getOptions();
        if(options == null || options.isEmpty()){
            options = Arrays.asList(TITLE, HEADERS);
        }

        return webPageService.getInfoOfWebPageWithParams(request.getUrl(), options);
    }
}
