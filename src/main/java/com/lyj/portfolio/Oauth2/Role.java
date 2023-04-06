package com.lyj.portfolio.Oauth2;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER");

    private final String value;
}
