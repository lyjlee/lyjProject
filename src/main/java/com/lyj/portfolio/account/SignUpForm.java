package com.lyj.portfolio.account;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SignUpForm {

    @NotBlank
    @Length(min =5, max =20)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{5,20}$")
    private String user_id;
    @NotBlank
    @Length(min = 8, max = 50)
    private String password;
    @Email
    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;
}
