package com.gmail.rgizmalkov.edu.projects.webpageparser.srv;

import com.gmail.rgizmalkov.edu.projects.vo.Header;
import com.gmail.rgizmalkov.edu.projects.vo.WebPage;
import com.gmail.rgizmalkov.edu.projects.vo.WebPageParsingOption;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service("getWebPageService")
public class GetWebPageService {
    private static final Logger logger = LoggerFactory.getLogger(GetWebPageService.class);

    private static final String[] schemes = {"http", "https"};


    public String getByUrl(String url) {
        if (validateUrl(url)) {
            try (InputStream inputStream = new URL(url).openStream()) {
                return IOUtils.toString(inputStream, "windows-1251");
            } catch (Exception ex) {
                logger.warn("Error during attempting to get info from web-site page!\n--" + url);
            }
        } else {
            logger.warn("The URL of web-site page is not valid!\n--" + url);
        }
        return null;
    }

    @SneakyThrows
    public Elements getLinksByUrl(String url) {
        if (validateUrl(url)) {
            Document document = Jsoup.connect(url).get();

            return document.select("a[href]");
        }
        return null;
    }


    private boolean validateUrl(String url) {
        UrlValidator urlValidator = new UrlValidator(schemes);

        return urlValidator.isValid(url);
    }

    @SneakyThrows
    public WebPage getInfoOfWebPageWithParams(String url, List<WebPageParsingOption> options) {
        if (validateUrl(url)) {
            WebPage webPage = new WebPage(url);
            Document document = Jsoup.connect(url).get();

            for (WebPageParsingOption option : options) {
                switch (option) {
                    case TITLE:
                        webPage.setTitle(document.title());
                        break;
                    case URLS:
                        webPage.setUrls(
                                document.select("a[href]").stream()
                                        .map((element -> element.attr("abs:href")))
                                        .collect(Collectors.toList())
                        );
                        break;
                    case HEAD:
                        webPage.setHead(
                                document.head().getAllElements().stream()
                                        .skip(1)
                                        .map(Element::toString)
                                        .collect(Collectors.toList())
                        );
                        break;
                    case HEADERS:
                        webPage.setHeaders(
                                document.select("h1, h2, h3, h4, h5, h6").stream()
                                        .map(element -> new Header(element.tagName(), element.text()))
                                        .collect(Collectors.toList())
                        );
                        break;
                }
            }
            return webPage;
        } else {
            logger.warn("Error during attempt get info from: " + url);
            return new WebPage();
        }
    }
}
