package com.lyj.portfolio.validator;

import com.lyj.portfolio.account.SignUpForm;
import com.lyj.portfolio.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {
    private final AccountMapper accountMapper;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm signUpForm=(SignUpForm) target;
        if(accountMapper.existsUserId(signUpForm)){
            errors.rejectValue("user_id" , "invalid.user_id",
                    new Object[]{signUpForm.getUser_id()},
                    "이미 사용중인 아이디입니다.");
        }
        if(accountMapper.existsEmail(signUpForm)){
            errors.rejectValue("email" , "invalid.email",
                    new Object[]{signUpForm.getEmail()},
                    "이미 사용중인 이메일입니다.");
        }
    }
}
