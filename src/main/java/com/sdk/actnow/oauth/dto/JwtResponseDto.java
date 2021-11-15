package com.sdk.actnow.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtResponseDto {

    private String token;
    private String message;

    public JwtResponseDto(String message,String token){
        this.message = message;
        this.token = token;
    }

    public JwtResponseDto(String message){
        this.message = message;
        this.token = null;
    }
}
