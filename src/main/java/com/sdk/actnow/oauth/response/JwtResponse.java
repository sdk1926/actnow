package com.sdk.actnow.oauth.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponse {

    private String token;
    private String message;

    public JwtResponse(String message, String token){
        this.message = message;
        this.token = token;
    }

    public JwtResponse(String message){
        this.message = message;
        this.token = null;
    }
}
