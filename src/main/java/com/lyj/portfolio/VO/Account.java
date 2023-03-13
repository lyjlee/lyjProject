package com.lyj.portfolio.VO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter @EqualsAndHashCode(of = "user_id")
@Builder @AllArgsConstructor @NoArgsConstructor
public class Account {

    private String user_id;
    private String password;
    private String name;
    private String email;
    private boolean emailVerified;
    private String emailCheckToken;
    private LocalDateTime joinedAt;

    private String findPasswordToken;

    public void generatedEmailCheckToken() {
        this.emailCheckToken = UUID.randomUUID().toString();
        this.joinedAt = LocalDateTime.now();
    }

    public void generatedFindPasswordToken() {
        this.findPasswordToken = UUID.randomUUID().toString();
    }

    public boolean isValidToken(String token) {
        return this.emailCheckToken.equals(token);
    }

    public boolean isValidPasswordToken(String token) {
        return this.findPasswordToken.equals(token);
    }
}
