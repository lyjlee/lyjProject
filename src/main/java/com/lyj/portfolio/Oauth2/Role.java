package com.lyj.portfolio.Oauth2;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE GUEST"),
    USER("ROLE USER");

    private final String value;
}
