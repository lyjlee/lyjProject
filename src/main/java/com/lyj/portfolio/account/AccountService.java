package com.lyj.portfolio.account;

import com.lyj.portfolio.VO.Account;
import com.lyj.portfolio.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    private final AccountMapper accountMapper;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;



    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generatedEmailCheckToken();
        accountMapper.insertEmailCheckToken(newAccount);
        sendMailToken(newAccount);
        return newAccount;
    }

    public Account saveNewAccount(SignUpForm signUpForm) {
        Account account = Account.builder()
                .user_id(signUpForm.getUser_id())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .name(signUpForm.getName())
                .email(signUpForm.getEmail())
                .build();

        accountMapper.save(account);
        return account;
    }

    public void sendMailToken(Account newAccount) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("회원 가입 인증");
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken()
                + "&email=" + newAccount.getEmail());
        javaMailSender.send(mailMessage);
    }

    public void sendMailFindPassword(Account account) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("비밀번호 찾기 인증");
        mailMessage.setTo(account.getEmail());
        mailMessage.setText("/check-find-password?token=" + account.getFindPasswordToken()
                + "&email=" + account.getEmail());
        javaMailSender.send(mailMessage);
    }

    public Account findByEmail(String email) {
        return accountMapper.findByEmail(email);
    }


    public void finishNewAccount(Account newAccount) {
        newAccount.setEmailVerified(true);
        newAccount.setEmail(newAccount.getEmail());
        newAccount.setJoinedAt(LocalDateTime.now());
        accountMapper.verifiedAccount(newAccount);
    }

    public void login(Account account) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                new UserAccount(account), //principle 에 맞는 객체로 변경해줌.
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE USER")));

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountMapper.findByUserId(username);
        return new UserAccount(account);
    }

    public void applyChangedPassword(SignUpForm signUpForm) {
        String email = signUpForm.getEmail();
        String encode = passwordEncoder.encode(signUpForm.getPassword());
        accountMapper.updatePassword(encode, email);
    }
}
