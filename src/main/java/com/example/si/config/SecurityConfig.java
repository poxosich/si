package com.example.si.config;

import com.example.si.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/style/css/**").permitAll()
                .requestMatchers("/style/images/**").permitAll()
                .requestMatchers("/style/js/**").permitAll()
                .requestMatchers("/style/webfonts/**").permitAll()

                .requestMatchers("/v1/").permitAll()
                .requestMatchers("/v1/login/login-page").permitAll()
                .requestMatchers("/v1/register").permitAll()
                .requestMatchers("/v1/register/confirmation").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/admin/product").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/v1/admin/product/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/admin-category").permitAll()
                .requestMatchers("/v1/product/**").permitAll()
                .requestMatchers("/grtImage").permitAll()
                .requestMatchers("/v1/liked/{id}").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .requestMatchers("/v1/shop/**").permitAll()
                .requestMatchers("/v1/shop").permitAll()
                .requestMatchers("/v1/basket").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .requestMatchers("/v1/order").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .requestMatchers("/v1/basket/delete").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())

                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/v1/login/login-page")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/v1/login/success")
                .and()
                .logout()
                .logoutSuccessUrl("/v1/");


        return httpSecurity.build();
    }
}
