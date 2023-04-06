package com.lyj.portfolio.config;

import com.lyj.portfolio.Oauth2.CustomOAuth2UserService;
import com.lyj.portfolio.login.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final DataSource dataSource;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/","/login","/account/sign-up","/account/done"
                        ,"/check-email-token","/account/checked-Email", "/board-index"
                        ,"/board-view","/mod-board","/board/board-modify","/remove-board",
                        "/board/view-reply", "/check-find-password",
                        "/account/find-password", "/account/change-password", "/searchBoard",
                        "/board/search-result", "/search-result",
                        "/pageTest", "/account/resend-email",
                        "/movie-index", "/movie-view","/edit-comment","/add-Comment",
                        "/generate","/login/oath2/code/google"
                        ).permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .deleteCookies("JSESSIONID").logoutSuccessUrl("/")
                .and()
                .rememberMe()
                .userDetailsService(customUserDetailsService)
                .tokenRepository(tokenRepository())
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
        ;

                return http.build();
        }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .mvcMatchers("/node_modules/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
