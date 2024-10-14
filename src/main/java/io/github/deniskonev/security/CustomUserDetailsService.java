package io.github.deniskonev.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class CustomUserDetailsService implements UserDetailsService {

    private static final String USER_NAME = "admin";
    private static final String PASSWORD = "admin";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (USER_NAME.equals(username)) {
            return new User(USER_NAME, PASSWORD, Collections.emptyList());
        } else {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
    }
}
