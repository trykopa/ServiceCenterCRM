package com.googe.ssadm.sc.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    private final
    UserDetailsService userDetailsService;

    public WebSecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers( "/favicon.ico").permitAll()
                .antMatchers( "/static/**").permitAll()
                .antMatchers("/users/**").hasRole("ADMIN")
                .antMatchers("/files/**").hasRole("ADMIN")
                .antMatchers("/news/**").hasRole("ADMIN")
                .antMatchers("/parts/**").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/transactions/**").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/suppliers/**").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/").hasAnyRole("ADMIN", "USER", "MANAGER")
                .antMatchers("/table/**").hasAnyRole("ADMIN", "USER", "MANAGER")
                .antMatchers("/orders/**").hasAnyRole("ADMIN", "USER", "MANAGER")
                .antMatchers("/getparts").hasAnyRole("ADMIN", "USER", "MANAGER")
                .antMatchers("/getmess/**").hasAnyRole("ADMIN", "USER", "MANAGER")
                .antMatchers("/messages/**").hasAnyRole("ADMIN", "USER", "MANAGER")
                .and()
                    .formLogin().usernameParameter("email").permitAll()
                .and()
                    .logout().permitAll().logoutSuccessUrl("/login")
                .and()
                    .rememberMe();
    }
}
