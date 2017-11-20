package com.gmail.rgizmalkov.edu.projects.vo;

import lombok.Value;

import java.util.List;

@Value public class WebPageServiceRequest {
    private String url;
    private List<WebPageParsingOption> options;
}
