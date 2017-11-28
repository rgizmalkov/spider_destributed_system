package com.gmail.rgizmalkov.edu.projects.vo;

import lombok.Data;

@Data
public class TaskResult {
    int status;
    String severity;
    String uid;

    public TaskResult() {
    }

    public TaskResult(int status, String severity, String uid) {
        this.status = status;
        this.severity = severity;
        this.uid = uid;
    }
}
