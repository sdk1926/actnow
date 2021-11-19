package com.sdk.actnow.util;

import lombok.Data;

@Data
public class Message {

    private String message;
    private long id;
    public Message() {
        this.message = null;
        this.id = 0;
    }
    public Message(String message){
        this.message = message;
        this.id = 0;
    }

    public Message(String message, long id){
        this.message = message;
        this.id = id;
    }
}
