package com.sdk.actnow.util;

import lombok.Data;

@Data
public class Message {

    private String message;
    private Long id;
    public Message() {
        this.message = null;
        this.id = null;
    }
    public Message(String message){
        this.message = message;
        this.id = null;
    }

    public Message(String message, Long id){
        this.message = message;
        this.id = id;
    }
}
