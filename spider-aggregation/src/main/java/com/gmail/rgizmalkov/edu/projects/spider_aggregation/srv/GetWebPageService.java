package com.gmail.rgizmalkov.edu.projects.spider_aggregation.srv;

import com.gmail.rgizmalkov.edu.projects.spider_aggregation.srv.errors.NotValidURL;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;

@Service("getWebPageService")
public class GetWebPageService {
    private static final Logger logger = LoggerFactory.getLogger(GetWebPageService.class);

    private static final String[] schemes = {"http", "https"};


    public String getByUrl(String url) {
        if (validateUrl(url)) {
            try (InputStream inputStream = new URL(url).openStream()) {
                return IOUtils.toString(inputStream, "UTF-8");
            } catch (Exception ex) {
                logger.warn("Error during attempting to get info from web-site page!\n--" + url);
            }
        } else {
            logger.warn("The URL of web-site page is not valid!\n--" + url);
        }
        return null;
    }

    private boolean validateUrl(String url) {
        UrlValidator urlValidator = new UrlValidator(schemes);

        return urlValidator.isValid(url);
    }

}
