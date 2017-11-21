package com.gmail.rgizmalkov.edu.projects.spider_aggregation.cg;

import com.gmail.rgizmalkov.edu.projects.spider_aggregation.bs.CommonBusinessService;
import com.gmail.rgizmalkov.edu.projects.vo.SpiderServiceRequest;
import com.gmail.rgizmalkov.edu.projects.vo.WebPage;
import com.gmail.rgizmalkov.edu.projects.vo.WebPageParsingOption;
import com.gmail.rgizmalkov.edu.projects.vo.WebPageServiceRequest;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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


    public List<WebPage> getWebInfo(@NonNull SpiderServiceRequest request) {
        List<WebPage> webPages = new ArrayList<>();

        int rqDeep = request.getDeep();

        while (rqDeep > -1) {
            rqDeep--;
            Optional<WebPage> singleWebInfo = getSingleWebInfo(request.getUrl(), request.getOptions());
            singleWebInfo.ifPresent(
                    webPages::add
            );

            List<String> urls = singleWebInfo.map(WebPage::getUrls).orElse(new ArrayList<>());
            if(urls.size() > 15) urls = urls.subList(0, 15);
            for (String webPageUrls : urls) {
                List<WebPage> webInfo = getWebInfo(new SpiderServiceRequest(
                        webPageUrls,
                        rqDeep,
                        request.getOptions()
                ));
                if(webInfo != null) webPages.addAll(webInfo);
            }
        }

        return webPages;
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
