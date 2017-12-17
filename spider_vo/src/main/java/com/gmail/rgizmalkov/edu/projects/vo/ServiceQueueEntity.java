package com.gmail.rgizmalkov.edu.projects.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class ServiceQueueEntity implements AppId {
    String uid;
    String json;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ServiceQueueEntity that = (ServiceQueueEntity) o;
        return Objects.equals(uid, that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uid);
    }

    @Override
    public String getAppId() {
        return uid;
    }

    @Override
    public void setAppId(String uid) {
        this.uid = uid;
    }
}
