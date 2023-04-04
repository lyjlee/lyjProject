package com.lyj.portfolio.config;

import com.lyj.portfolio.Oauth2.Oauth2Service;
import com.lyj.portfolio.Oauth2.Role;
import com.lyj.portfolio.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig /*extends WebSecurityConfigurerAdapter*/ {

    private final AccountService accountService;
    private final DataSource dataSource;
    private final Oauth2Service oauth2Service;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .and()
//                .headers().frameOptions().disable()
//                .and()
                .authorizeRequests()
                .antMatchers("/","/login","/account/sign-up","/account/done"
                        ,"/check-email-token","/account/checked-Email", "/board-index"
                        ,"/board-view","/mod-board","/board/board-modify","/remove-board",
                        "/board/view-reply", "/check-find-password",
                        "/account/find-password", "/account/change-password", "/searchBoard",
                        "/board/search-result", "/search-result",
                        "/pageTest", "/account/resend-email",
                        "/movie-index", "/movie-view","/edit-comment","/add-Comment",
                        "/generate","/google-callback"
                        ).permitAll()
//                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .and()
                .logout()
                .deleteCookies("JSESSIONID").logoutSuccessUrl("/")
                .and()
                .rememberMe()
                .userDetailsService(accountService)
                .tokenRepository(tokenRepository())
                .and()
                .oauth2Login()
                .loginPage("/login")
                .defaultSuccessUrl("/",true)
                .userInfoEndpoint()
                .userService(oauth2Service);

                return http.build();
        }
//    기존 configure 활용 코드
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests()
//                .mvcMatchers("/","/login","/account/sign-up","/account/done"
//                        ,"/check-email-token","/account/checked-Email", "/board-index"
//                        ,"/board-view","/mod-board","/board/board-modify","/remove-board",
//                        "/board/view-reply", "/check-find-password",
//                        "/account/find-password", "/account/change-password", "/searchBoard",
//                        "/board/search-result", "/search-result",
//                        "/pageTest", "/account/resend-email",
//                        "/movie-index", "/movie-view","/edit-comment","/add-Comment",
//                        "/login-google"
//                        ).permitAll()
//                .anyRequest().authenticated();
//
//        http.formLogin()
//                .loginPage("/login").permitAll();
//        http.logout()
//                .logoutSuccessUrl("/");
//        http.rememberMe()
//                .userDetailsService(accountService)
//                .tokenRepository(tokenRepository());
//    }
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }



//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .mvcMatchers("/node_modules/**")
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .mvcMatchers("/node_modules/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
