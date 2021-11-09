package com.sdk.actnow.util;

import lombok.Data;

@Data
public class Message {

    private String message;

    public Message() {
        this.message = null;
    }
}
