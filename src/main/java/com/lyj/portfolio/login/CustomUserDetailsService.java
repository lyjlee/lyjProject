package com.lyj.portfolio.login;

import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.account.UserAccount;
import com.lyj.portfolio.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountMapper.findByUserId(username);
        return new AccountAdapter(account);
    }

// ROLE 부여 되는건지 안되는건지 모르겠지만 오류없이 정상작동 되긴 함.
    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new AccountAdapter(account), //principle 에 맞는 객체로 변경해줌.
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE USER")));

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }
}
