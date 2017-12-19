package com.gmail.rgizmalkov.edu.projects.spider_aggregation.cg;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gmail.rgizmalkov.edu.projects.spider_aggregation.bs.CommonBusinessService;
import com.gmail.rgizmalkov.edu.projects.vo.SpiderServiceRequest;
import com.gmail.rgizmalkov.edu.projects.vo.WebPage;
import com.gmail.rgizmalkov.edu.projects.vo.WebPageParsingOption;
import com.gmail.rgizmalkov.edu.projects.vo.WebPageServiceRequest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("bridge")
public class PlatformBridge {

    private static final Logger logger = LoggerFactory.getLogger(PlatformBridge.class);

    @Autowired
    private CommonBusinessService commonBusinessService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    public void getWebInfo(@NonNull SpiderServiceRequest request) {

        int rqDeep = request.getDeep();

        while (rqDeep > 0) {
            rqDeep--;
            Optional<WebPage> singleWebInfo = getSingleWebInfo(request.getUrl(), request.getOptions());
            singleWebInfo.ifPresent((page) -> {
                        threadPoolTaskExecutor.execute(() -> {
                            try {
                                commonBusinessService.save(page);
                            } catch (Exception e) {
                                logger.warn("Cannot save web page!");
                            }

                        });
                    }
            );
            List<String> urls = singleWebInfo.map(WebPage::getUrls).orElse(new ArrayList<>());
            if(urls.size() > 50) urls = urls.subList(0, 50);
            for (String webPageUrls : urls) {
                getWebInfo(new SpiderServiceRequest(
                        webPageUrls,
                        rqDeep,
                        request.getOptions()
                ));
            }
        }

    }


    public Optional<WebPage> getSingleWebInfo(@NonNull String url, List<WebPageParsingOption> options) {
        return Optional.ofNullable(commonBusinessService.post(
                new WebPageServiceRequest(
                        url, options
                ),
                WebPage.class
        ));
    }
}
