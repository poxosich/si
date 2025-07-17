package com.example.si.security;

import com.example.si.entity.User;
import com.example.si.exeption.UserNotFoundException;
import com.example.si.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private static final String MASSAGE = "User is not find";
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userService.findByEmail(username);
            return new SpringUser(user);
        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException(MASSAGE, e);
        }
    }
}

