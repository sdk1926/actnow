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
}
