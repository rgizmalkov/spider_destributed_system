package com.gmail.rgizmalkov.edu.projects.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

public class TaskResult {
    int status;
    String severity;
    String uid;
    String node;

    public TaskResult() {
    }

    public TaskResult(int status, String severity, String uid, String node) {
        this.status = status;
        this.severity = severity;
        this.uid = uid;
        this.node = node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TaskResult)) return false;
        TaskResult that = (TaskResult) o;
        return getStatus() == that.getStatus() &&
                Objects.equals(getSeverity(), that.getSeverity()) &&
                Objects.equals(getUid(), that.getUid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getSeverity(), getUid());
    }

    public int getStatus() {
        return status;
    }

    public TaskResult setStatus(int status) {
        this.status = status;
        return this;
    }

    public String getSeverity() {
        return severity;
    }

    public TaskResult setSeverity(String severity) {
        this.severity = severity;
        return this;
    }

    public String getUid() {
        return uid;
    }

    public TaskResult setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getNode() {
        return node;
    }

    public TaskResult setNode(String node) {
        this.node = node;
        return this;
    }
}
