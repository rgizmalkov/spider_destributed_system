package com.gmail.rgizmalkov.edu.projetcs.destriburted_system.registr;

import lombok.Data;

@Data
public class Message {
    private String title;
    private String message;
    private Status status;

    public static enum Status{
        RDY_TO_SEND, SEND, TIME_OUT, OK
    }
}
