package com.lyj.portfolio.login;

import com.lyj.portfolio.VO.Account;

import java.util.Map;

public class AccountAdapter extends CustomUserDetails {
    private Account account;
    private Map<String, Object> attributes;

    public AccountAdapter(Account account) {
        super(account);
        this.account = account;
    }

    public AccountAdapter(Account account, Map<String, Object> attribute) {
        super(account, attribute);
        this.account = account;
        this.attributes = attribute;
    }
}
