package com.gmail.rgizmalkov.edu.projects.vo;

import lombok.Data;
import lombok.SneakyThrows;

import java.util.function.Supplier;

@Data
public class Result {
    public String message;
    public TaskResult data;

    public Result() {
    }

    public Result(String message, TaskResult data) {
        this.message = message;
        this.data = data;
    }

    public static Result success(final TaskResult data) {
        return new Result(null, data);
    }

    public static Result error(final String message) {
        return new Result(message, null);
    }

    public static Result run(final Supplier<TaskResult> function) {
        TaskResult taskResult = function.get();
        return taskResult.status == 0 ? new Result();
    }
}