package com.gmail.rgizmalkov.edu.projects.spider_aggregation.srv.errors;

public class NotValidURL extends RuntimeException {
    public NotValidURL() {
        super();
    }

    public NotValidURL(String message) {
        super(message);
    }

    public NotValidURL(String message, Throwable cause) {
        super(message, cause);
    }

    public NotValidURL(Throwable cause) {
        super(cause);
    }

    protected NotValidURL(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
