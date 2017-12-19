package com.gmail.rgizmalkov.edu.projects.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeResponse {
    int code;
    String severity;
    String desc;
    ServiceQueueEntity response;
}
