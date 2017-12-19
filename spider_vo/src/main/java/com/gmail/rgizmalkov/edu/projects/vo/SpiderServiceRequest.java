package com.gmail.rgizmalkov.edu.projects.vo;

        import lombok.Data;

        import java.util.List;

@Data
public class SpiderServiceRequest {

    private String url;
    private int deep;
    private List<WebPageParsingOption> options;

    public SpiderServiceRequest() {
    }

    public SpiderServiceRequest(String url, int deep, List<WebPageParsingOption> options) {
        this.url = url;
        this.deep = deep;
        this.options = options;
    }
}
