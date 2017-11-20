package com.gmail.rgizmalkov.edu.projects.vo;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@Data
public class WebPage {
    private String url;
    private List<String> head;
    private List<Header> headers;
    private String title;
    private List<String> urls;

    public WebPage() {
    }

    public WebPage(String url) {
        this.url = url;
    }
}
