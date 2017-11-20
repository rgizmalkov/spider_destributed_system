package com.gmail.rgizmalkov.edu.projects.vo;

import lombok.Data;

@Data
public class Header {
    private String scale;
    private String text;

    public Header(String scale, String text) {
        this.scale = scale;
        this.text = text;
    }
}
