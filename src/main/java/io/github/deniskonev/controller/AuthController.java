package io.github.deniskonev.controller;

import io.github.deniskonev.dto.AuthRequest;
import io.github.deniskonev.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/authenticate")
    public Map<String, String> authenticate(@RequestBody AuthRequest authRequest) {
        try {
            return Collections.singletonMap("token", jwtUtils.generateJwtToken(authRequest.getUsername()));
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
