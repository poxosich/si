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

                .requestMatchers("/").permitAll()
                .requestMatchers("/login/login-page").permitAll()
                .requestMatchers("/register").permitAll()
                .requestMatchers("/register/confirmation").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/admin/product").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/v1/admin/product/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/admin-category").permitAll()
                .requestMatchers("/product/**").permitAll()
                .requestMatchers("/getImage").permitAll()
                .requestMatchers("/liked/{id}").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .requestMatchers("/shop/**").permitAll()
                .requestMatchers("/basket").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .requestMatchers("/order").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .requestMatchers("/basket/delete").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())

                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login/login-page")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/login/success")
                .and()
                .logout()
                .logoutSuccessUrl("/");
        return httpSecurity.build();
    }
}
