package com.gmail.rgizmalkov.edu.projects.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeResponse<T> {
    int code;
    String severity;
    String desc;
    String urlForWriteInfo;
    T response;
}
