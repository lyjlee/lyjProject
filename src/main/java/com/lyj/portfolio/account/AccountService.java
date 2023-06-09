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
public class AccountService
{
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
        mailMessage.setText("http://lyjproject.p-e.kr/check-email-token?token=" + newAccount.getEmailCheckToken()
                + "&email=" + newAccount.getEmail() + " <- 주소를 입력하여 인증을 진행해주세요.");
        javaMailSender.send(mailMessage);
    }

    public void sendMailFindPassword(Account account) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("비밀번호 찾기 인증");
        mailMessage.setTo(account.getEmail());
        mailMessage.setText("http://lyjproject.p-e.kr/check-find-password?token=" + account.getFindPasswordToken()
                + "&email=" + account.getEmail() + " <- 주소를 입력하여 인증을 진행해주세요.");
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

    public void applyChangedPassword(SignUpForm signUpForm) {
        String email = signUpForm.getEmail();
        String encode = passwordEncoder.encode(signUpForm.getPassword());
        accountMapper.updatePassword(encode, email);
    }

    public Account updateAccount(SignUpForm signUpForm, String user_id) {
        if(signUpForm.getPassword() != null) {
            accountMapper.updateAccount(signUpForm, user_id);
            return accountMapper.findByUserId(user_id);
        }else {
            accountMapper.updateWithoutPwd(signUpForm, user_id);
            return accountMapper.findByUserId(user_id);
        }
    }

    public boolean checkPassword(String password, String user_id) {
        Account account = accountMapper.findByUserId(user_id);
        return passwordEncoder.matches(password,account.getPassword());
    }
}
