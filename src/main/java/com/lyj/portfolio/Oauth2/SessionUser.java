package com.lyj.portfolio.Oauth2;

import com.lyj.portfolio.VO.Account;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String user_id;
    private String name;
    private String email;

    SessionUser() {}
    public SessionUser(Account account) {
        this.user_id = account.getUser_id();
        this.name = account.getName();
        this.email = account.getEmail();
    }
}
